package nodes;

import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceOuterClass.TokenData;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import com.example.token.NodeServiceGrpc.NodeServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;
import java.util.Random;


public class JoinListener extends Thread {

    private Node node;

    public JoinListener(Node node) {
        this.node = node;
    }

    @Override
    public void run() {

        selectTarget(NodeList.getInstance().getNodeList(), node);

        // ask node to join the network if there is an error
        // select another target and retries the call
        boolean status = false;
        do {
            try {
                joinNet();
                status = true;
            } catch (Throwable t) {
                selectTarget(NodeList.getInstance().getNodeList(), node);
            }
        } while (!status);

        System.out.println("Node " + node.getId() + " linked to node " +
                TargetNode.getInstance().getTargetId());




        // add token if there are only two nodes
        if (NodeList.getInstance().getSize() == 1) {
            addToken();
        }
        /*
        else if (NodeList.getInstance().getSize() == 1) {
            try {
                while (TargetNode.getInstance().getTargetId() == node.getId()) {
                    synchronized (LocalAvgList.getInstance()) {
                        while (LocalAvgList.getInstance().getSize() < 1) {
                            LocalAvgList.getInstance().wait();
                        }
                        sendStatsToGateway(LocalAvgList.getInstance().getLastValue());
                        System.out.println("SENT STATS TO THE GATEWAY");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        */


    }


    // make the node join the network
    public void joinNet() {

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(TargetNode.getInstance().getTargetIpAddress() + ":" + TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        // creating the JoinRequest object which will be provided as input to the RPC method
        JoinRequest joinRequest = JoinRequest.newBuilder()
                .setId(node.getId())
                .setIpAddress(node.getIpAddress())
                .setPort(node.getPort())
                .build();

        // calling the method, it returns an instance of JoinResponse
        JoinResponse joinResponse = stub.joinNetwork(joinRequest);

        // save the new target node after the join
        TargetNode.getInstance().setTargetId(joinResponse.getId());
        TargetNode.getInstance().setTargetIpAddress(joinResponse.getIpAddress());
        TargetNode.getInstance().setTargetPort(joinResponse.getPort());

        // printing the answer
        System.out.println("Join Response:\n" +
                joinResponse.getId() + " " +
                joinResponse.getIpAddress() + " " +
                joinResponse.getPort() + " " +
                joinResponse.getMessage());


        channel.shutdown();

    }

    // add the token data on the network
    public void addToken() {

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(
                        TargetNode.getInstance().getTargetIpAddress() + ":" +
                                TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        // create the token data
        TokenData tokenData = TokenData.newBuilder().build();

        // send the token to the target node
        stub.tokenDeliveryData(tokenData);

        channel.shutdown();
    }

    public static void selectTarget(List<Node> nodeList, Node node) {
        Random rand = new Random();

        // if it is not the first node, he randomly puts one as target
        if (nodeList.size() > 1) {
            Node randomNode;

            // extract a random node to ask for the join
            do {
                randomNode = nodeList.get(rand.nextInt(nodeList.size()));
            } while (randomNode.getId() == node.getId());

            TargetNode.getInstance().setTargetId(randomNode.getId());
            TargetNode.getInstance().setTargetIpAddress(randomNode.getIpAddress());
            TargetNode.getInstance().setTargetPort(randomNode.getPort());

        } else {
            // if it is the first node, he put himself as target node
            TargetNode.getInstance().setTargetId(node.getId());
            TargetNode.getInstance().setTargetIpAddress(node.getIpAddress());
            TargetNode.getInstance().setTargetPort(node.getPort());
        }

        System.out.println("Target node: " + TargetNode.getInstance().getTargetId());
    }



}
