package nodes;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerGRPC extends Thread {

    private int port;
    TargetNode targetNode;

    public ServerGRPC(int port, TargetNode targetNode){
        this.port = port;
        this.targetNode = targetNode;
    }

    @Override
    public void run(){

        Server server = ServerBuilder.forPort(port)
                .addService(new NodeServiceImpl(targetNode)).build();

        try {

            server.start();

            System.out.println("gRPC server started at port " + port);

            server.awaitTermination();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }


}
