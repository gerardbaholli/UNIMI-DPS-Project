package nodes;

public class NodeGRPC3 {

    public static void main(String[] args) {

        final String IP = "localhost";
        final int PORT = 8082;
        int idNode = 3;
        String targetIp = "localhost";
        int targetPort = 8081;

        ServerGRPC serverGRPC = new ServerGRPC(PORT);
        //ClientGRPC clientGRPC = new ClientGRPC(idNode, targetIp, targetPort);

        serverGRPC.start();
        //clientGRPC.start();

    }

}
