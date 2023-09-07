package org.example.controller;

import org.example.models.*;
import org.example.server.Server;
import org.example.services.BillService;
import org.example.services.ProductService;
import org.example.services.RoomService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class BillController extends Controller {
    BillService billService;
    RoomService roomService;
    ProductService productService;

    private static BillController INSTANCE_BILL_CONTROLLER;

    public static BillController getInstance() {
        if (INSTANCE_BILL_CONTROLLER == null) {
            INSTANCE_BILL_CONTROLLER = new BillController();
        }
        return INSTANCE_BILL_CONTROLLER;
    }

    @Override
    public void doGet(Request request, Response response) throws IOException  {
        if (request.getAction().equals("show")) {
            response.setStatusCode("OK");

            List<Bill> billList = List.copyOf(billService.readFile());
            User user = request.getUser();
            if (user.getRole().equals("Client")) {
                List<Bill> bill = billService.findBillByIdWithRetry(user);
                if (bill.size() == 0) {
                    response.setData(user);
                }
                response.setAction("showBillClient");
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xem danh sách thanh toán.");
                response.setData(billList);

            } else {
                response.setAction("show");
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xem danh sách đơn hàng.");
                response.setData(billList);
            }

            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        }
        if(request.getAction().equals("showDTD")) {
            response.setStatusCode("OK");
            LocalDate localDate = (LocalDate) request.getData();

            User user = request.getUser();
            response.setAction("showDTD");
            System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xem doanh thu theo ngày.");
            response.setData(localDate);

            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        }
        if(request.getAction().equals("showDTY")) {
            response.setStatusCode("OK");
            LocalDate localDate = (LocalDate) request.getData();

            User user = request.getUser();
            response.setAction("showDTY");
            System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Xem doanh thu theo năm.");
            response.setData(localDate);

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
            Bill bill = (Bill) request.getData();
            Room rooms = bill.getBookingDetail().getRoom();
            List<Product> products = bill.getBookingDetail().getProduct();
            //luu lai

            productService.update(products);
            roomService.update(rooms);
            bill = billService.create(bill);
            System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Thêm hóa đơn thanh toán cho khách " + bill.getBookingDetail().getUser().getName() + " thành công.");

            response.setData(bill);
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
            Bill bill = (Bill) request.getData();

            billService.update(bill);
            if (user.getRole().equals("Admin") || user.getRole().equals("Cashier")) {
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Sửa hóa đơn thanh toán cho người dùng " + bill.getBookingDetail().getUser().getName() + " thành công.");
            } else {
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Thêm dịch vụ thành công.");
            }
            response.setData(bill);
            response.getObjectOutputStream().writeObject(response);
            response.getObjectOutputStream().flush();
        }else if (request.getAction().equals("pay")) {
            response.setStatusCode("OK");
            response.setAction("pay");

            User user = request.getUser();
            Bill bill = (Bill) request.getData();

            productService.update(bill.getBookingDetail().getProduct());
            billService.update(bill);

            if (user.getRole().equals("Admin") || user.getRole().equals("Cashier") ) {
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Thanh toán cho người dùng " + bill.getBookingDetail().getUser().getName() + " thành công.");
            } else {
                System.out.println("SERVER: (" + user.getRole() + ") " + user.getName() + " : Thanh toán thành công.");
            }

            response.setData(bill);
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
            Bill p = (Bill) request.getData();
            if (p != null) {
                billService.delete(p);
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
        productService = ProductService.getInstance();
        roomService = RoomService.getInstance();
        billService = BillService.getInstance();
        this.request = request;
        this.context = context;
        this.response = response;
        this.response.setController("/bill");
    }
}
