package com.hps.prep;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWorker implements Runnable {

    private Socket client;

    public ClientWorker(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("Server accepted a new client. Processing...");

        try {
            Thread.sleep(5000);
            PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream())
                )
            );
            pw.println("HTTP/1.1 200 OK");
            pw.println("Content-Type: text/html");
            pw.println("Content-Length: 53");
            pw.println();
            pw.println("<html><body><h1>Bare-Metal is UP!</h1></body></html>");
            pw.flush();
            pw.close();
            client.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error with the client :" + e);
        }
    }
}
