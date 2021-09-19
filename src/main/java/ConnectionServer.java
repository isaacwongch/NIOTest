import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConnectionServer {

    private Selector selector;
    private ServerSocketChannel socketChannel;

    public ConnectionServer(int port, String hostname) throws IOException {
        socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);

        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        socketChannel.socket().bind(new InetSocketAddress(hostname, port));

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                loopWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void loopWait() throws IOException {
        selector.select();

        for (SelectionKey sKey : selector.selectedKeys()) {
            if (sKey.isAcceptable()) {
                SocketChannel acceptedChannel = socketChannel.accept();

                acceptedChannel.configureBlocking(false);
                SelectionKey readKey = acceptedChannel.register(selector, SelectionKey.OP_READ);

                System.out.println("New client ip=" + acceptedChannel.getRemoteAddress());
            } else if (sKey.isReadable()) {
                SocketChannel channel = (SocketChannel) sKey.channel();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new ConnectionServer(12345, "localhost");
    }

//    private void test(){
//
//        RandomAccessFile file = new RandomAccessFile("/Users/IsaacWong/Documents/Material2021/ConnectionTest/src/main/resources/data/nio-data.txt", "rw");
//        FileChannel inChannel = file.getChannel();
//
//        ByteBuffer buffer = ByteBuffer.allocate(48);
//
//        int btyesRead = inChannel.read(buffer);
//
//        while(btyesRead != -1){
//            buffer.flip();
//
//            System.out.print("btyesRead: " + btyesRead);
//
//            while (buffer.hasRemaining()){
//                System.out.print((char) buffer.get());
//            }
//            buffer.clear();
//            btyesRead = inChannel.read(buffer);
//        }
//
//        file.close();
//    }
}
