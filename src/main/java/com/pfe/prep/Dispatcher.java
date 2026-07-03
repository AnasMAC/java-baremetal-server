package com.pfe.prep;

import com.pfe.prep.router.Controller;
import com.pfe.prep.router.userController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Dispatcher
 * 1- read the header
 * 2- paras the header and take the rout
 * 3- return the right page or the right think
 */

public class Dispatcher {

    private Map<String, Controller> routers = new HashMap<>();

    public Dispatcher() {
        routers.put("/user", new userController());
    }

    public String rout(String header) {
        String[] info = paras(header);
        String res =
            "HTTP/1.1 404 Not Found\r\n\r\n<html><body>404 Not Found</body></html>";
        if (routers.containsKey(info[1])) {
            switch (info[0]) {
                case "GET":
                    res = routers.get(info[1]).doGet();
                    break;
                case "POST":
                    res = routers.get(info[1]).doPost();
                    break;
                case "PUT":
                    res = routers.get(info[1]).doPut();
                    break;
                case "DELETE":
                    res = routers.get(info[1]).doDelete();
                    break;
            }
        }
        return res;
    }

    private static String[] paras(String header) {
        System.out.println("the header: " + header);
        String[] supportedMethods = { "GET", "POST", "PUT", "DELETE" };
        String[] parts = header.split(" ");
        String[] info = { "", "/" };

        if (parts.length >= 2) {
            String path = parts[1];
            String method = parts[0];
            System.out.println("the method is " + method);
            boolean contains = Arrays.stream(supportedMethods).anyMatch(
                method::equals
            );
            if (contains) {
                System.out.println("is it contains " + contains);
                info[0] = method;
            }
            System.out.println("the parsed path : " + path);

            if (!path.equals("/")) {
                info[1] = path;
            }
        }
        return info;
    }
}
