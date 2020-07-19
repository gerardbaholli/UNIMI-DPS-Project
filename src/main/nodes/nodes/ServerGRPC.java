package nodes;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerGRPC extends Thread {

    private Node node;

    public ServerGRPC(Node node){
        this.node = node;
    }

    @Override
    public void run(){

        Server server = ServerBuilder.forPort(node.getPort())
                .addService(new NodeServiceImpl(node)).build();

        try {

            server.start();

            System.out.println("gRPC server started at port " + node.getPort());

            server.awaitTermination();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        server.shutdown();


    }



}
