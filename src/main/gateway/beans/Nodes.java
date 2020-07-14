package beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Nodes {

    @XmlElement(name="node")
    private List<Node> nodesList;

    private static Nodes instance;

    private Nodes() {
        nodesList = new ArrayList<>();
    }

    //singleton
    public synchronized static Nodes getInstance(){
        if(instance==null)
            instance = new Nodes();
        return instance;
    }

    public synchronized List<Node> getNodesList() {
        return new ArrayList<>(nodesList);
    }


    // OK
    public synchronized String add(Node n){
        if (n!=null){
            for (Node node : nodesList){
                if (node.getId()==n.getId()){
                    return "Warning";
                }
            }
            nodesList.add(n);
            return "Success";
        }
        return "Fail";
    }

    // OK
    public synchronized String delete(Node n){
        if (n!=null){
            for (Node node : nodesList) {
                if (node.getId()==n.getId()){
                    this.nodesList.remove(node);
                    return "Success";
                }
            }
        }
        return "notFound";
    }

    // OK
    public int getNodesNumber(){
        List<Node> tempList = new ArrayList<>(getNodesList());
        int count = 0;
        for (Node n : tempList){
            count++;
        }
        return count;
    }



}
