package com.xutao.arts.nio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xutao
 * @email xt111024@alibaba-inc.com
 * @create 2019-06-25 2:32 PM
 */

public class BioServer {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("启动服务器....");
            while (true) {
                Socket s = ss.accept();
                pool.submit(() -> {
                    try {
                        handle(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handle(Socket s) throws IOException {
        System.out.println(Thread.currentThread().getName() + "-客户端:" + InetAddress.getLocalHost() + s.toString() + "已连接到服务器");
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        //读取客户端发送来的消息
        String mess = br.readLine();
        System.out.println(Thread.currentThread().getName() + "-客户端:" + mess);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        bw.write(mess + "\n");
        bw.flush();
        s.close();
    }
}
