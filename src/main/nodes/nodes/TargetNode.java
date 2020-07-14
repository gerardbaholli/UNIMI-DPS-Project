package nodes;

public class TargetNode {

    private int targetId;
    private String targetIpAddress;
    private int targetPort;

    private static TargetNode instance;

    private TargetNode(){}


    // singleton
    public synchronized static TargetNode getInstance(){
        if(instance==null)
            instance = new TargetNode();
        return instance;
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
}
