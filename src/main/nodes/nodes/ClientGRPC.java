package nodes;

import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import com.example.token.NodeServiceGrpc.NodeServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class ClientGRPC extends Thread {

    private int id;
    private String ipAddress;
    private int port;
    private TargetNode targetNode;


    public ClientGRPC(int id, String ipAddress, int port, TargetNode targetNode) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.targetNode = targetNode;
    }


    @Override
    public void run() {
        System.out.println("Sending join request message to " + targetNode.getTargetPort());

        syncJoinNet();

        System.out.println("Done");
    }


    public void syncJoinNet() {

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(targetNode.getTargetIpAddress() + ":" + targetNode.getTargetPort())
                .usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        // creating the JoinResponse object which will be provided as input to the RPC method
        JoinRequest joinRequest = JoinRequest.newBuilder()
                .setId(id)
                .setIpAddress(ipAddress)
                .setPort(port)
                .build();

        // calling the method, it returns an instance of JoinResponse
        JoinResponse joinResponse = stub.joinNetwork(joinRequest);

        // save the new target node after the join
        targetNode.setTargetId(joinResponse.getId());
        targetNode.setTargetIpAddress(joinResponse.getIpAddress());
        targetNode.setTargetPort(joinResponse.getPort());

        // printing the answer
        System.out.println("Join Response:\n" +
                joinResponse.getId() + " " +
                joinResponse.getIpAddress() + " " +
                joinResponse.getPort() + " " +
                joinResponse.getMessage());


        // TODO: closing the channel when the node want to esc
        //channel.shutdown();

    }


}
