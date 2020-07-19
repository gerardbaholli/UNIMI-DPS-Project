package network;

import com.example.token2.TokenServiceGrpc;
import com.example.token2.TokenServiceGrpc.TokenServiceBlockingStub;
import com.example.token2.TokenServiceGrpc.TokenServiceImplBase;
import com.example.token2.TokenServiceOuterClass.Empty;
import com.example.token2.TokenServiceOuterClass.Token;
import com.example.token2.TokenServiceOuterClass.JoinResponse;
import com.example.token2.TokenServiceOuterClass.JoinRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.sql.Timestamp;


public class TokenServiceImpl extends TokenServiceImplBase {

    @Override
    public void joinNetwork(JoinRequest joinRequest, StreamObserver<JoinResponse> joinResponseStreamObserver) {

        System.out.println("Ehy, I'm node " + joinRequest.getId() +
                " - " + joinRequest.getPort() + " join me as your target.");

        JoinResponse joinResponse = JoinResponse.newBuilder()
                .setId(Node.getInstance().getTargetId())
                .setIpAddress(Node.getInstance().getTargetIpAddress())
                .setPort(Node.getInstance().getTargetPort())
                .setMessage("Success").build();

        // update the target of the node receiving the join request
        updateTarget(joinRequest.getId(), joinRequest.getIpAddress(), joinRequest.getPort());
        shutdownChannel();
        updateTargetChannel();

        joinResponseStreamObserver.onNext(joinResponse);
        joinResponseStreamObserver.onCompleted();

    }

    @Override
    public void tokenDelivery(Token token, StreamObserver<Empty> response) {

        Empty empty = Empty.newBuilder().build();
        response.onNext(empty);
        response.onCompleted();

        System.out.println("Arrivato token a nodo " + Node.getInstance().getId());
        System.out.println("DELETE? " + Node.getInstance().isToDelete());

        /*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        Token newToken = token.toBuilder().build();

        if (wantToGoOut()) {
            newToken = insertDeleteOnToken(newToken);
            newToken = deleteFromReadyWaitingListOnToken(newToken);
            System.out.println("INSERT NODE " + Node.getInstance().getId() + " ON DELETE LIST");
            System.out.println("POST DELETE:");
            System.out.println(postDeleteOnGateway());

            // last one node doesn't send the token
            if (Node.getInstance().getTargetId() != Node.getInstance().getId()) {
                TokenServiceBlockingStub stub = TokenServiceGrpc.newBlockingStub(getChannel());
                stub.tokenDelivery(newToken);
            }
            shutdownChannel();
        } else {
            computeToken(newToken);
        }

        System.out.println("Invio token a " + Node.getInstance().getTargetId());
        System.out.println("Ready list:\n" + newToken.getReadyList());
        System.out.println("Waiting list:\n" + newToken.getWaitingList());
        System.out.println("Delete list:\n" + newToken.getDeleteList());

    }

    // shutdown the channel
    public void shutdownChannel() {
        Channel.getInstance().shutdownChannel();
    }

    // return the channel
    public ManagedChannel getChannel() {
        return Channel.getInstance().getChannel();
    }

    // update the target node
    public void updateTarget(int id, String ipAddress, int port) {
        synchronized (Node.getInstance()) {
            Node.getInstance().setTargetId(id);
            Node.getInstance().setTargetIpAddress(ipAddress);
            Node.getInstance().setTargetPort(port);
        }
    }

    // update the channel
    public void updateTargetChannel() {
        synchronized (Node.getInstance()) {
            synchronized (Channel.getInstance()) {
                Channel.getInstance().setChannel(
                        Node.getInstance().getTargetIpAddress(),
                        Node.getInstance().getTargetPort());
            }
        }
    }

    // check if the nodes is set to be delete
    public boolean wantToGoOut() {
        return Node.getInstance().isToDelete();
    }

    // insert node id and target on token
    public Token insertDeleteOnToken(Token token) {
        token = token.toBuilder()
                .addDelete(Token.Delete.newBuilder()
                        .setId(Node.getInstance().getId())
                        .setTargetId(Node.getInstance().getTargetId())
                        .setTargetIpAddress(Node.getInstance().getTargetIpAddress())
                        .setTargetPort(Node.getInstance().getTargetPort()))
                .build();
        return token;
    }

    // remove the node value from ready and waiting list
    public Token deleteFromReadyWaitingListOnToken(Token token) {
        Token temp = Token.newBuilder().build();
        // DELETE THE NODE FROM READY LIST
        for (Token.Ready item : token.getReadyList()) {
            if (item.getId() != Node.getInstance().getId()) {
                temp = temp.toBuilder().addReady(Token.Ready.newBuilder()
                        .setId(item.getId())
                        .setValue(item.getValue()))
                        .build();
            }
        }
        // DELETE THE NODE FROM WAITING LIST
        for (Token.Waiting item : token.getWaitingList()) {
            if (item.getId() != Node.getInstance().getId()) {
                temp = temp.toBuilder().addWaiting(Token.Waiting.newBuilder()
                        .setId(item.getId()))
                        .build();
            }
        }
        // SET THE SAME DELETE LIST
        for (Token.Delete item : token.getDeleteList()) {
            temp = temp.toBuilder().addDelete(Token.Delete.newBuilder()
                    .setId(item.getId())
                    .setTargetId(item.getTargetId())
                    .setTargetIpAddress(item.getTargetIpAddress())
                    .setTargetPort(item.getTargetPort()))
                    .build();
        }
        return temp;
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
    public String postDeleteOnGateway() {
        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:1200/nodes/remove");

        ClientResponse response;

        String jsonStr;
        ObjectMapper mapper = new ObjectMapper();

        synchronized (Node.getInstance()) {
            // get node object as a json string
            jsonStr = "{\"id\": " + Node.getInstance().getId()
                    + ",\"ipAddress\": \"" + Node.getInstance().getIpAddress()
                    + "\",\"port\": " + Node.getInstance().getPort()
                    + "}";
        }

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

    }

    // return true if the node is inside ready list on the token
    public boolean isInsideReady(Token token) {
        for (Token.Ready item : token.getReadyList()) {
            if (item.getId() == Node.getInstance().getId()) {
                return true;
            }
        }
        return false;
    }

    // return true if the node is inside waiting list on the token
    public boolean isInsideWaiting(Token token) {
        for (Token.Waiting item : token.getWaitingList()) {
            if (item.getId() == Node.getInstance().getId()) {
                return true;
            }
        }
        return false;
    }

    // return true if the target is inside delete list on the token
    public boolean isTargetInsideDelete(Token token) {
        for (Token.Delete item : token.getDeleteList()) {
            if (item.getId() == Node.getInstance().getTargetId()) {
                return true;
            }
        }
        return false;
    }

    // delete the target from the token if it is inside
    public Token deleteTargetFromToken(Token token) {
        Token temp = Token.newBuilder().build();
        // SET THE SAME READY LIST
        for (Token.Ready item : token.getReadyList()) {
            temp = temp.toBuilder().addReady(Token.Ready.newBuilder()
                    .setId(item.getId())
                    .setValue(item.getValue()))
                    .build();
        }
        // SET THE SAME WAITING LIST
        for (Token.Waiting item : token.getWaitingList()) {
            temp = temp.toBuilder().addWaiting(Token.Waiting.newBuilder()
                    .setId(item.getId()))
                    .build();
        }
        // SET THE DELETE LIST WITHOUT THE ONE UPDATED
        for (Token.Delete item : token.getDeleteList()) {
            if (!isTargetInsideDelete(token)) {
                temp = temp.toBuilder().addDelete(Token.Delete.newBuilder()
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
    public Token cleanReadyListFromToken(Token token) {
        Token temp = Token.newBuilder().build();
        // SET THE SAME WAITING LIST
        for (Token.Waiting item : token.getWaitingList()) {
            temp = temp.toBuilder().addWaiting(Token.Waiting.newBuilder()
                    .setId(item.getId()))
                    .build();
        }
        // SET THE SAME DELETE LIST
        for (Token.Delete item : token.getDeleteList()) {
            temp = temp.toBuilder().addDelete(Token.Delete.newBuilder()
                    .setId(item.getId())
                    .setTargetId(item.getTargetId())
                    .setTargetIpAddress(item.getTargetIpAddress())
                    .setTargetPort(item.getTargetPort()))
                    .build();
        }
        return temp;
    }

    // remove all the waiting list
    public Token cleanWaitingListFromToken(Token token) {
        Token temp = Token.newBuilder().build();
        // SET THE SAME READY LIST
        for (Token.Ready item : token.getReadyList()) {
            temp = temp.toBuilder().addReady(Token.Ready.newBuilder()
                    .setId(item.getId())
                    .setValue(item.getValue()))
                    .build();
        }
        // SET THE SAME DELETE LIST
        for (Token.Delete item : token.getDeleteList()) {
            temp = temp.toBuilder().addDelete(Token.Delete.newBuilder()
                    .setId(item.getId())
                    .setTargetId(item.getTargetId())
                    .setTargetIpAddress(item.getTargetIpAddress())
                    .setTargetPort(item.getTargetPort()))
                    .build();
        }
        return temp;
    }

    // compute the avg of all the stats in ready list
    public double computeFinalAvg(Token token) {
        double result = 0.0;
        int count = 0;

        // CALCOLA LA MEDIA FINALE ITERANDO SU READY LIST
        for (Token.Ready item : token.getReadyList()) {
            result += item.getValue();
            count++;
        }

        return result / count;
    }

    // set the new target based on delete list so, if your target is inside
    // the list update it until there is no target inside this list
    public Token updateTargetFromDeleteList(Token token) {
        int tempTargetId, tempTargetPort;
        String tempTargetIpAddress;
        while (isTargetInsideDelete(token)) {
            for (Token.Delete item : token.getDeleteList()) {
                if (item.getId() == Node.getInstance().getTargetId()) {
                    tempTargetId = item.getTargetId();
                    tempTargetIpAddress = item.getTargetIpAddress();
                    tempTargetPort = item.getTargetPort();
                    System.out.println(tempTargetId);
                    token = deleteTargetFromToken(token);

                    synchronized (Node.getInstance()) {
                        Node.getInstance().setTargetId(tempTargetId);
                        Node.getInstance().setTargetIpAddress(tempTargetIpAddress);
                        Node.getInstance().setTargetPort(tempTargetPort);
                    }
                }
            }
        }
        return token;
    }

    // return true if ready list equals waiting list and node is in ready list
    public boolean isTokenFullAndReadyToSend(Token token) {
        return (token.getReadyCount() == token.getWaitingCount()) && isInsideReady(token);
    }

    // return true if stat is ready
    public boolean isStatReady() {
        return LocalAverageList.getInstance().getSize() >= 1;
    }

    // add local average to the token
    public void computeToken(Token token) {

        /* ------------- CHECK IF NODE IS INSIDE READY AND WAITING ------------- */
        boolean insideReady = isInsideReady(token);
        boolean insideWaiting = isInsideWaiting(token);


        /* ------------ CHECK IF TARGET IS TO BE DELETED ----------- */
        if (isTargetInsideDelete(token)) {
            token = updateTargetFromDeleteList(token);
            updateTargetChannel();
        }

        // create stub
        TokenServiceBlockingStub stub = TokenServiceGrpc.newBlockingStub(getChannel());

        // check if token is full of stats and ready to send
        if (isTokenFullAndReadyToSend(token)) {
            double finalAvg = computeFinalAvg(token);

            // post the stats to the gateway
            sendStatsToGateway(finalAvg);
            System.out.println("SENT STATS TO THE GATEWAY " + finalAvg);

            // clean the ready and waiting list from the token
            token = cleanReadyListFromToken(token);
            token = cleanWaitingListFromToken(token);

            stub.tokenDelivery(token);

        } else if (isStatReady()) {

            // stats is READY
            if (insideReady) {
                System.out.println("1");
                stub.tokenDelivery(token);
            } else if (insideWaiting) {
                System.out.println("2");
                token = token.toBuilder()
                        .addReady(Token.Ready.newBuilder()
                                .setId(Node.getInstance().getId())
                                .setValue(LocalAverageList.getInstance().getLastValue()))
                        .build();
                stub.tokenDelivery(token);
            } else if (!insideReady && !insideWaiting) {
                token = token.toBuilder()
                        .addReady(Token.Ready.newBuilder()
                                .setId(Node.getInstance().getId())
                                .setValue(LocalAverageList.getInstance().getLastValue()))
                        .addWaiting(Token.Waiting.newBuilder()
                                .setId(Node.getInstance().getId()))
                        .build();
                stub.tokenDelivery(token);
            }

        } else {

            // stats IS NOT READY
            if (insideReady || insideWaiting) {
                System.out.println("3");
                stub.tokenDelivery(token);
            } else if (!insideWaiting) {
                System.out.println("4");
                token = token.toBuilder()
                        .addWaiting(Token.Waiting.newBuilder()
                                .setId(Node.getInstance().getId()).build())
                        .build();
                System.out.println("Target Id: " + Node.getInstance().getTargetId());
                System.out.println("Target Ip Address: " + Node.getInstance().getTargetIpAddress());
                System.out.println("Target Port: " + Node.getInstance().getTargetPort());
                stub.tokenDelivery(token);
            }

        }
    }

}
