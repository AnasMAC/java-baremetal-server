package com.pfe.prep;

import com.pfe.prep.core.net.HttpServer;
import com.pfe.prep.core.net.NioServer;
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
        // HttpServer hs = new HttpServer(8080, "127.0.0.1");
        // hs.start();
        NioServer ns = new NioServer(8080, "127.0.0.1");
        ns.start();
    }
}
