package nodes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Random;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.StreamSource;


@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.FIELD)
public class Node implements Comparable<Node> {

    private int id;
    private String ipAddress;
    private int port;
    List<Node> nodeList;

    String outputNodeList;

    public Node(){}

    public Node(int id, String ipAddress, int port){
        this.id=id;
        this.ipAddress=ipAddress;
        this.port=port;
    }


    public void start() {

        Client client = Client.create();

        WebResource webResource = client
                .resource("http://localhost:1200/nodes/add");

        Random rand = new Random();

        String jsonStr;

        Node node = new Node(id, this.ipAddress, this.port);

        ObjectMapper Obj = new ObjectMapper();


        try {
            node.setId(rand.nextInt((1000 - 1) + 1) + 1);

            // get Node object as a json string
            jsonStr = Obj.writeValueAsString(node);

            // send post
            postNode(webResource, jsonStr);

            // get nodeList
            getNodeList(this.outputNodeList);
        }
        catch (IOException | JAXBException e) {
            e.printStackTrace();
        }


    }

    public void postNode(WebResource webResource, String input){
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
            this.outputNodeList = response.getEntity(String.class);

            System.out.println(this.outputNodeList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNodeList(String s) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Node.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        StringBuffer xmlStr = new StringBuffer(this.outputNodeList);
        Nodes n = (Nodes) unmarshaller.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );

        System.out.println(n);


    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int compareTo(Node n) {
        return 0;
    }
}
