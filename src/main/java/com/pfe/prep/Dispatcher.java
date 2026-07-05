package com.pfe.prep;

import com.pfe.prep.db.CustomConnectionPool;
import com.pfe.prep.router.userController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Dispatcher
 * 1- read the header
 * 2- paras the header and take the rout
 * 3- return the right page or the right think
 */

public class Dispatcher {

    private Map<String, Controller> routers = new HashMap<>();
    private Set<Class<?>> discoveredComponents;

    public Dispatcher() {
        CustomConnectionPool db = new CustomConnectionPool();

        discoveredComponents = scannerComponents(
            "com.pfe.prep.router",
            "target/classes/com/pfe/prep/router"
        );

        for (Class<?> clazz : discoveredComponents) {
            System.out.println("Checking class: " + clazz.getName());
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                System.out.println("Found annotation on: " + clazz.getName());
                RequestMapping mapping = clazz.getAnnotation(
                    RequestMapping.class
                );
                String path = mapping.value();
                System.out.println("the path in the constructor: " + path);
                try {
                    Controller controllerInstance = (Controller) clazz
                        .getDeclaredConstructor(CustomConnectionPool.class)
                        .newInstance(db);
                    routers.put(path, controllerInstance);
                } catch (Exception e) {
                    System.err.println(
                        "Failed to instantiate controller: " + clazz.getName()
                    );
                    e.printStackTrace();
                }
            } else {
                System.out.println(
                    "Annotation NOT found on: " + clazz.getName()
                );
            }
        }
    }

    private Set<Class<?>> scannerComponents(String packageName, String dir) {
        File directory = new File(dir);
        File[] classFiles = directory.listFiles((dir1, name) ->
            name.endsWith(".class")
        );
        if (classFiles == null) {
            System.out.println(
                "Warning: Could not find directory " +
                    directory.getAbsolutePath()
            );
            return java.util.Collections.emptySet();
        }

        return Arrays.stream(classFiles)
            .map(file -> {
                String className =
                    packageName + "." + file.getName().replace(".class", "");
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .filter(clazz -> clazz != null)
            .collect(Collectors.toSet());
    }

    public String rout(String header) {
        for (String endpoint : routers.keySet()) {
            System.out.println("endpoint: " + endpoint);
        }
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

            info[1] = path;
        }
        return info;
    }
}
