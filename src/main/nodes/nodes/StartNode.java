package nodes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


import org.eclipse.persistence.jaxb.MarshallerProperties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class StartNode {

    public static void main(String[] args) {

        String outputNodeList;

        Node node = new Node(12, "localhost", 1254);

        start(node);


    }


    public static void start(Node node) {

        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:1200/nodes/add");




        Random rand = new Random();
        String jsonStr;
        ObjectMapper mapper = new ObjectMapper();


        try {

            System.out.println("Output from Server:");
            String response;
            do {
                // generate random id
                node.setId(rand.nextInt((10 - 1) + 1) + 1);

                // get node object as a json string
                jsonStr = mapper.writeValueAsString(node);

                // send post and receive response as string
                response = postNode(webResource, jsonStr);
            } while (response.equals("error409"));
            System.out.println(response);

            // cut the first 8 words and the last 1 from the string
            response = response.substring(8, response.length()-1);

            // put the json into the list of nodes
            List<Node> nodeJsonToList = mapper
                    .readValue(response, new TypeReference<List<Node>>(){});

            // sort and print the list of nodes
            System.out.println("List of nodes:");
            nodeJsonToList.sort(Comparator.comparing(Node::getId));
            for(int i=0;i<nodeJsonToList.size();i++){
                System.out.println("id: " + nodeJsonToList.get(i).getId() +
                        ", address: " + nodeJsonToList.get(i).getIpAddress()+
                        ", port: " + nodeJsonToList.get(i).getPort());
            }

        }
        catch ( JsonProcessingException e) {
            e.printStackTrace();
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
