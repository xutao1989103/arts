package com.xutao.arts.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xutao
 * @email xt111024@alibaba-inc.com
 * @create 2019-06-25 3:43 PM
 */

public class NioClient {
    private static Selector selector;

    public static void main(String[] args) throws IOException {
        selector = Selector.open();
        request();
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isConnectable()) {
                    handleConnect(key);
                } else if(key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private static void request() {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost",9999));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel)key.channel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        channel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        System.out.println("client receive msg from server:" + new String(data).trim());
    }

    private static void handleConnect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel)key.channel();
        if(channel.isConnectionPending()) {
            channel.finishConnect();
        }
        channel.configureBlocking(false);
        channel.write(ByteBuffer.wrap(new String("Hello Server!!").getBytes()));
        channel.register(selector, SelectionKey.OP_READ);
    }
}
