package com.hps.prep;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try (ServerSocket sc = new ServerSocket()) {
            sc.bind(new InetSocketAddress("127.0.0.1", 8080));
            ExecutorService es = Executors.newFixedThreadPool(10);
            System.out.println("the server is ready");
            while (true) {
                Socket cleint = sc.accept();
                Runnable worker = () -> {
                    System.out.println(
                        "Server accepted a new client. Processing..."
                    );

                    try {
                        Thread.sleep(5000);
                        PrintWriter pw = new PrintWriter(
                            new BufferedWriter(
                                new OutputStreamWriter(cleint.getOutputStream())
                            )
                        );
                        pw.println("HTTP/1.1 200 OK");
                        pw.println("Content-Type: text/html");
                        pw.println("Content-Length: 53");
                        pw.println();
                        pw.println(
                            "<html><body><h1>Bare-Metal is UP!</h1></body></html>"
                        );
                        pw.flush();
                        pw.close();
                        cleint.close();
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println("error with the client :" + e);
                    }
                };
                es.execute(worker);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error in the main method with :" + e);
        }
    }
}
