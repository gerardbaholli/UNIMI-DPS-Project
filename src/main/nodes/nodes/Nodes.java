package nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "nodes")
public class Nodes {

    List<Node> nodeList;

    public Nodes(List<Node> nodeList){
        this.nodeList = nodeList;
    }

    @XmlElement(name = "node", type = Node.class)
    public List<Node> getNodes() {
        return this.nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }


}
