package org.example.controller;

import org.example.models.Request;
import org.example.models.Response;
import org.example.models.User;
import org.example.server.Server;
import org.example.services.UserService;

import java.io.IOException;

public class AuthController extends Controller {
    private static AuthController INSTANCE_AUTH_CONTROLLER;
    public static AuthController getInstance() {
        if (INSTANCE_AUTH_CONTROLLER == null) {
            INSTANCE_AUTH_CONTROLLER = new AuthController();
        }
        return INSTANCE_AUTH_CONTROLLER;
    }
    @Override
    public void doGet(Request request, Response response) throws IOException {

    }

    @Override
    public void doPost(Request request, Response response) throws IOException {
        response.setController("/login");
        response.setAction("login");
        if (request.getAction().equals("login")) {
            if(Authentication.checkLogin(request)){
                response.setStatusCode("200");  // "OK" 200
                response.setData(request.getUser());

                User user = request.getUser();
                System.out.println("SERVER: ("+user.getRole()+") "+user.getName()+" đăng nhập thành công.");
            }else{
                response.setStatusCode("401");  // 401 : not authorized
                System.out.println("SERVER: Tài khoản hoặc mật khẩu không chính xác.");
            }

            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();

        }
    }

    @Override
    public void doPut(Request request, Response response) throws IOException {

    }

    @Override
    public void doDelete(Request request, Response response) throws IOException {

    }

    @Override
    public void init(Server context, Request request, Response response) {
        this.context = context;
        this.request = request;
        this.response = response;
        this.response.setController("/login");
    }
}
