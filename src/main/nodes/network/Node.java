package network;

import java.util.List;

public class Node {

    private int id;
    private String ipAddress;
    private int port;

    private int targetId;
    private String targetIpAddress;
    private int targetPort;

    private List<network.beans.Node> nodeList;

    private boolean toDelete;

    private static Node instance;

    private Node(){
        this.toDelete = false;
    }

    public synchronized static Node getInstance(){
        if(instance==null)
            instance = new Node();
        return instance;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public synchronized String getIpAddress() {
        return ipAddress;
    }

    public synchronized void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public synchronized int getPort() {
        return port;
    }

    public synchronized void setPort(int port) {
        this.port = port;
    }

    public synchronized int getTargetId() {
        return targetId;
    }

    public synchronized void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public synchronized String getTargetIpAddress() {
        return targetIpAddress;
    }

    public synchronized void setTargetIpAddress(String targetIpAddress) {
        this.targetIpAddress = targetIpAddress;
    }

    public synchronized int getTargetPort() {
        return targetPort;
    }

    public synchronized void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    public synchronized List<network.beans.Node> getNodeList() {
        return nodeList;
    }

    public synchronized void setNodeList(List<network.beans.Node> nodeList) {
        this.nodeList = nodeList;
    }

    public synchronized int getNodeListSize(){
        return nodeList.size();
    }

    public synchronized boolean isToDelete() {
        return toDelete;
    }

    public synchronized void setToDelete() {
        this.toDelete = true;
    }

}