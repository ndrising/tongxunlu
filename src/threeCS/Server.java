package threeCS;

import java.io.*;
import java.net.*;

public class Server {
    private static BusinessLogic businessLogic = new BusinessLogic();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("服务器启动，等待连接...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("客户端连接成功");

                    String option;
                    while ((option = in.readLine()) != null) {
                        if ("exit".equals(option)) {
                            break;
                        }

                        switch (option) {
                            case "1":
                                out.println(businessLogic.viewContacts());
                                out.println("END");
                                break;
                            case "2":
                                String[] newContact = in.readLine().split(",");
                                out.println(businessLogic.addContact(newContact[0], newContact[1], newContact[2]));
                                break;
                            case "3":
                                String modifyName = in.readLine();
                                String modifyOption = in.readLine();

                                String newAddress = "", newPhone = "";
                                switch (modifyOption) {
                                    case "1": // 修改住址
                                        newAddress = in.readLine().split(",")[0];
                                        break;
                                    case "2": // 修改电话
                                        newPhone = in.readLine().split(",")[1];
                                        break;
                                    case "3": // 修改住址和电话
                                        String[] details = in.readLine().split(",");
                                        newAddress = details[0];
                                        newPhone = details[1];
                                        break;
                                }
                                out.println(businessLogic.modifyContact(modifyName, newAddress, newPhone));
                                break;
                            case "4":
                                String deleteName = in.readLine();
                                out.println(businessLogic.deleteContact(deleteName));
                                break;
                            default:
                                out.println("无效选项");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
