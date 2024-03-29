package nodes;


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
        int PORT = rand.nextInt((9000 - 6000) + 1) + 6000;

        Node node = new Node(IP, PORT);




        /* ------------ NODE JOINING ------------ */

        // post call, sends to the gateway the intention to join the network
        NodeList.getInstance().setNodeList(start(node));


        // sets the id node given from the gateway
        idNode = node.getId();
        System.out.println("I'm node: " + idNode);

        // start the gRPC server
        ServerGRPC serverGRPC = new ServerGRPC(node);
        serverGRPC.start();





        /* ------------ NODE RUNNING ------------ */

        System.out.println("Node running");

        Queue buffer = new Queue();

        // create thread simulator
        PM10Simulator pm10Simulator = new PM10Simulator(buffer);
        pm10Simulator.start();

        // create thread that compute avg of the data from the buffer
        ThreadAverage threadAverage = new ThreadAverage(buffer);
        threadAverage.start();



        // accept new join request from the incoming nodes
        JoinListener joinListener = new JoinListener(node);
        joinListener.start();



        // ------------ STOPPING NODE ------------ */

        System.out.println("Hit return to stop...");
        System.in.read();


        // set node status to wantToEsc = true
        synchronized (NodeStatus.getInstance()) {
            NodeStatus.getInstance().setDelete(true);
            NodeStatus.getInstance().notifyAll();
        }


        // TODO: shutdown the channel of the gRPC Server
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
                node.setId(rand.nextInt((15 - 1) + 1) + 1);

                // get node object as a json string
                jsonStr = mapper.writeValueAsString(node);

                // send post and receive response as string
                response = addNodeToGateway(jsonStr);
            } while (Objects.equals(response, "error409"));
            // System.out.println("Output from Server:\n" + response);

            // cut the first 8 words and the last 1 from the string
            assert response != null;
            response = response.substring(8, response.length()-1);

            // returns the list of nodes mapped from the json
            return mapper.readValue(response, new TypeReference<List<Node>>(){});

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String addNodeToGateway(String input){

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


}
