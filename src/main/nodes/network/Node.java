package network;

import java.util.List;

public class Node {

    private int id;
    private String ipAddress;
    private int port;

    private int targetId;
    private String targetIpAddress;
    private int targetPort;

    private List<nodes.Node> nodeList;

    private static Node instance;

    private Node(){}

    public synchronized static Node getInstance(){
        if(instance==null)
            instance = new Node();
        return instance;
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

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTargetIpAddress() {
        return targetIpAddress;
    }

    public void setTargetIpAddress(String targetIpAddress) {
        this.targetIpAddress = targetIpAddress;
    }

    public int getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    public List<nodes.Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<nodes.Node> nodeList) {
        this.nodeList = nodeList;
    }

    public int getNodeListSize(){
        return nodeList.size();
    }

}
