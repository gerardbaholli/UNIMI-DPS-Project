package nodes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public class N1 {

    public static void main(String[] args) {

        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:1200/nodes/add");

        Random rand = new Random();
        int id = 1;
        String ipAddress = "localhost";
        int port = 1500;

        String jsonStr;

        Node n1 = new Node(id, ipAddress, port);

        ObjectMapper Obj = new ObjectMapper();

        List<Node> nodeList;

        try {
            n1.setId(rand.nextInt((1000 - 1) + 1) + 1);

            // get Node object as a json string
            jsonStr = Obj.writeValueAsString(n1);

            // send post
            postNode(webResource, jsonStr);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void postNode(WebResource webResource, String input){
        try {

            ClientResponse response;

            do {
                response = webResource.type("application/json")
                        .post(ClientResponse.class, input);
            } while (response.getStatus() == 409);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed - HTTP error code : "
                        + response.getStatus());
            }

            System.out.println("Output from Server .... \n");
            String output = response.getEntity(String.class);
            System.out.println(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
