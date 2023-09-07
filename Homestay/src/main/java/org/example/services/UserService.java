package org.example.services;


import org.example.models.Bank;
import org.example.models.User;
import org.example.untils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class UserService implements CRUD<User> {
    private static int nextIdUser;
    public static String address = "users.txt";
    public List<User> userList;

    public UserService() {
        userList = (List<User>) SerializationUtil.deserialize(address);
        if (userList.isEmpty()) {
            userList = new ArrayList<>();
        }
        nextIdUser = AppUtils.findNext(userList.stream().map(User::getId).collect(Collectors.toList()));
    }
    private static UserService INSTANCE_USER_SERVICE;
    public static UserService getInstance() {
        if (INSTANCE_USER_SERVICE == null) {
            INSTANCE_USER_SERVICE = new UserService();
        }
        return INSTANCE_USER_SERVICE;
    }
    @Override
    public void save() {
        SerializationUtil.serialize(userList, address);
    }


    @Override
    public User create(User user) {
        user.setId(nextIdUser);
        nextIdUser++;
        userList.add(user);
        save();
        return userList.get(userList.size() - 1);
    }

    @Override
    public void delete(User user) {
        User userdelete = null;
        for (User user1 : userList) {
            if (user1.getId() == (user.getId())) {
                userdelete = user1;
                break;
            }
        }
        userList.remove(userdelete);
        save();
    }


    public User update(int idUser) {
        List<User> usersL = UserService.INSTANCE_USER_SERVICE.readFile();
        if (userList != null) {
            User users = new User();
            PrintViews.printChooseEditUser();
            int choose = GetValue.getInt("Nhập lựa chọn: ");
            switch (choose) {
                case 1:
                   users = usersL.stream().filter(user -> user.getId() == idUser)
                           .map(user -> {
                        user.setUsername(checkUsername(GetValue.getString("Nhap username khach hang")));
                        user.setPassword(GetValue.getString("Nhap password khach hang"));
                        return user;
                    })
                           .findFirst()
                           .orElseThrow();
                    SerializationUtil.serialize(usersL, address);
                    break;
                case 2:
                    users = usersL.stream().filter(user -> user.getId() == idUser)
                            .map(user -> {
                        user.setName(GetValue.getString("Nhap ten khach hang"));
                        user.setPhone(GetValue.getPhone("Nhap so dien thoai khach hang"));
                        user.setAddress(GetValue.getString("Nhap dia chi khach hang"));
                        user.setEmail(GetValue.getEmail("Nhap email khach hang"));
                        user.setGender(GetValue.getGender("Chon gioi tinh"));
                        return user;
                    })
                            .findFirst()
                            .orElseThrow();
                    SerializationUtil.serialize(usersL, address);
                    break;
            }
            return users;
        }
        return new User();

    }
    @Override
    public void update(User user) {
        for (User u: userList) {
            if (user.getId() == u.getId()) {
                u.setName(user.getName());
                u.setBank(user.getBank());
                u.setPassword(user.getPassword());
                u.setUsername(user.getUsername());
                u.setAddress(user.getAddress());
                u.setEmail(user.getEmail());
                u.setPhone(user.getPhone());
                u.setClientIDUser(user.getClientIDUser());
                break;
            }
        }
        save();
    }

    @Override
    public List<User> readFile() {
        return  (List<User>) SerializationUtil.deserialize(address);
    }


    @Override
    public User find(int idUser) {
        if (userList != null) {
            for (User user1: userList) {
                if (user1.getId() == idUser) {
                    return user1;
                }
            }
        }
        return find(idUser);
    }


    public int checkIdUser(int idUser) {
        if (userList != null) {
            if (userList.stream().anyMatch(user -> user.getId()==idUser)) {
                return idUser;
            } else {
                System.out.println("ID da nhap khong ton tai");
                return checkIdUser(GetValue.getInt("Nhap lai id can chinh sua"));
            }

        }
        return -1;
    }
    public String checkUsername(String username) {
        if (userList != null) {
            if (userList.stream().anyMatch(user -> user.getUsername().equals(username))) {
                System.out.println("Tai khoan khach hang da ton tai!");
                return checkUsername(GetValue.getString("Nhap lai tai khoan"));
            }
            return username;
        }
        return "ERROR";
    }
    public void showUser(User user) {
         System.out.println("THÔNG TIN CÁ NHÂN");
         System.out.println("Full Name : " + user.getName());
         System.out.println("Username : " + user.getUsername());
         System.out.println("Gender : " + user.getGender());
         System.out.println("Phone : " + user.getPhone());
         System.out.println("Email : " + user.getEmail());
         System.out.println("Address :" + user.getAddress());
         Bank bank = user.getBank();
         if (bank != null) {
             System.out.println("Bank : " + ((bank.getNameBank() != null) ? bank.getNameBank() : " NONE "));
             System.out.println("Number card :" + ((bank.getCardID() != null) ? bank.getCardID() : " NONE "));
             System.out.println("}");
         } else {
             System.out.println("Bank : NONE ");
             System.out.println("Number card : NONE ");
         }
     }
}
