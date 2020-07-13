package network;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerGRPC extends Thread {

    public ServerGRPC(){}

    @Override
    public void run(){
        Server server = ServerBuilder.forPort(Node.getInstance().getPort())
                .addService(new TokenServiceImpl()).build();
        try {
            server.start();
            System.out.println("Server GRPC started at port " + Node.getInstance().getPort());
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
