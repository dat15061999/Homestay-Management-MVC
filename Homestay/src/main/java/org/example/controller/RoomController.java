package org.example.controller;

import org.example.models.*;
import org.example.server.Server;
import org.example.services.ProductService;
import org.example.services.RoomService;

import java.io.IOException;
import java.util.List;

public class RoomController extends Controller{
    RoomService roomService;
    private static RoomController INSTANCE_PRODUCT_CONTROLLER;

    public static RoomController getInstance() {
        if (INSTANCE_PRODUCT_CONTROLLER == null) {
            INSTANCE_PRODUCT_CONTROLLER = new RoomController();
        }
        return INSTANCE_PRODUCT_CONTROLLER;
    }
    @Override
    public void doGet(Request request, Response response) throws IOException {
        if (request.getAction().equals("show")) {
            response.setStatusCode("OK");
            response.setAction("show");

            User user = request.getUser();
            System.out.println("SERVER: ("+user.getRole()+") "+user.getName()+" : Xem danh sách phòng.");
            List<Room> roomList = List.copyOf(roomService.readFile());
            response.setData(roomList);
            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        }
    }

    @Override
    public void doPost(Request request, Response response) throws IOException {

    }

    @Override
    public void doPut(Request request, Response response) throws IOException {

    }

    @Override
    public void doDelete(Request request, Response response) throws IOException {

    }

    @Override
    public void init(Server context, Request request, Response response) {
        roomService = RoomService.getInstance();
        this.context = context;
        this.request = request;
        this.response = response;
        this.response.setController("/room");
    }
}
