package nodes;

import javax.xml.bind.annotation.*;


@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.FIELD)
public class Node implements Comparable<Node> {

    private int id;
    private String ipAddress;
    private int port;

    public Node(){}

    public Node(int id, String ipAddress, int port){
        this.id=id;
        this.ipAddress=ipAddress;
        this.port=port;
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
