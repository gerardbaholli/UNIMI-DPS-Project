package network;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Channel {

    ManagedChannel channel;
    private static Channel instance;

    private Channel() {
    }

    public synchronized static Channel getInstance() {
        if (instance == null)
            instance = new Channel();
        return instance;
    }

    public synchronized void setChannel(String IpAddress, int port) {
        this.channel = ManagedChannelBuilder
                .forTarget(IpAddress + ":" + port)
                .usePlaintext(true).build();
    }

    public synchronized ManagedChannel getChannel() {
        return channel;
    }

    public synchronized void shutdownChannel() {
        this.channel.shutdown();
    }

}
