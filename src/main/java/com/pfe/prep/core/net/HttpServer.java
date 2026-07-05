package com.pfe.prep.core.net;

import com.pfe.prep.core.mvc.Dispatcher;
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
        es = Executors.newFixedThreadPool(10);
    }

    public void start() {
        try (ServerSocket sc = new ServerSocket()) {
            sc.bind(new InetSocketAddress(host, port.intValue()));
            System.out.println("the server is ready ");
            Dispatcher dispatcher = new Dispatcher();
            while (true) {
                Socket client = sc.accept();
                es.execute(new ClientWorker(client, dispatcher));
            }
        } catch (Exception e) {
            System.out.println("error in the main method with :" + e);
        }
    }
}
