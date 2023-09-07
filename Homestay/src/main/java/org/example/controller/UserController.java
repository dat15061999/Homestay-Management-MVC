package org.example.controller;

import lombok.NoArgsConstructor;
import org.example.models.User;
import org.example.models.Request;
import org.example.models.Response;
import org.example.server.Server;
import org.example.services.UserService;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public class UserController extends Controller {
    private UserService userService;
    private static UserController INSTANCE_USER_CONTROLLER;

    public static UserController getInstance() {
        if (INSTANCE_USER_CONTROLLER == null) {
            INSTANCE_USER_CONTROLLER = new UserController();
        }
        return INSTANCE_USER_CONTROLLER;
    }

    @Override
    public void doGet(Request request, Response response) throws IOException {
        if (request.getAction().equals("show")) {
            //Su dung doi tuong DecimalFormat de lam tron so
            response.setStatusCode("OK");


            User user = request.getUser();
            if (user.getRole().equals("Client")) {
                response.setAction("showUser");
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xem thông tin cá nhân.");
                response.setData(user);
            } else {
                response.setAction("show");
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xem danh sách khách hàng.");
                List<User> userList = List.copyOf(userService.readFile());
                response.setData(userList);
            }

            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        }

    }

    @Override
    public void doPost(Request request, Response response) throws IOException {
        if (request.getAction().equals("create")) {
            response.setStatusCode("OK");
            response.setAction("create");

            User user = request.getUser();
            User p = (User) request.getData();
            p = userService.create(p);
            if(user == null) {
                System.out.println("SERVER: Them nguoi dung " + p.getName() + " thanh cong.");
            } else {
            System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Them nguoi dung " + p.getName() + " thanh cong.");
            }


            response.setData(p);
            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        }
    }

    @Override
    public void doPut(Request request, Response response) throws IOException {
        if (request.getAction().equals("update")) {
            response.setStatusCode("OK");
            response.setAction("update");

            User user = request.getUser();
            User p = (User) request.getData();

            userService.update(p);
            if (user.getRole().equals("Admin") || user.getRole().equals("Cashier")) {
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Chinh sua thong tin nguoi dung " + p.getName() + " thanh cong.");
            } else {
                System.out.println("SERVER: Khách hàng " + user.getName() + " chinh sửa thông tin thanh công.");
            }

            response.setData(p);
            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        }
    }

    @Override
    public void doDelete(Request request, Response response) throws IOException {
        if (request.getAction().equals("delete") && request.getUser().getRole().equals("Admin")) {
            response.setStatusCode("OK");
            response.setAction("delete");

            User user = request.getUser();
            User p = (User) request.getData();
            if (p != null) {
                userService.delete(p);
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xoa nguoi dung thanh cong.");

                response.setData(request.getUser());
                response.getObjectOutputStream().writeObject(response);
                response.getObjectOutputStream().flush();
            }
        } else {
            System.out.println("SERVER: (" + request.getUser().getRole() + ") " + request.getUser().getName() + " : Không đủ quyền xóa người dùng.");
            Authentication.sendPermission(response);
        }
    }

    @Override
    public void init(Server context, Request request, Response response) {
        userService = UserService.getInstance();
        this.context = context;
        this.request = request;
        this.response = response;
        this.response.setController("/user");
    }
}
