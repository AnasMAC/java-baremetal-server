package com.pfe.prep.core.net;

import com.pfe.prep.core.mvc.Dispatcher;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.*;

/**
 * NioWorker
 */
public class NioWorker implements Runnable {

    private SocketChannel sc;
    private Dispatcher dispatcher;

    public NioWorker(SocketChannel client, Dispatcher dispatcher) {
        this.sc = client;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        System.out.println("Server accepted a new client. Processing...");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuilder sb = new StringBuilder();
        try {
            // Thread.sleep(5000);
            int r = 1;
            while ((r = sc.read(buffer)) > 0) {
                buffer.flip();
                sb.append(
                    new String(
                        buffer.array(),
                        0,
                        buffer.limit(),
                        StandardCharsets.UTF_8
                    )
                );
                buffer.clear();
            }
            String message = sb.toString();

            String res = dispatcher.rout(message.split("\n")[0]);
            ByteBuffer resBuffer = ByteBuffer.wrap(res.getBytes());
            sc.write(resBuffer);
            resBuffer.clear();
            sc.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error in the worker: " + e);
        }
    }
}
