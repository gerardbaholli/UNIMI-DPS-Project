package nodes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import simulator.PM10Simulator;

import java.io.IOException;
import java.util.*;


public class StartNode {

    public static void main(String[] args) throws IOException {

        Random rand = new Random();

        int idNode;
        String IP = "localhost";
        int PORT = rand.nextInt((8090 - 8000) + 1) + 8000;

        Node node = new Node(IP, PORT);




        /* ------------ NODE JOINING ------------ */

        // post call, sends to the gateway the intention to join the network
        NodeList.getInstance().setNodeList(start(node));

        // TODO: eliminare, stampa lista nodi ricevuta dal gateway
        System.out.println("STAMPA QUI");
        for (Node value : NodeList.getInstance().getNodeList())
            System.out.println(value.getId());


        // sets the id node given from the gateway
        idNode = node.getId();
        System.out.println("I'm node: " + idNode);

        // start the gRPC server
        ServerGRPC serverGRPC = new ServerGRPC(node);
        serverGRPC.start();

        // join the network
        join(NodeList.getInstance().getNodeList(), node);




        /* ------------ NODE RUNNING ------------ */

        System.out.println("Node running in the network");

        Queue buffer = new Queue();
        double avg = 0;

        // create thread simulator
        PM10Simulator pm10Simulator = new PM10Simulator(buffer);
        pm10Simulator.start();

        // create thread that compute avg of the data from the buffer
        ThreadAverage threadAverage = new ThreadAverage(buffer);
        threadAverage.start();







        // ------------ STOPPING NODE ------------ */

        System.out.println("Hit return to stop...");
        System.in.read();
        // TODO: STOP NODE --------v
        StopNodeGRPC stopNodeGRPC = new StopNodeGRPC(node);
        stopNodeGRPC.start();


        System.out.println(remove(node));

        // TODO: shutdown the channel of the gRPC Server
        // TODO: post delete call to the gateway
        pm10Simulator.stopMeGently();
        System.out.println("Node stopped");



    }

    public static List<Node> start(Node node) {

        Random rand = new Random();
        String jsonStr;
        ObjectMapper mapper = new ObjectMapper();

        try {

            String response;
            do {
                // generate random id
                node.setId(rand.nextInt((5 - 1) + 1) + 1);

                // get node object as a json string
                jsonStr = mapper.writeValueAsString(node);

                // send post and receive response as string
                response = addNode(jsonStr);
            } while (Objects.equals(response, "error409"));
            // System.out.println("Output from Server:\n" + response);

            // cut the first 8 words and the last 1 from the string
            assert response != null;
            response = response.substring(8, response.length()-1);

            // put the json into the list of nodes
            List<Node> nodeJsonToList = mapper
                    .readValue(response, new TypeReference<List<Node>>(){});

            return nodeJsonToList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String addNode(String input){

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

    public static void join(List<Node> nodeList, Node node){
        Random rand = new Random();

        // if it is not the first node, he randomly puts one as target
        if (nodeList.size() > 1) {
            Node randomNode;

            // extract a random node to ask for the join
            do {
                randomNode = nodeList.get(rand.nextInt(nodeList.size()));
            } while (randomNode.getId() == node.getId());

            TargetNode.getInstance().setTargetId(randomNode.getId());
            TargetNode.getInstance().setTargetIpAddress(randomNode.getIpAddress());
            TargetNode.getInstance().setTargetPort(randomNode.getPort());

        } else {
            // if it is the first node, he put himself as target node
            TargetNode.getInstance().setTargetId(node.getId());
            TargetNode.getInstance().setTargetIpAddress(node.getIpAddress());
            TargetNode.getInstance().setTargetPort(node.getPort());
        }

        System.out.println("Target node: " + TargetNode.getInstance().getTargetId());

        // create the client GRPC
        ClientGRPC clientGRPC = new ClientGRPC(node);
        clientGRPC.start();
    }

    public static String remove(Node node){

        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:1200/nodes/remove");

        ClientResponse response;

        String jsonStr;
        ObjectMapper mapper = new ObjectMapper();


        // get node object as a json string
        try {
            jsonStr = mapper.writeValueAsString(node);

            response = webResource.accept("application/json").type("application/json")
                        .delete(ClientResponse.class, jsonStr);

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
