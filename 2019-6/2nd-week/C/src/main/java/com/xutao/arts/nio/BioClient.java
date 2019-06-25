package com.xutao.arts.nio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xutao
 * @email xt111024@alibaba-inc.com
 * @create 2019-06-25 2:33 PM
 */

public class BioClient {
    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        for(int i = 0; i < 12; i++) {
            int finalI = i;
            pool.submit(() -> request(String.valueOf(finalI)));
        }

    }

    public static void request(String id) {
        try {
            Socket s = new Socket("127.0.0.1",9999);

            //构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write("client:" + id + ",测试客户端和服务器通信，服务器接收到消息返回到客户端\n");
            bw.flush();

            //读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            System.out.println("服务器："+mess);
            s.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
