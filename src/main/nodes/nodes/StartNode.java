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

        Node node1 = new Node(12, "localhost", 1254);

        start(node1);

    }


    public static void start(Node node) {

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
            System.out.println("Output from Server:\n" + response);

            // cut the first 8 words and the last 1 from the string
            assert response != null;
            response = response.substring(8, response.length()-1);

            // put the json into the list of nodes
            List<Node> nodeJsonToList = mapper
                    .readValue(response, new TypeReference<List<Node>>(){});

            // sort and print the list of nodes
            System.out.println("List of nodes:");
            nodeJsonToList.sort(Comparator.comparing(Node::getId));
            for (Node value : nodeJsonToList) {
                System.out.println("id: " + value.getId() +
                        ", address: " + value.getIpAddress() +
                        ", port: " + value.getPort());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

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

}
