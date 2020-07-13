package network;

import com.example.token2.TokenServiceGrpc;
import com.example.token2.TokenServiceOuterClass.JoinRequest;
import com.example.token2.TokenServiceOuterClass.JoinResponse;
import com.example.token2.TokenServiceOuterClass.Empty;
import com.example.token2.TokenServiceOuterClass.Token;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class StartNode {

    static ManagedChannel channel = ManagedChannelBuilder
            .forTarget("localhost:8000")
            .usePlaintext(true).build();

    static Object Test = new Object();

    public static void main(String[] args) throws IOException {


        setNodeInfo();

        ServerGRPC serverGRPC = new ServerGRPC();
        serverGRPC.start();

        Node.getInstance().setNodeList(start());

        infoNode();

        if (listSizeBiggerThanOne()) {
            joinNetwork();
        } else {
            setHimselfAsTarget();
        }

        infoNode();

        updateTargetChannel();

        sendToken();

        System.out.println("Hit return to stop...");
        System.in.read();

    }


    public static List<nodes.Node> start() {

        Random rand = new Random();
        String jsonStr;
        ObjectMapper mapper = new ObjectMapper();
        nodes.Node node = new nodes.Node();

        node.setIpAddress(Node.getInstance().getIpAddress());
        node.setPort(Node.getInstance().getPort());

        try {

            String response;
            do {
                // generate random id
                Node.getInstance().setId(rand.nextInt((5 - 1) + 1) + 1);

                node.setId(Node.getInstance().getId());

                // get node object as a json string
                jsonStr = "{\"id\": " + Node.getInstance().getId()
                        + ",\"ipAddress\": \"" + Node.getInstance().getIpAddress()
                        + "\",\"port\": " + Node.getInstance().getPort()
                        + "}";

                // send post and receive response as string
                response = addNode(jsonStr);
            } while (Objects.equals(response, "error409"));
            // System.out.println("Output from Server:\n" + response);

            // cut the first 8 words and the last 1 from the string
            assert response != null;
            response = response.substring(8, response.length() - 1);

            // returns the list of nodes mapped from the json
            return mapper.readValue(response, new TypeReference<List<nodes.Node>>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String addNode(String input) {

        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:1200/nodes/add");

        ClientResponse response;

        response = webResource.accept("application/json").type("application/json")
                .post(ClientResponse.class, input);

        if (response.getStatus() == 409) {
            return "error409";
        }

        try {
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed - HTTP error code : "
                        + response.getStatus());
            }
            return response.getEntity(String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setNodeInfo() {
        Random rand = new Random();
        Node.getInstance().setIpAddress("localhost");
        Node.getInstance().setPort(rand.nextInt((8090 - 8000) + 1) + 8000);
    }

    public static void setHimselfAsTarget(){
        Node.getInstance().setTargetId(Node.getInstance().getId());
        Node.getInstance().setTargetIpAddress(Node.getInstance().getIpAddress());
        Node.getInstance().setTargetPort(Node.getInstance().getPort());
    }

    public static boolean listSizeBiggerThanOne(){
        return (Node.getInstance().getNodeListSize()>1);
    }

    public static void joinNetwork() {
        Random rand = new Random();

        // if it is not the first node, he randomly puts one as target
        if (Node.getInstance().getNodeListSize() > 1) {
            nodes.Node randomNode;

            // extract a random node to ask for the join different from himself
            do {
                randomNode = Node.getInstance().getNodeList().get(rand.nextInt(Node.getInstance().getNodeListSize()));
            } while (randomNode.getId() == Node.getInstance().getId());

            Node.getInstance().setTargetId(randomNode.getId());
            Node.getInstance().setTargetIpAddress(randomNode.getIpAddress());
            Node.getInstance().setTargetPort(randomNode.getPort());

        } else {

            // if it is the first node, he put himself as target node
            Node.getInstance().setTargetId(Node.getInstance().getId());
            Node.getInstance().setTargetIpAddress(Node.getInstance().getIpAddress());
            Node.getInstance().setTargetPort(Node.getInstance().getPort());

        }

        System.out.println("Target node: " + Node.getInstance().getTargetId());

        /* ------- SEND MESSAGE TO THE TARGET --------- */

        ManagedChannel channelTemp = ManagedChannelBuilder
                .forTarget(Node.getInstance().getTargetIpAddress() + ":" + Node.getInstance().getTargetPort())
                .usePlaintext(true).build();

        TokenServiceGrpc.TokenServiceBlockingStub stub = TokenServiceGrpc.newBlockingStub(channelTemp);

        JoinRequest joinRequest = JoinRequest.newBuilder()
                .setId(Node.getInstance().getId())
                .setIpAddress(Node.getInstance().getIpAddress())
                .setPort(Node.getInstance().getPort())
                .build();

        JoinResponse joinResponse = stub.joinNetwork(joinRequest);

        System.out.println("Join Response:\n" +
                joinResponse.getId() + " " +
                joinResponse.getIpAddress() + " " +
                joinResponse.getPort() + " " +
                joinResponse.getMessage());

        channelTemp.shutdown();

    }

    public static void sendToken() {

        TokenServiceGrpc.TokenServiceStub stub = TokenServiceGrpc.newStub(channel);

        Token token = Token.newBuilder().setValue(1).build();

        stub.tokenDelivery(token, new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty empty) {
                System.out.println("Ciaone");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Finito");
                //channel.shutdownNow();
            }
        });

        /*
        try {
            synchronized (Test){
                Test.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

    }

    public static void updateTargetChannel(){
        channel.shutdown();

        channel = ManagedChannelBuilder
                .forTarget(Node.getInstance().getTargetIpAddress() + ":" + Node.getInstance().getTargetPort())
                .usePlaintext(true).build();
    }

    public static void infoNode(){
        System.out.println("I'm node "+ Node.getInstance().getId());
        System.out.println("Target node " + Node.getInstance().getTargetId());
    }


}
