package nodes;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerGRPC extends Thread {

    private int port;

    public ServerGRPC(int port){
        this.port = port;
    }

    @Override
    public void run(){

        Server server = ServerBuilder.forPort(port)
                .addService(new NodeServiceImpl()).build();

        try {

            server.start();

            System.out.println("Node gRPC server started at port " + port);

            server.awaitTermination();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }


}
