package org.example.controller;

import org.example.models.Request;
import org.example.models.Response;
import org.example.models.User;
import org.example.services.UserService;

import java.io.IOException;
import java.util.List;

public class Authentication {
    static UserService userService = new UserService();

    public static boolean checkLogin(Request request) {
        List<User> userList = (List<User>) userService.readFile();
        User user = (User) request.getData();

        if (userList.stream().anyMatch(user1 -> user1.getUsername().equals(user.getUsername())
                && user1.getPassword().equals(user.getPassword())
                && user1.getRole().equals("Admin"))) {
            User p = userList.stream().filter(user1 -> user1.getUsername()
                            .equals(user.getUsername())&& user1.getPassword().equals(user.getPassword()))
                    .findFirst().orElseThrow(null);


            request.setUser(p);
            return true;
        }
        if (userList.stream().anyMatch(user1 -> user1.getUsername().equals(user.getUsername())
                && user1.getPassword().equals(user.getPassword())
                && user1.getRole().equals("Cashier"))) {
            User p = userList.stream().filter(user1 -> user1.getUsername()
                            .equals(user.getUsername())&& user1.getPassword().equals(user.getPassword()))
                    .findFirst().orElseThrow(null);

            request.setUser(p);
            return true;
        }
        if (userList.stream().anyMatch(user1 -> user1.getUsername().equals(user.getUsername())
                && user1.getPassword().equals(user.getPassword())
                && user1.getRole().equals("Client"))) {
            User p = userList.stream().filter(user1 -> user1.getUsername()
                            .equals(user.getUsername())&& user1.getPassword().equals(user.getPassword()))
                    .findFirst().orElseThrow(null);

            request.setUser(p);
            return true;
        }
        return false;
    }
    public static boolean hasRole(Request request, String role) {
        return request.getUser().getRole().equals(role);
    }

    public static void sendPermission(Response response) throws IOException {
        response.setController("/authentication");
        response.setAction("authentication");
        response.setStatusCode("401");
        response.getObjectOutputStream().writeObject(response);
        response.getObjectOutputStream().flush();
    }
}
