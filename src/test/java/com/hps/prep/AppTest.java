package com.hps.prep;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    /**
     * Conection Test
     */
    public void connectionApp() {
        String expectedResponse =
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: 53\r\n" +
            "\r\n" +
            "<html><body><h1>Bare-Metal is UP!</h1></body></html>\r\n";
        try (Socket sc = new Socket("127.0.0.1", 8080)) {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(sc.getInputStream())
            );
            StringBuilder actualMessage = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                actualMessage.append(line).append("\r\n");
            }
            assertEquals(expectedResponse, actualMessage.toString());
        } catch (Exception e) {
            throw new RuntimeException("Test failed due to network error", e);
        }
    }

    /**
     * Concurrency Test
     */
    public void concurrencyTest() {
        ExecutorService es = Executors.newFixedThreadPool(2);

        Runnable clientTask = () -> {
            long start = System.currentTimeMillis();
            try (Socket sc = new Socket("127.0.0.1", 8080)) {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(sc.getInputStream())
                );
                in.readLine();

                long end = System.currentTimeMillis();
                System.out.println(
                    "Client finished in: " + (end - start) + " ms"
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        es.execute(clientTask);
        es.execute(clientTask);

        es.shutdown();

        try {
            // Force the main test thread to wait up to 15 seconds for the clients to finish
            es.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdownNow();
    }
}
