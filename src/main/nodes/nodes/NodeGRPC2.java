package nodes;

public class NodeGRPC2 {

    public static void main(String[] args) {

        final String IP = "localhost";
        final int PORT = 8081;
        int idNode = 2;
        String targetIp = "localhost";
        int targetPort = 8080;

        ServerGRPC serverGRPC = new ServerGRPC(PORT);
        ClientGRPC clientGRPC = new ClientGRPC(idNode, targetIp, targetPort);

        serverGRPC.start();
        clientGRPC.start();

    }

}
