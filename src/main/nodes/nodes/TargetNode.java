package nodes;

public class TargetNode {

    private int targetId;
    private String targetIpAddress;
    private int targetPort;

    private static TargetNode instance;

    private TargetNode(){}

    /*
    public TargetNode(int targetId, String targetIpAddress, int targetPort){
        this.targetId = targetId;
        this.targetIpAddress = targetIpAddress;
        this.targetPort = targetPort;
    }
    */

    // singleton
    public synchronized static TargetNode getInstance(){
        if(instance==null)
            instance = new TargetNode();
        return instance;
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
}
