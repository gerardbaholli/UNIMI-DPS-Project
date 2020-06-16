package nodes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.util.*;


public class StartNode {

    public static void main(String[] args) {

        Random rand = new Random();

        int idNode;
        String IP = "localhost";
        int PORT = rand.nextInt((8090 - 8000) + 1) + 8000;
        List<Node> nodeList;

        Node node = new Node(IP, PORT);
        TargetNode targetNode = new TargetNode();

        nodeList = start(node);

        idNode = node.getId();
        System.out.println("I'm node: " + idNode);

        ServerGRPC serverGRPC = new ServerGRPC(PORT, targetNode);
        serverGRPC.start();

        assert nodeList != null;
        if (nodeList.size() > 1) {
            Node randomNode;

            // extract a random node to ask for the join
            do {
                randomNode = nodeList.get(rand.nextInt(nodeList.size()));
            } while (randomNode.getId() == node.getId());

            // TODO mettere come target il target di quello a cui si è chiesta la join
            targetNode.setTargetId(randomNode.getId());
            targetNode.setTargetIpAddress(randomNode.getIpAddress());
            targetNode.setTargetPort(randomNode.getPort());
            System.out.println("Target node: " + targetNode.getTargetId());

            ClientGRPC clientGRPC = new ClientGRPC(idNode, IP, PORT, targetNode);
            clientGRPC.start();

        } else {
            // mette se stesso come target SICURAMENTE DA REVISIONARE
            targetNode.setTargetId(node.getId());
            targetNode.setTargetIpAddress(node.getIpAddress());
            targetNode.setTargetPort(node.getPort());
            System.out.println("Target node: " + targetNode.getTargetId());
        }


    }

    public static List<Node> start(Node node) {

        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:1200/nodes/add");

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
                response = postNode(webResource, jsonStr);
            } while (Objects.equals(response, "error409"));
            // System.out.println("Output from Server:\n" + response);

            // cut the first 8 words and the last 1 from the string
            assert response != null;
            response = response.substring(8, response.length()-1);

            // put the json into the list of nodes
            List<Node> nodeJsonToList = mapper
                    .readValue(response, new TypeReference<List<Node>>(){});

            // sort and print the list of nodes
            /* System.out.println("List of nodes:");
            nodeJsonToList.sort(Comparator.comparing(Node::getId));
            for (Node value : nodeJsonToList) {
                System.out.println("id: " + value.getId() +
                        ", address: " + value.getIpAddress() +
                        ", port: " + value.getPort());
            } */

            return nodeJsonToList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String postNode(WebResource webResource, String input){
        try {
            ClientResponse response;

            response = webResource.accept("application/json").type("application/json")
                    .post(ClientResponse.class, input);

            if (response.getStatus() == 409) {
                return "error409";
            }

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

    // public static join();

}
