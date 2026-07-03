package com.pfe.prep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWorker implements Runnable {

    private Socket client;
    private Dispatcher dispatcher;

    public ClientWorker(Socket client, Dispatcher dispatcher) {
        this.client = client;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        System.out.println("Server accepted a new client. Processing...");
        try {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(client.getInputStream())
            );
            String line;
            if ((line = br.readLine()) != null) {
                String res = dispatcher.rout(line);
                PrintWriter pw = new PrintWriter(
                    new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream())
                    )
                );
                System.out.println("response : " + res);
                pw.println(res);
                pw.flush();
                pw.close();
            }
            client.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error with the client :" + e);
        }
    }
}
