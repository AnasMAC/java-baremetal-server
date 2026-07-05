package com.pfe.prep.app.router;

import com.pfe.prep.core.annotations.RequestMapping;
import com.pfe.prep.core.db.CustomConnectionPool;
import com.pfe.prep.core.mvc.Controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * userController - Pure Java Server-Side Rendering
 */
@RequestMapping("/user")
public class userController extends Controller {

    private CustomConnectionPool db;

    public userController(CustomConnectionPool db) {
        this.db = db;
    }

    @Override
    public String doGet() {
        Connection conn = null;
        try {
            // 1. Borrow a connection from the pool
            conn = db.getConnection();

            // 2. Execute the query
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM users ORDER BY id ASC"
            );
            ResultSet rs = stmt.executeQuery();

            // 3. Build the ENTIRE HTML page dynamically in Java
            StringBuilder html = new StringBuilder();

            // --- HTML HEAD & TAILWIND ---
            html.append("<!DOCTYPE html>\n")
                .append("<html lang='en'>\n")
                .append("<head>\n")
                .append("    <meta charset='UTF-8'>\n")
                .append(
                    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n"
                )
                .append("    <title>Bare-Metal Server Dashboard</title>\n")
                .append(
                    "    <script src='https://cdn.tailwindcss.com'></script>\n"
                )
                .append("</head>\n")
                .append(
                    "<body class='bg-slate-100 p-8 font-sans text-slate-800'>\n"
                )
                .append(
                    "    <div class='max-w-4xl mx-auto bg-white rounded-xl shadow-lg border border-slate-200 p-8'>\n"
                );

            // --- HEADER & BUTTONS ---
            html.append(
                "        <div class='flex justify-between items-center mb-8 border-b pb-4'>\n"
            )
                .append("            <div>\n")
                .append(
                    "                <h1 class='text-3xl font-bold text-slate-900'>User Management</h1>\n"
                )
                .append(
                    "                <p class='text-sm text-slate-500 mt-1'>Powered by Custom Bare-Metal Java Engine</p>\n"
                )
                .append("            </div>\n")
                .append("            <div class='flex gap-3'>\n")
                .append(
                    "                <button onclick='addUser()' class='bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg shadow-sm transition-colors'>+ Add Random User</button>\n"
                )
                .append(
                    "                <button onclick='updateUser()' class='bg-amber-500 hover:bg-amber-600 text-white font-semibold py-2 px-4 rounded-lg shadow-sm transition-colors'>✎ Update User 1</button>\n"
                )
                .append(
                    "                <button onclick='deleteUser()' class='bg-red-600 hover:bg-red-700 text-white font-semibold py-2 px-4 rounded-lg shadow-sm transition-colors'>🗑 Delete User 1</button>\n"
                )
                .append("            </div>\n")
                .append("        </div>\n");

            // --- SQL DATA TABLE ---
            html.append(
                "        <div class='overflow-hidden rounded-lg border border-slate-200 shadow-sm'>\n"
            )
                .append(
                    "            <table class='min-w-full divide-y divide-slate-200'>\n"
                )
                .append("                <thead class='bg-slate-50'><tr>\n")
                .append(
                    "                    <th class='px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider'>ID</th>\n"
                )
                .append(
                    "                    <th class='px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider'>Name</th>\n"
                )
                .append(
                    "                    <th class='px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider'>Email</th>\n"
                )
                .append("                </tr></thead>\n")
                .append(
                    "                <tbody class='bg-white divide-y divide-slate-200'>\n"
                );

            // Inject SQL rows into the table
            while (rs.next()) {
                html.append("                <tr class='hover:bg-slate-50'>\n")
                    .append(
                        "                    <td class='px-6 py-4 whitespace-nowrap text-sm text-slate-900'>"
                    )
                    .append(rs.getInt("id"))
                    .append("</td>\n")
                    .append(
                        "                    <td class='px-6 py-4 whitespace-nowrap text-sm text-slate-900'>"
                    )
                    .append(rs.getString("name"))
                    .append("</td>\n")
                    .append(
                        "                    <td class='px-6 py-4 whitespace-nowrap text-sm text-slate-500'>"
                    )
                    .append(rs.getString("email"))
                    .append("</td>\n")
                    .append("                </tr>\n");
            }

            // --- JAVASCRIPT LOGIC ---
            html.append("                </tbody></table>\n")
                .append("        </div>\n")
                .append("    </div>\n")
                .append("    <script>\n")
                .append("        function handleResponse(response) {\n")
                .append(
                    "            if (response.ok) { window.location.reload(); }\n"
                )
                .append(
                    "            else { alert('Server Error! Check Java console.'); }\n"
                )
                .append("        }\n")
                .append(
                    "        function addUser() { fetch('/user', { method: 'POST' }).then(handleResponse); }\n"
                )
                .append(
                    "        function updateUser() { fetch('/user', { method: 'PUT' }).then(handleResponse); }\n"
                )
                .append(
                    "        function deleteUser() { fetch('/user', { method: 'DELETE' }).then(handleResponse); }\n"
                )
                .append("    </script>\n")
                .append("</body>\n")
                .append("</html>");

            String finalHtml = html.toString();

            // 4. Return the HTTP Response
            StringBuilder res = new StringBuilder();
            res.append("HTTP/1.1 200 OK\r\n")
                .append("Content-Type: text/html\r\n")
                .append("Content-Length:")
                .append(finalHtml.length())
                .append("\r\n")
                .append("\r\n")
                .append(finalHtml);

            return res.toString();
        } catch (Exception e) {
            System.out.println("Error in user controller GET: " + e);
            return "HTTP/1.1 500 Internal Server Error\r\n\r\n<html><body>500 Server Error</body></html>";
        } finally {
            if (conn != null) {
                db.releaseConnection(conn);
            }
        }
    }

    @Override
    public String doPost() {
        Connection conn = null;
        try {
            conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (name, email) VALUES (?, ?)"
            );
            stmt.setString(1, "New User");
            stmt.setString(
                2,
                "user_" + System.currentTimeMillis() + "@hps.com"
            );
            int rowsAffected = stmt.executeUpdate();

            return "HTTP/1.1 201 Created\r\nContent-Length: 0\r\n\r\n";
        } catch (Exception e) {
            System.out.println("Error in user controller POST: " + e);
            return "HTTP/1.1 500 Internal Server Error\r\n\r\n<html><body>500 Server Error</body></html>";
        } finally {
            if (conn != null) db.releaseConnection(conn);
        }
    }

    @Override
    public String doPut() {
        Connection conn = null;
        try {
            conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE users SET name = ? WHERE id = ?"
            );
            stmt.setString(1, "Updated User");
            stmt.setInt(2, 1);
            int rowsAffected = stmt.executeUpdate();

            return "HTTP/1.1 200 OK\r\nContent-Length: 0\r\n\r\n";
        } catch (Exception e) {
            System.out.println("Error in user controller PUT: " + e);
            return "HTTP/1.1 500 Internal Server Error\r\n\r\n<html><body>500 Server Error</body></html>";
        } finally {
            if (conn != null) db.releaseConnection(conn);
        }
    }

    @Override
    public String doDelete() {
        Connection conn = null;
        try {
            conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM users WHERE id = ?"
            );
            stmt.setInt(1, 1);
            int rowsAffected = stmt.executeUpdate();

            return "HTTP/1.1 200 OK\r\nContent-Length: 0\r\n\r\n";
        } catch (Exception e) {
            System.out.println("Error in user controller DELETE: " + e);
            return "HTTP/1.1 500 Internal Server Error\r\n\r\n<html><body>500 Server Error</body></html>";
        } finally {
            if (conn != null) db.releaseConnection(conn);
        }
    }
}
