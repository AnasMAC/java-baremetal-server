package com.hps.prep;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private Integer port;
    private String host;
    private ExecutorService es;

    public HttpServer(Integer port, String host) {
        this.port = port;
        this.host = host;
        es = Executors.newCachedThreadPool();
    }

    public void start() {
        try (ServerSocket sc = new ServerSocket()) {
            sc.bind(new InetSocketAddress(host, port.intValue()));
            System.out.println("the server is ready ");
            while (true) {
                Socket client = sc.accept();
                es.execute(new ClientWorker(client));
            }
        } catch (Exception e) {
            System.out.println("error in the main method with :" + e);
        }
    }
}
