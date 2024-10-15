package threeCS;

import java.util.HashMap;
import java.util.Map;

public class BusinessLogic {
    private Map<String, String> contacts = new HashMap<>();

    public String viewContacts() {
        if (contacts.isEmpty()) {
            return "没有联系人信息";
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : contacts.entrySet()) {
            String[] contactDetails = entry.getValue().split(",");
            result.append("姓名: ").append(entry.getKey())
                    .append(", 住址: ").append(contactDetails[0])
                    .append(", 电话: ").append(contactDetails[1])
                    .append("\n");
        }
        return result.toString();
    }

    public String addContact(String name, String address, String phone) {
        if (contacts.containsKey(name)) {
            return "联系人已存在，无法重复添加";
        }
        contacts.put(name, address + "," + phone);
        return "联系人已添加";
    }

    public String modifyContact(String name, String newAddress, String newPhone) {
        if (!contacts.containsKey(name)) {
            return "联系人不存在";
        }
        String[] currentDetails = contacts.get(name).split(",");
        String address = newAddress.isEmpty() ? currentDetails[0] : newAddress;
        String phone = newPhone.isEmpty() ? currentDetails[1] : newPhone;
        contacts.put(name, address + "," + phone);
        return "联系人已修改";
    }

    public String deleteContact(String name) {
        if (!contacts.containsKey(name)) {
            return "联系人不存在";
        }
        contacts.remove(name);
        return "联系人已删除";
    }
}
