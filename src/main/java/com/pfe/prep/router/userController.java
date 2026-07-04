package com.pfe.prep.router;

import com.pfe.prep.Controller;
import com.pfe.prep.RequestMapping;

/**
 * userController
 */

@RequestMapping("/user")
public class userController extends Controller {

    @Override
    public String doGet() {
        try {
            String html = htmlLoader("user.html");
            StringBuilder res = new StringBuilder();
            res.append("HTTP/1.1 200 OK\r\n")
                .append("Content-Type: text/html\r\n")
                .append("Content-Length:" + html.length() + "\r\n")
                .append("\r\n")
                .append(html);
            return res.toString();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error in user controller: " + e);
            return "HTTP/1.1 500 Internal Server Error\r\n\r\n<html><body>500 Server Error</body></html>";
        }
    }
}
