package nodes;

import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceGrpc.NodeServiceImplBase;
import com.example.token.NodeServiceOuterClass.TokenData;
import com.example.token.NodeServiceOuterClass.TokenData.Ready;
import com.example.token.NodeServiceOuterClass.TokenData.Waiting;
import com.example.token.NodeServiceOuterClass.TokenData.Delete;
import com.example.token.NodeServiceOuterClass.Empty;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.sql.Timestamp;

public class NodeServiceImpl extends NodeServiceImplBase {

    Node node;

    public NodeServiceImpl(Node node) {
        this.node = node;
    }

    // service called to join the network
    // synchronized to prevent to prevent two nodes joining at the same time
    @Override
    public synchronized void joinNetwork(JoinRequest joinRequest,
                                         StreamObserver<JoinResponse> joinResponseStreamObserver) {

        System.out.println("Ehy, I'm node " + joinRequest.getId() +
                " - " + joinRequest.getPort() + " join me as your target.");

        JoinResponse joinResponse = JoinResponse.newBuilder()
                .setId(TargetNode.getInstance().getTargetId())
                .setIpAddress(TargetNode.getInstance().getTargetIpAddress())
                .setPort(TargetNode.getInstance().getTargetPort())
                .setMessage("success").build();

        // update the target of the node receiving the join request
        TargetNode.getInstance().setTargetId(joinRequest.getId());
        TargetNode.getInstance().setTargetIpAddress(joinRequest.getIpAddress());
        TargetNode.getInstance().setTargetPort(joinRequest.getPort());

        joinResponseStreamObserver.onNext(joinResponse);
        joinResponseStreamObserver.onCompleted();

    }


    @Override
    public synchronized void tokenDeliveryData(TokenData tokenData,
                                               StreamObserver<Empty> responseObserver) {


        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("Arrivato token a nodo " + node.getId());
        System.out.println("Da cancellare? " + NodeStatus.getInstance().isDelete());



        TokenData newTokenData = tokenData.toBuilder().build();
        boolean wantToEsc = NodeStatus.getInstance().isDelete();


        // SE VUOLE USCIRE INSERISCE NEL TOKEN CHE VUOLE USCIRE
        if (wantToEsc) {
            newTokenData = insertDeleteOnToken(newTokenData);
            newTokenData = deleteFromReadyWaitingListOnToken(newTokenData);
            System.out.println("INSERT NODE " + node.getId() + " ON DELETE LIST");

            System.out.println("POST DELETE:");
            System.out.println(postDeleteOnGateway(node));


            /* ------------------ CREATE CHANNEL -------------------- */
            ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(
                            TargetNode.getInstance().getTargetIpAddress() + ":" +
                                    TargetNode.getInstance().getTargetPort())
                    .usePlaintext(true).build();
            NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);


            stub.tokenDeliveryData(newTokenData);
            channel.shutdown();
        } else {


            /* ------------- CHECK IF NODE IS INSIDE READY AND WAITING ------------- */
            boolean insideReady = isInsideReady(newTokenData);
            boolean insideWaiting = isInsideWaiting(newTokenData);


            /* ------------ CHECK IF TARGET IS TO BE DELETED ----------- */
            if (isTargetInsideDelete(newTokenData)) {
                newTokenData = updateTargetFromDeleteList(newTokenData);
            }


            /* ------------------ CREATE CHANNEL -------------------- */
            ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(
                            TargetNode.getInstance().getTargetIpAddress() + ":" +
                                    TargetNode.getInstance().getTargetPort())
                    .usePlaintext(true).build();
            NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);



            /* ------------------ CHECK TOKEN CONTENT ---------------- */

            // check if token is full of stats and ready to send
            if (isTokenFullAndReadyToSend(newTokenData)) {
                double finalAvg = computeFinalAvg(newTokenData);

                // post the stats to the gateway
                sendStatsToGateway(finalAvg);
                System.out.println("SENT STATS TO THE GATEWAY " + finalAvg);

                // clean the ready and waiting list from the token
                newTokenData = cleanReadyListFromToken(newTokenData);
                newTokenData = cleanWaitingListFromToken(newTokenData);

                stub.tokenDeliveryData(newTokenData);
                channel.shutdown();

            } else if (isStatReady()) {

                // stats is READY
                if (insideReady) {
                    System.out.println("1");
                    stub.tokenDeliveryData(newTokenData);
                    channel.shutdown();
                } else if (insideWaiting) {
                    System.out.println("2");
                    newTokenData = newTokenData.toBuilder()
                            .addReady(TokenData.Ready.newBuilder()
                                    .setId(node.getId())
                                    .setValue(LocalAvgList.getInstance().getLastValue()))
                            .build();
                    stub.tokenDeliveryData(newTokenData);
                    channel.shutdown();
                } else if (!insideReady && !insideWaiting) {
                    newTokenData = newTokenData.toBuilder()
                            .addReady(TokenData.Ready.newBuilder()
                                    .setId(node.getId())
                                    .setValue(LocalAvgList.getInstance().getLastValue()))
                            .addWaiting(TokenData.Waiting.newBuilder()
                                    .setId(node.getId()))
                            .build();
                    stub.tokenDeliveryData(newTokenData);
                    channel.shutdown();
                }

            } else {

                // stats IS NOT READY
                if (insideReady || insideWaiting) {
                    System.out.println("3");

                    stub.tokenDeliveryData(newTokenData);
                    channel.shutdown();
                    System.out.println("QUI NON ARRIVO");
                } else if (!insideWaiting) {
                    System.out.println("4");
                    newTokenData = newTokenData.toBuilder()
                            .addWaiting(TokenData.Waiting.newBuilder()
                                    .setId(node.getId()).build())
                            .build();
                    stub.tokenDeliveryData(newTokenData);
                    channel.shutdown();
                }

            }

            System.out.println("Invio token a " + TargetNode.getInstance().getTargetId());
            System.out.println("Ready list:\n" + newTokenData.getReadyList());
            System.out.println("Waiting list:\n" + newTokenData.getWaitingList());
            System.out.println("Delete list:\n" + newTokenData.getDeleteList());

        }


        ///* SLEEP
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //*/

    }

    // return true if ready list equals waiting list and node is in ready list
    public boolean isTokenFullAndReadyToSend(TokenData tokenData) {
        return (tokenData.getReadyCount() == tokenData.getWaitingCount()) && isInsideReady(tokenData);
    }

    // return true if stat is ready
    public boolean isStatReady() {
        return LocalAvgList.getInstance().getSize() >= 1;
    }

    // insert node id and target on token
    public TokenData insertDeleteOnToken(TokenData tokenData) {

        tokenData = tokenData.toBuilder()
                .addDelete(TokenData.Delete.newBuilder()
                        .setId(node.getId())
                        .setTargetId(TargetNode.getInstance().getTargetId())
                        .setTargetIpAddress(TargetNode.getInstance().getTargetIpAddress())
                        .setTargetPort(TargetNode.getInstance().getTargetPort()))
                .build();

        return tokenData;
    }

    public TokenData deleteFromReadyWaitingListOnToken(TokenData tokenData) {
        TokenData temp = TokenData.newBuilder().build();
        // DELETE THE NODE FROM READY LIST
        for (Ready item : tokenData.getReadyList()) {
            if (item.getId() != node.getId()) {
                temp = temp.toBuilder().addReady(TokenData.Ready.newBuilder()
                        .setId(item.getId())
                        .setValue(item.getValue()))
                        .build();
            }
        }
        // DELETE THE NODE FROM WAITING LIST
        for (Waiting item : tokenData.getWaitingList()) {
            if (item.getId() != node.getId()) {
                temp = temp.toBuilder().addWaiting(TokenData.Waiting.newBuilder()
                        .setId(item.getId()))
                        .build();
            }
        }
        // SET THE SAME DELETE LIST
        for (Delete item : tokenData.getDeleteList()) {
            temp = temp.toBuilder().addDelete(TokenData.Delete.newBuilder()
                    .setId(item.getId())
                    .setTargetId(item.getTargetId())
                    .setTargetIpAddress(item.getTargetIpAddress())
                    .setTargetPort(item.getTargetPort()))
                    .build();
        }
        return temp;
    }

    // return true if the node is inside ready list on the token
    public boolean isInsideReady(TokenData tokenData) {
        for (Ready item : tokenData.getReadyList()) {
            if (item.getId() == node.getId()) {
                return true;
            }
        }
        return false;
    }

    // return true if the node is inside waiting list on the token
    public boolean isInsideWaiting(TokenData tokenData) {
        for (Waiting item : tokenData.getWaitingList()) {
            if (item.getId() == node.getId()) {
                return true;
            }
        }
        return false;
    }

    // return true if the target is inside delete list on the token
    public boolean isTargetInsideDelete(TokenData tokenData) {
        for (Delete item : tokenData.getDeleteList()) {
            if (item.getId() == TargetNode.getInstance().getTargetId()) {
                return true;
            }
        }
        return false;
    }

    // set the new target based on delete list so, if your target is inside
    // the list update it until there is no target inside this list
    public TokenData updateTargetFromDeleteList(TokenData tokenData) {
        int tempTargetId, tempTargetPort;
        String tempTargetIpAddress;
        while (isTargetInsideDelete(tokenData)) {
            for (Delete item : tokenData.getDeleteList()) {
                if (item.getId() == TargetNode.getInstance().getTargetId()) {
                    tempTargetId = item.getTargetId();
                    tempTargetIpAddress = item.getTargetIpAddress();
                    tempTargetPort = item.getTargetPort();
                    System.out.println(tempTargetId);
                    tokenData = deleteTargetFromToken(tokenData);
                    TargetNode.getInstance().setTargetId(tempTargetId);
                    TargetNode.getInstance().setTargetIpAddress(tempTargetIpAddress);
                    TargetNode.getInstance().setTargetPort(tempTargetPort);
                }
            }
        }
        return tokenData;
    }

    // delete the target from the token if it is inside
    public TokenData deleteTargetFromToken(TokenData tokenData) {
        TokenData temp = TokenData.newBuilder().build();
        // SET THE SAME READY LIST
        for (Ready item : tokenData.getReadyList()) {
            temp = temp.toBuilder().addReady(TokenData.Ready.newBuilder()
                    .setId(item.getId())
                    .setValue(item.getValue()))
                    .build();
        }
        // SET THE SAME WAITING LIST
        for (Waiting item : tokenData.getWaitingList()) {
            temp = temp.toBuilder().addWaiting(TokenData.Waiting.newBuilder()
                    .setId(item.getId()))
                    .build();
        }
        // SET THE DELETE LIST WITHOUT THE ONE UPDATED
        for (Delete item : tokenData.getDeleteList()) {
            if (!isTargetInsideDelete(tokenData)) {
                temp = temp.toBuilder().addDelete(TokenData.Delete.newBuilder()
                        .setId(item.getId())
                        .setTargetId(item.getTargetId())
                        .setTargetIpAddress(item.getTargetIpAddress())
                        .setTargetPort(item.getTargetPort()))
                        .build();
            }
        }

        return temp;
    }

    // remove all the ready list
    public TokenData cleanReadyListFromToken(TokenData tokenData) {
        TokenData temp = TokenData.newBuilder().build();
        // SET THE SAME WAITING LIST
        for (Waiting item : tokenData.getWaitingList()) {
            temp = temp.toBuilder().addWaiting(TokenData.Waiting.newBuilder()
                    .setId(item.getId()))
                    .build();
        }
        // SET THE SAME DELETE LIST
        for (Delete item : tokenData.getDeleteList()) {
            temp = temp.toBuilder().addDelete(TokenData.Delete.newBuilder()
                    .setId(item.getId())
                    .setTargetId(item.getTargetId())
                    .setTargetIpAddress(item.getTargetIpAddress())
                    .setTargetPort(item.getTargetPort()))
                    .build();
        }
        return temp;
    }

    // remove all the waiting list
    public TokenData cleanWaitingListFromToken(TokenData tokenData) {
        TokenData temp = TokenData.newBuilder().build();
        // SET THE SAME READY LIST
        for (Ready item : tokenData.getReadyList()) {
            temp = temp.toBuilder().addReady(TokenData.Ready.newBuilder()
                    .setId(item.getId())
                    .setValue(item.getValue()))
                    .build();
        }
        // SET THE SAME DELETE LIST
        for (Delete item : tokenData.getDeleteList()) {
            temp = temp.toBuilder().addDelete(TokenData.Delete.newBuilder()
                    .setId(item.getId())
                    .setTargetId(item.getTargetId())
                    .setTargetIpAddress(item.getTargetIpAddress())
                    .setTargetPort(item.getTargetPort()))
                    .build();
        }
        return temp;
    }

    // compute the avg of all the stats in ready list
    public double computeFinalAvg(TokenData tokenData) {

        double result = 0.0;
        int count = 0;

        // CALCOLA LA MEDIA FINALE ITERANDO SU READY LIST
        for (Ready item : tokenData.getReadyList()) {
            result += item.getValue();
            count++;
        }

        return result / count;
    }

    // send stats to the gateway
    public String sendStatsToGateway(double avg) {
        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:1200/stats/add");

        ClientResponse response;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String jsonStr = "{\"value\":" + avg + ",\"timestamp\":\"" + timestamp + "\"}";


        response = webResource.accept("application/json").type("application/json")
                .post(ClientResponse.class, jsonStr);

        if (response.getStatus() == 409) {
            return "error409";
        }

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed - HTTP error code : "
                    + response.getStatus());
        }
        return response.getEntity(String.class);

    }

    // send delete request to the gateway
    public String postDeleteOnGateway(Node node) {
        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:1200/nodes/remove");

        ClientResponse response;

        String jsonStr;
        ObjectMapper mapper = new ObjectMapper();


        // get node object as a json string
        try {
            jsonStr = mapper.writeValueAsString(node);

            System.out.println(jsonStr);

            response = webResource.accept("application/json").type("application/json")
                    .delete(ClientResponse.class, jsonStr);

            System.out.println(response.getStatus());

            if (response.getStatus() == 409) {
                return "error409";
            }

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed - HTTP error code : "
                        + response.getStatus());
            }
            return response.getEntity(String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }


}
