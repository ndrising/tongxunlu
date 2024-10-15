package threeCS;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            String message;
            while (true) {
                System.out.println("1. 查看联系人信息");
                System.out.println("2. 添加新联系人");
                System.out.println("3. 修改联系人信息");
                System.out.println("4. 删除联系人");
                System.out.println("5. 退出");
                System.out.print("选择操作: ");
                String option = scanner.nextLine();

                if ("5".equals(option)) {
                    out.println("exit");
                    break;
                }

                out.println(option);
                switch (option) {
                    case "1":
                        System.out.println("所有联系人信息: ");
                        String contactInfo;
                        while (!(contactInfo = in.readLine()).equals("END")) {
                            System.out.println(contactInfo);
                        }
                        break;
                    case "2":
                        System.out.print("输入姓名: ");
                        String name = scanner.nextLine();
                        System.out.print("输入住址: ");
                        String address = scanner.nextLine();
                        System.out.print("输入电话: ");
                        String phone = scanner.nextLine();
                        out.println(name + "," + address + "," + phone);
                        System.out.println(in.readLine());  // 反馈添加结果
                        break;
                    case "3":
                        System.out.print("输入要修改的联系人姓名: ");
                        String modifyName = scanner.nextLine();
                        out.println(modifyName);

                        System.out.println("选择要修改的项: ");
                        System.out.println("1. 修改住址");
                        System.out.println("2. 修改电话号码");
                        System.out.println("3. 修改住址和电话号码");
                        String modifyOption = scanner.nextLine();
                        out.println(modifyOption);

                        String newAddress = "";
                        String newPhone = "";
                        switch (modifyOption) {
                            case "1":
                                System.out.print("输入新的住址: ");
                                newAddress = scanner.nextLine();
                                out.println(newAddress + ",");
                                break;
                            case "2":
                                System.out.print("输入新的电话: ");
                                newPhone = scanner.nextLine();
                                out.println("," + newPhone);
                                break;
                            case "3":
                                System.out.print("输入新的住址: ");
                                newAddress = scanner.nextLine();
                                System.out.print("输入新的电话: ");
                                newPhone = scanner.nextLine();
                                out.println(newAddress + "," + newPhone);
                                break;
                            default:
                                System.out.println("无效选项");
                        }
                        System.out.println(in.readLine());  // 反馈修改结果
                        break;
                    case "4":
                        System.out.print("输入要删除的联系人姓名: ");
                        String deleteName = scanner.nextLine();
                        out.println(deleteName);
                        System.out.println(in.readLine());  // 反馈删除结果
                        break;
                    default:
                        System.out.println("无效选项");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
