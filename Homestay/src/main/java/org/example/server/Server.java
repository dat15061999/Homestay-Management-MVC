package org.example.server;

import org.example.models.Messenger;
import org.example.models.User;
import org.example.services.UserService;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    UserService userService;
    private final List<ServiceThread> serviceThreads;
    public Server(){
        serviceThreads = new ArrayList<>();
        ServerSocket listener = null;
        System.out.println("Server is waiting to accept user...");
        int clientNumber = 0;
        try {
            listener = new ServerSocket(7777);
            //InetAddress inetAddress = InetAddress.getByName("192.168.1.142");
            //listener = new ServerSocket(7777, 50, inetAddress);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        try{
            while (true){
                Socket socketOfServer = listener.accept();

                String clientId = System.currentTimeMillis() %10000 + "";
                System.out.println("Có client kết nối tới: " + clientId);
                serviceThreads.add(new ServiceThread(socketOfServer,clientId,this));
                serviceThreads.get(serviceThreads.size()-1).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void sendTo(String clientID, Messenger message) {
        ServiceThread sThread = serviceThreads.stream()
                .filter(serviceThread -> serviceThread.getClientId().equals(clientID))
                .findFirst().get();

        System.out.println("Thực hiện gửi thông tin cho client: " + sThread.getClientId());

        userService  = UserService.getInstance();
        User user = message.getUser();
        if (user.getRole().equals("Client")) {
            user.setClientIDUser(clientID);
            userService.update(user);
            message.setUser(user);
        }


        sThread.sendMessage(message);


    }
    public static void main(String[] args) {
        Server server = new Server();

    }
}
