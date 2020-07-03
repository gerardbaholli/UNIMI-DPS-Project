package nodes;

import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class StopNodeGRPC extends Thread {

    private Node node;

    public StopNodeGRPC(Node node) {
        this.node = node;
    }

    @Override
    public void run() {

        addTokenDelete();
        System.out.println("Fatto addTokenDelete()");

    }

    // add the token delete to the network
    public void addTokenDelete(){

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(
                        TargetNode.getInstance().getTargetIpAddress() + ":" +
                                TargetNode.getInstance().getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceGrpc.NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        // create the token delete
        NodeServiceOuterClass.TokenDelete tokenDelete =
                NodeServiceOuterClass.TokenDelete
                        .newBuilder()
                        .setId(node.getId())
                        .setTargetId(TargetNode.getInstance().getTargetId())
                        .setTargetIpAddress(TargetNode.getInstance().getTargetIpAddress())
                        .setTargetPort(TargetNode.getInstance().getTargetPort())
                        .build();

        // send the token to the next node
        stub.tokenDeliveryDelete(tokenDelete);

        channel.shutdown();
    }





}
