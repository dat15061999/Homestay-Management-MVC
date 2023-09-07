package org.example.controller;

import org.example.models.*;
import org.example.server.Server;
import org.example.services.NotifyService;

import java.io.IOException;
import java.util.List;

public class NotifyController extends Controller {
    private NotifyService notifyService;
    private static NotifyController INSTANCE_NOTIFY_CONTROLLER;

    public static NotifyController getInstance() {
        if (INSTANCE_NOTIFY_CONTROLLER == null) {
            INSTANCE_NOTIFY_CONTROLLER = new NotifyController();
        }
        return INSTANCE_NOTIFY_CONTROLLER;
    }

    @Override
    public void doGet(Request request, Response response) throws IOException {
        if (request.getAction().equals("show")) {
            response.setStatusCode("OK");

            User user = request.getUser();
            if (user.getRole().equals("Client")) {
                response.setAction("showNotify");
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xem danh sách thông báo.");
                List<Notify> notifyList = notifyService.find(user);
                response.setData(notifyList);
            } else {
                response.setAction("show");
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xem danh sách thông báo.");
                List<Notify> notifyList = List.copyOf(notifyService.readFile());
                response.setData(notifyList);
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
            Notify p = (Notify) request.getData();
            p = notifyService.create(p);
            System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Thêm thông báo thành công.");

            response.setData(p);
            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        } else if (request.getAction().equals("notify")) {
            response.setStatusCode("OK");

            User user = request.getUser();
            Messenger messenger = (Messenger) request.getData();
            messenger.setUser(user);
            context.sendTo(messenger.getClientID(), messenger);

            response.setAction("to");
            response.setController("/notify");
            response.setData("Đã gửi cho client: " + messenger.getClientID());
            System.out.println("SERVER: Gửi về thông tin thành công.");
            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();

        }

    }

    @Override
    public void doPut(Request request, Response response) throws IOException {
        if (request.getAction().equals("update") && request.getUser().getRole().equals("Admin")) {
            response.setStatusCode("OK");
            response.setAction("update");

            User user = (User) request.getUser();
            Notify notify = (Notify) request.getData();
            notifyService.update(notify);
            System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Sửa thông báo thành công.");


            response.setData(notify);
            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        } else {
            System.out.println("SERVER: (" + request.getUser().getRole() + ") " + request.getUser().getName() + " : Không đủ quyền sửa thông báo.");
            Authentication.sendPermission(response);
        }
    }

    @Override
    public void doDelete(Request request, Response response) throws IOException {
        if (request.getAction().equals("delete") && request.getUser().getRole().equals("Admin")) {
            response.setStatusCode("OK");
            response.setAction("delete");

            User user = (User) request.getUser();
            Notify notify = (Notify) request.getData();
            notifyService.delete(notify);
            System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xóa thông báo thành công.");

            response.setData(request.getUser());
            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();

        } else {
            System.out.println("SERVER: (" + request.getUser().getRole() + ") " + request.getUser().getName() + " : Không đủ quyền xóa thông báo.");
            Authentication.sendPermission(response);
        }
    }

    @Override
    public void init(Server context, Request request, Response response) {
        notifyService = NotifyService.getInstance();
        this.context = context;
        this.request = request;
        this.response = response;
        this.response.setController("/notify");
    }


}
