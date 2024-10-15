import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ContactServer {

    // 存储联系人信息，使用HashMap模拟数据库
    private static Map<String, String[]> contacts = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // 创建一个HTTP服务器，监听8080端口
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HomePageHandler());
        server.createContext("/addContact", new AddContactHandler());
        server.createContext("/viewContacts", new ViewContactsHandler());
        server.createContext("/deleteContact", new DeleteContactHandler());

        server.setExecutor(null); // 使用默认的线程池
        System.out.println("Server started at http://localhost:8080");
        server.start();
    }

    static class HomePageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><body>" +
                    "<h1>添加新联系人</h1>" +
                    "<form action='/addContact' method='post'>" +
                    "姓名: <input type='text' name='name'><br><br>" +
                    "住址: <input type='text' name='address'><br><br>" +
                    "电话: <input type='text' name='phone'><br><br>" +
                    "<input type='submit' value='添加'>" +
                    "</form>" +
                    "<br><a href='/viewContacts'>查看所有联系人</a>" +
                    "</body></html>";

            exchange.getResponseHeaders().add("Content-Type", "text/html;charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    static class AddContactHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // 解析请求体中的表单数据
                String[] params = parseFormData(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String name = params[0], address = params[1], phone = params[2];

                // 添加联系人到HashMap
                contacts.put(name, new String[]{address, phone});
                exchange.getResponseHeaders().add("Location", "/viewContacts");
                exchange.sendResponseHeaders(303, -1); // 重定向到联系人列表页
            }
        }

        private String[] parseFormData(String formData) {
            // 解析表单数据格式：name=张三&address=北京&phone=123456
            String[] params = new String[3];
            String[] pairs = formData.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                switch (keyValue[0]) {
                    case "name":
                        params[0] = keyValue[1];
                        break;
                    case "address":
                        params[1] = keyValue[1];
                        break;
                    case "phone":
                        params[2] = keyValue[1];
                        break;
                }
            }
            return params;
        }
    }

    static class ViewContactsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder response = new StringBuilder();
            response.append("<html><body>");
            response.append("<h1>联系人列表</h1>");
            response.append("<table border='1'><tr><th>姓名</th><th>住址</th><th>电话</th><th>操作</th></tr>");

            for (Map.Entry<String, String[]> entry : contacts.entrySet()) {
                String name = entry.getKey();
                String address = entry.getValue()[0];
                String phone = entry.getValue()[1];
                response.append("<tr><td>").append(name).append("</td><td>")
                        .append(address).append("</td><td>").append(phone)
                        .append("</td><td><a href='/deleteContact?name=")
                        .append(name).append("'>删除</a></td></tr>");
            }

            response.append("</table>");
            response.append("<br><a href='/'>返回首页</a>");
            response.append("</body></html>");

            exchange.getResponseHeaders().add("Content-Type", "text/html;charset=UTF-8");
            exchange.sendResponseHeaders(200, response.toString().getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.toString().getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    static class DeleteContactHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.startsWith("name=")) {
                String name = query.split("=")[1];
                contacts.remove(name);
            }
            exchange.getResponseHeaders().add("Location", "/viewContacts");
            exchange.sendResponseHeaders(303, -1); // 重定向到联系人列表页
        }
    }
}
