package com.gobeyond.registration;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) throws IOException {

        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "8080")
        );

        RegistrationService service = new RegistrationService();

        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 0
        );

        // Health check endpoint
        server.createContext("/health", exchange -> {
            sendResponse(exchange, 200, "Application is healthy");
        });

        // Registration endpoint
        server.createContext("/api/register", exchange -> {

            if (!exchange.getRequestURI().getPath().equals("/api/register")) {
                sendResponse(exchange, 404, "Not Found");
                return;
            }

            if (!exchange.getRequestMethod().equals("POST")) {
                exchange.getResponseHeaders().set("Allow", "POST");
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            try {
                String requestBody = new String(
                        exchange.getRequestBody().readNBytes(10241),
                        StandardCharsets.UTF_8
                );

                if (requestBody.length() > 10240) {
                    sendResponse(exchange, 413, "Request too large");
                    return;
                }

                Map<String, String> params = parseFormData(requestBody);

                String name = params.get("name");
                String email = params.get("email");

                String result = service.register(name, email);

                sendResponse(exchange, 200, result);

            } catch (IllegalArgumentException e) {
                sendResponse(exchange, 400, e.getMessage());
            } catch (Exception e) {
                sendResponse(exchange, 500, "Internal Server Error");
            }
        });

        // Simple browser interface
        server.createContext("/", exchange -> {

            String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>Registration Application</title>
                    </head>
                    <body>
                        <h2>Registration Application</h2>
                        <form action="/api/register" method="POST">
                            <label>Name:</label>
                            <input type="text" name="name" required>
                            <br><br>
                            <label>Email:</label>
                            <input type="email" name="email" required>
                            <br><br>
                            <button type="submit">Register</button>
                        </form>
                    </body>
                    </html>
                    """;

            exchange.getResponseHeaders().set(
                    "Content-Type", "text/html; charset=UTF-8"
            );

            sendResponse(exchange, 200, html);
        });

        server.setExecutor(null);
        server.start();

        System.out.println("Registration application started on port " + port);
    }

    private static Map<String, String> parseFormData(String body) {

        Map<String, String> params = new HashMap<>();

        for (String pair : body.split("&")) {

            String[] parts = pair.split("=", 2);

            String key = URLDecoder.decode(
                    parts[0], StandardCharsets.UTF_8
            );

            String value = parts.length > 1
                    ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                    : "";

            params.put(key, value);
        }

        return params;
    }

    private static void sendResponse(
            HttpExchange exchange, int status, String response
    ) throws IOException {

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type", "text/plain; charset=UTF-8"
        );

        exchange.sendResponseHeaders(status, bytes.length);

        try (var output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}