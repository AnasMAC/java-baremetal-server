package com.pfe.prep.core.net;

import com.pfe.prep.core.mvc.Dispatcher;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NioServer
 */
public class NioServer {

    private Integer port;
    private String host;

    public NioServer(Integer port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() {
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.bind(new InetSocketAddress(host, port));
            Dispatcher dispatcher = new Dispatcher();
            Selector selector = Selector.open();
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("NIO Server started on " + host + ":" + port);
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        new NioWorker(sc, dispatcher).run();
                    }
                    iter.remove();
                }
            }
        } catch (Exception e) {
            System.out.println("erro in the nio server : " + e);
            // TODO: handle exception
        }
    }
}
