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
}
