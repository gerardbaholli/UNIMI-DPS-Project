import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;


public class StartGateway {

    private static final String HOST = "localhost";
    private static final int PORT = 1200;


    public static void main(String[] args) throws IOException {
        System.out.println("ok");
        HttpServer server = HttpServerFactory.create("http://"+HOST+":"+PORT+"/");
        server.start();

        System.out.println("Gateway running!");
        System.out.println("Gateway started on: http://"+HOST+":"+PORT);

        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping gateway");
        server.stop(0);
        System.out.println("Gateway stopped");
    }
}
