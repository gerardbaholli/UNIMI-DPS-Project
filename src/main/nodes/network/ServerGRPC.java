package network;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerGRPC extends Thread {

    Server server;

    public ServerGRPC() {
        this.server = ServerBuilder.forPort(Node.getInstance().getPort())
                .addService(new TokenServiceImpl()).build();
    }

    @Override
    public void run() {
        try {
            this.server.start();
            System.out.println("Server GRPC started at port " + Node.getInstance().getPort());
            this.server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdownServer() {
        this.server.shutdown();
    }

}
