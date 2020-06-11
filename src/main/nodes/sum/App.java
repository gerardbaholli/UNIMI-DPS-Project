package sum;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class App {

    public static void main(String[] args) {

        try {

            Server s = ServerBuilder.forPort(9999).addService(new SumServiceImpl()).build();

            s.start();

            System.out.println("Server started!");

            s.awaitTermination();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (InterruptedException e) {

            e.printStackTrace();

        }


    }

}