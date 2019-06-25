package com.xutao.arts.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xutao
 * @email xt111024@alibaba-inc.com
 * @create 2019-06-25 3:26 PM
 */

public class NioServer {

    private static Selector selector;

    public static void main(String[] args) throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(9999));

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()) {
                    handleAccept(key);
                } else if(key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        System.out.println("receive msg from client:"+ new String(data).trim());
        RandomAccessFile file = new RandomAccessFile("/Users/xutao/code/arts/arts/2019-6/2nd-week/C/src/main/java/com/xutao/arts/App.java","rw");
        FileChannel fileChannel = file.getChannel();
        fileChannel.read(byteBuffer);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
        byteBuffer.clear();
    }

    private static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel)key.channel();
        SocketChannel socketChannel = server.accept();
        System.out.println("accept client:" + socketChannel.getLocalAddress());
        socketChannel.configureBlocking(false);

        socketChannel.write(ByteBuffer.wrap(new String("hello client!!").getBytes()));
        socketChannel.register(selector, SelectionKey.OP_READ);
    }
}
