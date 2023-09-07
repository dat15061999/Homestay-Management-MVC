package org.example.server;

import lombok.Getter;
import lombok.Setter;
import org.example.controller.*;


import org.example.models.Messenger;
import org.example.models.Request;
import org.example.models.Response;


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

@Getter
@Setter
public class ServiceThread extends Thread {
    private Server context;
    private Scanner scanner = new Scanner(System.in);

    private String clientId;
    private Socket socketOfServer;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    Controller controller;

    public ServiceThread(Socket socketOfServer, String clientId, Server context) {
        this.clientId = clientId;
        this.socketOfServer = socketOfServer;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            is = new ObjectInputStream(socketOfServer.getInputStream());
            os = new ObjectOutputStream(socketOfServer.getOutputStream());

            if (!socketOfServer.isClosed()) {
                while (true) {
                    Object obj = is.readObject();
                    if (obj != null) {
                        Request request = (Request) obj;
                        Response response = new Response();
                        response.setObjectOutputStream(os);

                        //System.out.println("Request: " + request);
                        if (request.getController().contains("/product")) {
                            controller = ProductController.getInstance();
                        }
                        if (request.getController().contains("/room")) {
                            controller = RoomController.getInstance();
                        }
                        if (request.getController().contains("/user")) {
                            controller = UserController.getInstance();
                        }
                        if (request.getController().contains("/notify")) {
                            controller = NotifyController.getInstance();
                        }
                        if (request.getController().contains("/login")) {
                            controller = AuthController.getInstance();
                        }
                        if (request.getController().contains("/bill")) {
                            controller = BillController.getInstance();
                        }
                        controller.init(context, request, response);
                        switch (request.getMethod()) {
                            case "GET":
                                controller.doGet(request, response);
                                break;
                            case "POST":
                                controller.doPost(request, response);
                                break;
                            case "PUT":
                                controller.doPut(request, response);
                                break;
                            case "DELETE":
                                controller.doDelete(request, response);
                                break;
                        }
                    }
                }
            }
        } catch (EOFException e) {
            System.out.println("SERVER: Disconnect...");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Messenger message) {
        try {
            //public Response(String method, String statusCode, String action, String controller, Object data) {
            Response response = new Response(null, "OK", "from", "/notify", message);
            os.writeObject(response);
            os.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
