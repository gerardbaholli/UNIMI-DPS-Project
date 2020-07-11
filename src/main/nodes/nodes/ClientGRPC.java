package nodes;

import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceOuterClass.TokenData;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import com.example.token.NodeServiceGrpc.NodeServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class ClientGRPC extends Thread {

    private Node node;

    public ClientGRPC(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        System.out.println("Sending join request message to " +
                TargetNode.getInstance().getTargetId());

        joinNet();

        System.out.println("Node " + node.getId() + " linked to node " +
                TargetNode.getInstance().getTargetId());


        System.out.println("Dimensione lista nodi " + NodeList.getInstance().getSize());
        // add token if there are only two nodes
        if (NodeList.getInstance().getSize()==2){
            addToken();
        }

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
    public void addToken(){

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


}
