package com.pfe.prep.core.mvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controller
 */
public abstract class Controller {

    private String notFound =
        "HTTP/1.1 404 Not Found\r\n\r\n<html><body>404 Not Found</body></html>";

    public String doGet() {
        return notFound;
    }

    public String doPost() {
        return notFound;
    }

    public String doPut() {
        return notFound;
    }

    public String doDelete() {
        return notFound;
    }

    protected String htmlLoader(String url)
        throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();

        try (
            BufferedReader br = new BufferedReader(
                new FileReader(new File("template/" + url))
            )
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}
