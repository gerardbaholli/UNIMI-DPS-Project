package network;

import com.example.token2.TokenServiceGrpc;
import com.example.token2.TokenServiceGrpc.TokenServiceBlockingStub;
import com.example.token2.TokenServiceOuterClass.JoinRequest;
import com.example.token2.TokenServiceOuterClass.JoinResponse;
import com.example.token2.TokenServiceOuterClass.Token;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;

import simulator.PM10Simulator;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class StartPeer {

    public static void main(String[] args) throws IOException {

        setNodeInfo();

        ServerGRPC serverGRPC = new ServerGRPC();
        serverGRPC.start();

        contactGateway();

        if (listSizeBiggerThanOne()) {
            joinNetwork();
            updateTargetChannel();
        } else {
            setHimselfAsTarget();
            updateTargetChannel();
            sendToken();
        }

        printInfoNode();

        // THREAD SIMULATOR
        Queue buffer = new Queue();
        PM10Simulator pm10Simulator = new PM10Simulator(buffer);
        pm10Simulator.start();

        // THREAD LOCAL AVERAGE
        LocalAverage localAverage = new LocalAverage(buffer);
        localAverage.start();


        System.out.println("Hit return to stop...");
        System.in.read();

        setNodeToBeDelete();
        pm10Simulator.stopMeGently();
        System.out.println("Node stopped");

    }

    public static void contactGateway() {
        Node.getInstance().setNodeList(getNodeListFromGateway());
    }

    public static List<network.beans.Node> getNodeListFromGateway() {

        Random rand = new Random();
        String jsonStr;
        ObjectMapper mapper = new ObjectMapper();
        network.beans.Node node = new network.beans.Node();

        synchronized (Node.getInstance()) {
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

                // cut the first 8 words and the last 1 from the string
                assert response != null;
                response = response.substring(8, response.length() - 1);

                // returns the list of nodes mapped from the json
                return mapper.readValue(response, new TypeReference<List<network.beans.Node>>() {
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
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
        synchronized (Node.getInstance()) {
            Node.getInstance().setIpAddress("localhost");
            Node.getInstance().setPort(rand.nextInt((9000 - 8000) + 1) + 8000);
        }
    }

    public static void setHimselfAsTarget() {
        synchronized (Node.getInstance()) {
            Node.getInstance().setTargetId(Node.getInstance().getId());
            Node.getInstance().setTargetIpAddress(Node.getInstance().getIpAddress());
            Node.getInstance().setTargetPort(Node.getInstance().getPort());
        }
    }

    public static boolean listSizeBiggerThanOne() {
        return (Node.getInstance().getNodeListSize() > 1);
    }

    public static void joinNetwork() {
        Random rand = new Random();

        synchronized (Node.getInstance()) {

            // randomly puts one node as target
            if (Node.getInstance().getNodeListSize() > 1) {
                network.beans.Node randomNode;

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

            Channel.getInstance().setChannel(
                    Node.getInstance().getTargetIpAddress(),
                    Node.getInstance().getTargetPort());

            TokenServiceBlockingStub stub = TokenServiceGrpc.newBlockingStub(getChannel());

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

            // update the new target from the joinResponse
            Node.getInstance().setTargetId(joinResponse.getId());
            Node.getInstance().setTargetIpAddress(joinResponse.getIpAddress());
            Node.getInstance().setTargetPort(joinResponse.getPort());
            shutdownChannel();

        }

    }

    public static void sendToken() {
        TokenServiceBlockingStub stub = TokenServiceGrpc.newBlockingStub(Channel.getInstance().getChannel());
        Token token = Token.newBuilder().build();
        stub.tokenDelivery(token);
    }

    public static void updateTargetChannel() {
        synchronized (Node.getInstance()) {
            synchronized (Channel.getInstance()) {
                Channel.getInstance().setChannel(
                        Node.getInstance().getTargetIpAddress(),
                        Node.getInstance().getTargetPort());
            }
        }
    }

    public static void printInfoNode() {
        synchronized (Node.getInstance()) {
            System.out.println("I'm node " + Node.getInstance().getId());
            System.out.println("Target node " + Node.getInstance().getTargetId());
        }
    }

    public static ManagedChannel getChannel() {
        return Channel.getInstance().getChannel();
    }

    public static void shutdownChannel() {
        Channel.getInstance().shutdownChannel();
    }

    public static void setNodeToBeDelete(){
        Node.getInstance().setToDelete();
    }
}
