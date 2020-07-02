package nodes;

import java.util.ArrayList;
import java.util.List;

public class NodeList {

    private List<Node> nodeList;

    private static NodeList instance;

    private NodeList() {
        this.nodeList = new ArrayList<Node>();
    }

    public static NodeList getInstance() {
        if(instance==null)
            instance = new NodeList();
        return instance;
    }


    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public int getSize(){
        return nodeList.size();
    }

}
