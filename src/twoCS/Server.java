package twoCS;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<String, String> contacts = new LinkedHashMap<>();

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
                                if (contacts.isEmpty()) {
                                    out.println("没有联系人信息");
                                } else {
                                    for (Map.Entry<String, String> entry : contacts.entrySet()) {
                                        String[] contactDetails = entry.getValue().split(",");
                                        out.println("姓名: " + entry.getKey());
                                        out.println("住址: " + contactDetails[0]);
                                        out.println("电话: " + contactDetails[1]);
                                        out.println("--------------------");
                                    }
                                }
                                out.println("END");
                                break;
                            case "2":
                                String[] newContact = in.readLine().split(",");
                                if (contacts.containsKey(newContact[0])) {
                                    out.println("联系人已存在，无法重复添加");
                                } else {
                                    contacts.put(newContact[0], newContact[1] + "," + newContact[2]);
                                    out.println("联系人已添加");
                                }
                                break;
                            case "3":
                                String modifyName = in.readLine();
                                if (contacts.containsKey(modifyName)) {
                                    String[] existingContact = contacts.get(modifyName).split(",");
                                    String existingAddress = existingContact[0];
                                    String existingPhone = existingContact[1];

                                    String modifyOption = in.readLine();
                                    switch (modifyOption) {
                                        case "1":
                                            String newAddress = in.readLine().split(",")[0];
                                            contacts.put(modifyName, newAddress + "," + existingPhone);
                                            out.println("住址已修改");
                                            break;
                                        case "2":
                                            String newPhone = in.readLine().split(",")[1];
                                            contacts.put(modifyName, existingAddress + "," + newPhone);
                                            out.println("电话号码已修改");
                                            break;
                                        case "3":
                                            String[] newContactDetails = in.readLine().split(",");
                                            contacts.put(modifyName, newContactDetails[0] + "," + newContactDetails[1]);
                                            out.println("住址和电话号码已修改");
                                            break;
                                    }
                                } else {
                                    out.println("联系人不存在");
                                }
                                break;
                            case "4":
                                String deleteName = in.readLine();
                                if (contacts.containsKey(deleteName)) {
                                    contacts.remove(deleteName);
                                    out.println("联系人已删除");
                                } else {
                                    out.println("联系人不存在");
                                }
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
