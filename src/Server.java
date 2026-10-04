import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    private static final int PORT = 8080;
    private static final String HOST = "0.0.0.0";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);
        server.createContext("/", Server::handleRequest);
        server.setExecutor(null);
        server.start();

        System.out.println("DevStudy API server listening on " + HOST + ":" + PORT);
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod()) && "/".equals(exchange.getRequestURI().getPath())) {
            sendPlainText(exchange, 200, "DevStudy API\n");
            return;
        }

        sendPlainText(exchange, 404, "Not Found\n");
    }

    private static void sendPlainText(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream body = exchange.getResponseBody()) {
            body.write(bytes);
        }
    }
}
