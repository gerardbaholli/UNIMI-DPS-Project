package nodes;


import com.example.grpc.GreetingServiceOuterClass;
import com.example.token.NodeServiceGrpc;
import com.example.token.NodeServiceOuterClass;
import com.example.token.NodeServiceOuterClass.TokenMessage;
import com.example.token.NodeServiceOuterClass.TokenResponse;
import com.example.token.NodeServiceOuterClass.JoinRequest;
import com.example.token.NodeServiceOuterClass.JoinResponse;
import com.example.token.NodeServiceGrpc.NodeServiceBlockingStub;
import com.example.token.NodeServiceGrpc.NodeServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class ClientGRPC extends Thread {


    private int idNode;
    private String targetIp;
    private int targetPort;


    public ClientGRPC(int idNode, String targetIp, int targetPort) {
        this.idNode = idNode;
        this.targetIp = targetIp;
        this.targetPort = targetPort;
    }


    @Override
    public void run() {
        System.out.println("Sending join request message to " + targetPort);

        synchronousJoin();

        System.out.println("Done");
    }


    public void synchronousJoin() {

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(targetIp + ":" + targetPort).usePlaintext(true).build();

        // creating a blocking stub on the channel
        NodeServiceBlockingStub stub = NodeServiceGrpc.newBlockingStub(channel);

        // creating the JoinResponse object which will be provided as input to the RPC method
        JoinRequest joinRequest = JoinRequest.newBuilder()
                .setIp(targetIp)
                .setPort(targetPort)
                .build();

        // calling the method, it returns an instance of JoinResponse
        JoinResponse joinResponse = stub.joinNetwork(joinRequest);

        // printing the answer
        System.out.println(joinResponse.getIp() + " " +
                        joinResponse.getPort() + " " +
                        joinResponse.getMessage());

        // closing the channel
        //channel.shutdown();

    }


    public void asynchronousCall() throws InterruptedException {

        // plaintext channel on the address (ip/port)
        // which offers the TokenService service
        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(targetIp + ":" + targetPort).usePlaintext(true).build();

        // creating an asynchronous stub on the channel
        NodeServiceStub stub = NodeServiceGrpc.newStub(channel);

        // creating the TokenMessage object which will be provided
        // as input to the RPC method
        TokenMessage message = TokenMessage.newBuilder()
                .setFrom("node " + idNode)
                .setTo(Integer.toString(targetPort))
                .setMessage("token")
                .build();


        stub.token(message, new StreamObserver<TokenResponse>() {

            @Override
            public void onNext(TokenResponse tokenResponse) {
                System.out.println(tokenResponse.getAck());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                channel.shutdown();
            }

        });

        // need this, otherwise the method will terminate
        // before that answer from the server are received
        channel.awaitTermination(10, TimeUnit.SECONDS);

    }


}
