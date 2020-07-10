package nodes;

import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceGrpc.NodeServiceImplBase;
import com.example.token.NodeServiceOuterClass;
import com.example.token.NodeServiceOuterClass.TokenData;
import com.example.token.NodeServiceOuterClass.TokenDelete;
import com.example.token.NodeServiceOuterClass.TokenData.Ready;
import com.example.token.NodeServiceOuterClass.TokenData.Waiting;
import com.example.token.NodeServiceOuterClass.Empty;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
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

        System.out.println("Test 1");

        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("Test 2");

        System.out.println("Arrivato token a nodo " + node.getId());


        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(
                        TargetNode.getInstance().getTargetIpAddress() + ":" +
                                TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        TokenData newTokenData = tokenData.toBuilder().build();


        // CONTROLLA SE E' PRESENTE L'ID IN READY LIST
        boolean insideReady = isInsideReady(tokenData);

        // CONTROLLA SE E' PRESENTE L'ID IN WAITING LIST
        boolean insideWaiting = isInsideWaiting(tokenData);

        
        /*
        // SLEEP DA RIMUOVERE, MESSA PER RALLENTARE UN PO'
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */


        // CONTROLLA SE IL TOKEN E' PIENO IN CASO POSITIVO MANDA AL GATEWAY
        // SE NON E' PIENO INSERISCE IL VALORE NELLA LISTA CORRISPONDENTE AL SUO STATO
        if ((tokenData.getReadyCount() == tokenData.getWaitingCount()) && insideReady) {
            double finalAvg = computeFinalAvg(tokenData);
            // INVIA STATISTICA AL GATEWAY
            System.out.println(sendToGateway(finalAvg));
            System.out.println("SENT TOKEN TO THE GATEWAY " + finalAvg);
            newTokenData = TokenData.newBuilder().build();
            stub.tokenDeliveryData(newTokenData);
            channel.shutdown();
        } else if (LocalAvgList.getInstance().getSize() >= 1) {

            // ESEGUITO QUANDO LA STATISTICA E' PRONTA
            if (insideReady) {
                System.out.println("1");
                stub.tokenDeliveryData(tokenData);
                channel.shutdown();
            } else if (insideWaiting) {
                System.out.println("2");
                newTokenData = tokenData.toBuilder()
                        .addReady(TokenData.Ready.newBuilder()
                                .setId(node.getId())
                                .setValue(LocalAvgList.getInstance().getLastValue()))
                        .build();
                stub.tokenDeliveryData(newTokenData);
                channel.shutdown();
            } else if (!insideReady && !insideWaiting) {
                newTokenData = tokenData.toBuilder()
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

            // ESEGUITO QUANDO LA STATISTICA NON E' PRONTA
            if (insideReady || insideWaiting) {
                System.out.println("3");

                stub.tokenDeliveryData(tokenData);
                channel.shutdown();
                System.out.println("QUI NON ARRIVO");
            } else if (!insideWaiting) {
                System.out.println("4");
                newTokenData = tokenData.toBuilder()
                        .addWaiting(TokenData.Waiting.newBuilder()
                                .setId(node.getId()).build())
                        .build();
                stub.tokenDeliveryData(newTokenData);
                channel.shutdown();
            }

        }




        /*
        // SLEEP DA RIMUOVERE, MESSA PER RALLENTARE UN PO'
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */


        System.out.println("Ready list:\n" + newTokenData.getReadyList());
        System.out.println("Waiting list:\n" + newTokenData.getWaitingList());
        System.out.println("Invio token a " + TargetNode.getInstance().getTargetId());


    }


    public boolean isInsideReady(TokenData tokenData) {
        for (Ready item : tokenData.getReadyList()) {
            if (item.getId() == node.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInsideWaiting(TokenData tokenData) {
        for (Waiting item : tokenData.getWaitingList()) {
            if (item.getId() == node.getId()) {
                return true;
            }
        }
        return false;
    }

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

    public String sendToGateway(double avg) {
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


    @Override
    public synchronized void tokenDeliveryDelete(NodeServiceOuterClass.TokenDelete tokenDelete,
                                                 StreamObserver<Empty> responseObserver) {

        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("Arrivato TOKEN DELETE a nodo " + node.getId());


        // CONTROLLA SE IL TOKEN E' ARRIVATO AL CREATORE
        boolean isCreator = false;
        if (node.getId() == tokenDelete.getId()) {
            isCreator = true;
            System.out.println("Appena arrivato al creatore!");
        }


        if (!isCreator) {

            ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(
                            TargetNode.getInstance().getTargetIpAddress() + ":" +
                                    TargetNode.getInstance().getTargetPort())
                    .usePlaintext(true).build();

            // creating a blocking stub on the channel
            NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

            TokenDelete newTokenDelete = tokenDelete.toBuilder().build();

            // CONTROLLA SE IL TARGET E' IL CREATORE DEL TOKEN
            if (TargetNode.getInstance().getTargetId() == tokenDelete.getId()) {

                System.out.println("Il mio target è il creatore!");
                TargetNode.getInstance().setTargetId(tokenDelete.getTargetId());
                TargetNode.getInstance().setTargetIpAddress(tokenDelete.getTargetIpAddress());
                TargetNode.getInstance().setTargetPort(tokenDelete.getTargetPort());
                System.out.println("Il mio nuovo target è: " + TargetNode.getInstance().getTargetId());

            }


            // send the token to the next node
            stub.tokenDeliveryDelete(newTokenDelete);

            channel.shutdown();


        } else if (isCreator) {
            System.out.println("Ok sono uscito!");
        }


    }


}
