package nodes;


public class NodeStatus {

    private boolean delete;

    private static NodeStatus instance;

    private NodeStatus() {
        this.delete = false;
    }

    // singleton
    public synchronized static NodeStatus getInstance(){
        if(instance==null)
            instance = new NodeStatus();
        return instance;
    }


    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
