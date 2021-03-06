package burtis.modules.network.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import burtis.modules.network.AbstractSocketService;

/**
 * Manages connection to specified address/port pair.
 * 
 * Allows for opening, closing and reopening connection. Connection status can
 * be retrieved. Thread-safety: each action on Socket is done in Locked block.
 * 
 * @author Amadeusz Sadowski
 */
class ClientSocketService extends AbstractSocketService
{
    private static final Logger logger = Logger
            .getLogger(ClientConnection.class.getName());
    private final String serverAddress;

    /**
     * Creates new unconnected client connection to server. Call
     * {@link #connect()} to attempt connection with server.
     * 
     * @param serverAddress
     *            - the address of server to connect to.
     * @param serverPort
     *            - the port of the server.
     */
    public ClientSocketService(final String serverAddress, final int serverPort)
    {
        super(serverPort, logger);
        this.serverAddress = serverAddress;
    }

    @Override
    protected SocketChannel socketChannelFactory() throws IOException
    {
        final SocketAddress address = new InetSocketAddress(serverAddress,
                getPort());
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
        socketChannel.connect(address);
        socketChannel.finishConnect();
        return socketChannel;
    }
}
