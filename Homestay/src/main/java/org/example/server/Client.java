package org.example.server;

import org.example.models.*;
import org.example.services.*;
import org.example.untils.GetValue;
import org.example.untils.PrintViews;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;


public class Client {
    UserService userService;
    NotifyService notifyService;
    ProductService productService;
    RoomService roomService;
    BillService billService;
    private final SharedResource sharedResource;
    ObjectInputStream is = null;
    ObjectOutputStream os = null;

    Socket socketOfClient = null;
    Request request;
    Response response;

    public Client() {
        final String serverHost = "192.168.1.142";
        sharedResource = new SharedResource();
        try {
            socketOfClient = new Socket("localhost", 7777);
            os = new ObjectOutputStream(socketOfClient.getOutputStream());
            is = new ObjectInputStream(socketOfClient.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverHost);
            return;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + serverHost);
            return;
        }
        try {
            launcher(is, os);
            os.close();
            is.close();
            socketOfClient.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException | IllegalMonitorStateException e) {
            e.printStackTrace();
        }
    }

    private void launcher(ObjectInputStream is, ObjectOutputStream os) throws ClassNotFoundException, IOException, InterruptedException, IllegalMonitorStateException {
        boolean checkLogin = false;
        do {

            PrintViews.loginView();
            int choice = GetValue.getInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    request = new Request();
                    response = new Response();
                    checkLogin = loginView(is, os);
                    break;
                case 2:
                    request = new Request();
                    response = new Response();
                    addUser(is,os);
                    break;
            }
        } while (!checkLogin);

        Thread readerThread = new ReaderThread();
        readerThread.start();

        boolean checkAction = false;
        do {
            sharedResource.setClientRequested();
            if (request.getUser().getRole().equals("Admin") ||
                    request.getUser().getRole().equals("Cashier")) {
                pageAdmin(is, os);
            } else {
                pageClient(is, os);
            }


            sharedResource.waitReceiveResponse();
            String continueAction = GetValue.getString("Bạn có muốn dừng không Y/N");
            switch (continueAction) {
                case "Y": {
                    checkAction = false;
                    //byeClient(os,is);
                    break;
                }
                case "N": {
                    checkAction = true;
                    break;
                }
            }
        } while (checkAction);

    }

    private void pageClient(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        PrintViews.printClientViewPage();
        int choice = GetValue.getInt("Nhập lựa chọn: ");
        switch (choice) {
            case 1:
                showUser(is, os);
                break;
            case 2:
                serviceClient(is, os);
                break;
            case 3:
                showBillClient(is, os);
                break;
            case 4:
                showNotify(is, os);
                break;
            default:
                pageClient(is, os);
                break;
        }
    }

    private void serviceClient(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        PrintViews.printMenuService();
        int choice = GetValue.getInt("Nhập lựa chọn: ");
        User p = (User) request.getUser();
        billService = BillService.getInstance();
        Bill b = billService.findBill(p, "CHƯA THANH TOÁN");
        switch (choice) {
            case 1:
                confirmNotify(is, os);
                break;
            case 2:
                showProduct(is, os);
                break;
            case 3:
                if (b != null) {
                    callService(is, os);
                } else {
                    pageClient(is, os);
                }
                break;
            case 4:
                notifyClient(is, os);
                break;
            default:
                pageClient(is, os);
                break;
        }
    }

    private void callService(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            Request reqs = (Request) request.clone();
            User user = (User) reqs.getUser();

            String comment = " Cho mình gọi "
                    + GetValue.getString("Nhập dịch vụ sử dụng: ")
                    + " , số lượng: "
                    + GetValue.getString("Nhập số lượng: ") + " đang ở phòng "
                    + GetValue.getString("Đang ở phòng: ") + " .";
            Messenger messenger = new Messenger();
            if (user.getClientIDUser() == null) {
                String nameID = GetValue.getString("Nhập ID người nhận: ");
                messenger.setClientID(nameID);
                messenger.setComment(comment);
            } else {
                messenger.setClientID(user.getClientIDUser());
                messenger.setComment(comment);
            }
            reqs.setData(messenger);
            reqs.setController("/notify");
            reqs.setAction("notify");
            reqs.setMethod("POST");
            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void showBillClient(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            Request req = (Request) request.clone();
            req.setAction("show");
            req.setController("/bill");
            req.setMethod("GET");
            os.writeObject(req);
            os.flush();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void pageAdmin(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        PrintViews.printAdminViewPage();
        int choice = GetValue.getInt("Enter your choice: ");
        switch (choice) {
            case 1:
                PrintViews.printMenuUserView();
                choice = GetValue.getInt("Enter your choice: ");
                switch (choice) {
                    case 1:
                        showUser(is, os);
                        break;
                    case 2:
                        addUser(is, os);
                        break;
                    case 3:
                        updateUser(is, os);
                        break;
                    case 4:
                        deleteUser(is, os);
                        break;
                    default:
                        pageAdmin(is,os);
                        break;
                }
                break;
            case 2:
                showProduct(is, os);
                break;
            case 3:
                showRoom(is, os);
                break;
            case 4:
                PrintViews.printMenuNotifyView();
                choice = GetValue.getInt("Enter your choice: ");
                switch (choice) {
                    case 1:
                        showNotify(is, os);
                        break;
                    case 2:
                        PrintViews.printMenuNotifyChoose();
                        choice = GetValue.getInt("Nhập lựa chọn : ");
                        switch (choice) {
                            case 1:
                                addNotify(is, os);
                                break;
                            case 2:
                                confirmNotify(is, os);
                                break;
                        }
                        break;
                    case 3:
                        updateNotify(is, os);
                        break;
                    case 4:
                        deleteNotify(is, os);
                        break;
                    default:
                        pageAdmin(is,os);
                        break;
                }
                break;
            case 5:
                //Doanhthu
                PrintViews.printMenuDTView();
                choice = GetValue.getInt("Enter your choice: ");
                switch (choice) {
                    case 1:
                        showDTD(is,os);
                        break;
                    case 2:
                        showDTY(is,os);
                        break;
                    default:
                        pageAdmin(is,os);
                        break;
                }
                break;
            case 6:
                PrintViews.printMenuPayView();
                choice = GetValue.getInt("Enter your choice: ");
                switch (choice) {
                    case 1:
                        showBill(is, os);
                        break;
                    case 2:
                        addBill(is, os);
                        break;
                    case 3:
                        payBill(is, os);
                        break;
                    case 4:
                        updateBill(is, os);
                        break;
                    case 5:
                        deleteBill(is, os);
                        break;
                    default:
                        pageAdmin(is,os);
                        break;
                }
                break;
            case 7:
                notifyClient(is, os);
                break;
            default:
                pageAdmin(is, os);
                break;

        }
    }
    private void showDTY(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            Request reqs = (Request) request.clone();

            LocalDate localDate = LocalDate.of(GetValue.getInt("Nhap nam: "),12,1);

            reqs.setData(localDate);
            reqs.setMethod("GET");
            reqs.setController("/bill");
            reqs.setAction("showDTY");

            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void showDTD(ObjectInputStream is, ObjectOutputStream os)throws IOException {
        try {
            Request reqs = (Request) request.clone();

            LocalDate localDate = LocalDate.of(2023,GetValue.getIntWithAbout("Nhập tháng: ",12,1),GetValue.getInt("Nhập ngày: "));

            reqs.setData(localDate);
            reqs.setMethod("GET");
            reqs.setController("/bill");
            reqs.setAction("showDTD");

            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmNotify(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            userService = UserService.getInstance();
            Notify notify = new Notify();

            User u = (User) request.getUser();
            if (u.getRole().equals("Client")) {
                PrintViews.printChooseServiceRoomNotify();
                int choice = GetValue.getInt("Nhập lựa chọn: ");
                switch (choice) {
                    case 1:
                        LocalDate dayIn = GetValue.getDate("Nhập ngày vào");
                        LocalDate dayOut = GetValue.getDateOut(dayIn,"Nhập ngày ra");
                        notify.setName("Thông báo khách hàng " + u.getName() +
                                " muốn đặt " + GetValue.getIntWithAbout("Nhập số phòng: ",5,1) + " " +
                                " phòng " + GetValue.getIntWithAbout("Nhập số người",50,1) +
                                " người " + GetValue.getIntWithAbout("Nhập số giường",2,1) +
                                " giường ,ngày vào: " + dayIn  +
                                " ngày ra: " + dayOut + ".");
                        break;
                    case 2:
                        notify.setName("Khách hàng " + u.getName() + " xác nhận đặt phòng!");
                        break;
                    case 3:
                        notify.setName("Khách hàng " + u.getName() + " hủy đặt phòng "+ GetValue.getString("Nhập phòng hủy: ")+" .");
                        break;
                    default:
                        pageClient(is,os);
                        break;
                }
                List<User> users = new ArrayList<>();
                users.add(u);
                notify.setUser(users);
            } else {
                List<User> user = new ArrayList<>();
                User u1 = userService.find(GetValue.getInt("Nhập id người dùng cần gửi thông báo."));
                user.add(u1);
                notify.setName("Đặt phòng thành công! Phòng của khách hàng " +u1.getName()+ " là phòng: " + GetValue.getString("Nhập số phòng: "));
                notify.setUser(user);

            }
            notify.setTimeStart(LocalDate.now());
            notify.setTimeEnd(LocalDate.now());

            Request reqs = (Request) request.clone();
            reqs.setData(notify);
            reqs.setAction("create");
            reqs.setController("/notify");
            reqs.setMethod("POST");
            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void addBill(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            billService = BillService.getInstance();
            userService = UserService.getInstance();
            roomService = RoomService.getInstance();
            productService = ProductService.getInstance();
            Request reqs = (Request) request.clone();

            Bill bill = new Bill();
            List<User> users = userService.readFile();
            users.stream()
                    .filter(user -> user.getRole().equals("Client"))
                    .forEach(user -> System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Email: " + user.getEmail()));
            User user = userService.find(GetValue.getInt("Nhập id người dùng thêm hóa đơn"));
            Room rooms = roomService.find(GetValue.getInt("Nhập ID phòng"), "Trống");
            List<Product> products = productService.readFile();
            List<Product> products1 = new ArrayList<>();
            products1.add(products.get(0));

            BookingDetail bookingDetail = new BookingDetail();

            bookingDetail.setProduct(products1);
            bookingDetail.getProduct().get(0).setQuantity(2);
            bookingDetail.setUser(user);
            bookingDetail.setRoom(rooms);
            LocalDate dayCheckIn= GetValue.getDate("Nhập ngày ở: ") ;
            LocalDate dayCheckOut= GetValue.getDateOut(dayCheckIn,"Nhập ngày đi: ");
            bookingDetail.setTimeCheckin(dayCheckIn);
            bookingDetail.setTimeCheckout(dayCheckOut);
            bill.setBookingDetail(bookingDetail);

            reqs.setData(bill);
            reqs.setAction("create");
            reqs.setController("/bill");
            reqs.setMethod("POST");
            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void payBill(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            roomService = RoomService.getInstance();
            billService = BillService.getInstance();
            Request reqs = (Request) request.clone();

            Bill bill = billService.findBillByIdWithRetry(GetValue.getInt("Nhập id hóa đơn cần thanh toán."), "CHƯA THANH TOÁN");
            billService.showBill(bill, request.getUser());
            PrintViews.printChoosePay();
            int choice = GetValue.getInt("Nhập lựa chọn: ");
            switch (choice) {
                case 1:
                    bill.setStatus(EStatusBill.PAYED.getName());
                    Room room = bill.getBookingDetail().getRoom();
                    roomService.update(room, "Trống");
                    break;
                case 2:
                    bill.setStatus(EStatusBill.NONE.getName());
                    break;
            }

            reqs.setData(bill);
            reqs.setAction("pay");
            reqs.setController("/bill");
            reqs.setMethod("PUT");
            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteBill(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            billService = BillService.getInstance();
            Request reqs = (Request) request.clone();

            Bill bill = billService.findBillByIdWithRetry(GetValue.getInt("Nhập id hóa đơn cần xóa."), "ĐÃ THANH TOÁN");

            reqs.setData(bill);
            reqs.setAction("delete");
            reqs.setController("/bill");
            reqs.setMethod("DELETE");
            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateBill(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            roomService = RoomService.getInstance();
            billService = BillService.getInstance();
            productService = ProductService.getInstance();

            Request reps = (Request) request.clone();
            Bill bill = billService.findBillByIdWithRetry(GetValue.getLong("Nhập đơn hàng cần sửa."), "CHƯA THANH TOÁN");
            BookingDetail bookingDetail = bill.getBookingDetail();
            PrintViews.printMenuUpdateBill();
            int choice = GetValue.getInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    roomService.update(bookingDetail.getRoom(), "Trống");
                    Room room = roomService.find(GetValue.getInt("Nhập ID phòng"), "Trống", bookingDetail.getRoom());
                    bookingDetail.setRoomChangeFee(bookingDetail.getRoom().getPrice() * bookingDetail.getTime());
                    bookingDetail.setTimeCheckin(GetValue.getDateNow());
                    bookingDetail.setRoom(room);
                    roomService.update(room, "ĐANG Ở");
                    bill.setBookingDetail(bookingDetail);
                    break;
                case 2:
                    Room room1 = bookingDetail.getRoom();
                    roomService.update(room1, "ĐANG Ở");
                    bookingDetail.setRoom(room1);
                    bill.setBookingDetail(bookingDetail);
                    break;
                case 3:
                    productService = ProductService.getInstance();
                    Product product = productService.find(GetValue.getInt("Nhập ID sản phẩm: "));
                    product.setQuantity(GetValue.getLong("Nhập số lượng: "));
                    List<Product> products = new ArrayList<>();
                    products.add(product);
                    if (bookingDetail.getProduct() == null) {
                        bookingDetail.setProduct(products);
                    } else {
                        bookingDetail.getProduct().add(product);
                    }
                    bill.setBookingDetail(bookingDetail);
                    break;
                case 4:
                    bookingDetail.getProduct()
                            .forEach(product1 -> System.out.println("ID : " + product1.getId() + " + " + "Name : " + product1.getName()));
                    Product products1 = productService.find(bookingDetail.getProduct(), GetValue.getInt("Nhập ID sản phẩm thay đổi: "));
                    PrintViews.printChooseChangeProductInBill();
                    choice = GetValue.getInt("Nhập: ");
                    switch (choice) {
                        case 1:
                            Product p = productService.find(products1.getId());
                            products1.setQuantity(GetValue.getDouble("Nhập lại số lượng: ",p.getQuantityInStock(),0));
                            bookingDetail.setProduct(bookingDetail.getProduct()
                                    .stream()
                                    .filter(p1->p1.getId()==products1.getId()).
                                    map(p2->{
                                        p2.setQuantity(products1.getQuantity());
                                        return p2;
                                    }).collect(Collectors.toList()));
                            break;
                        case 2:
                            bookingDetail.getProduct().remove(products1);
                            break;
                    }
                    bill.setBookingDetail(bookingDetail);
                    break;
                case 5:
                    PrintViews.printChooseMenuUpdateDate();
                    choice = GetValue.getInt("Nhập: ");
                    switch (choice) {
                        case 1:
                            bookingDetail.setTimeCheckin(GetValue.getDate("Nhập ngày vào: "));
                            break;
                        case 2:
                            bookingDetail.setTimeCheckout(GetValue.getDateOut(bookingDetail.getTimeCheckin(),"Nhập ngày ra: "));
                            break;
                    }
                    bill.setBookingDetail(bookingDetail);
                    break;
            }
            reps.setData(bill);
            reps.setAction("update");
            reps.setController("/bill");
            reps.setMethod("PUT");
            os.writeObject(reps);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    private void showBill(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            Request reqs = (Request) request.clone();

            reqs.setMethod("GET");
            reqs.setController("/bill");
            reqs.setAction("show");

            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void showRoom(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            Request reqs = (Request) request.clone();

            reqs.setMethod("GET");
            reqs.setController("/room");
            reqs.setAction("show");

            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void notifyClient(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            String name = GetValue.getString("Messenger đến client: ");
            String comment = GetValue.getString("Nhập tin nhắn đến client: ");
            Messenger messenger = new Messenger(name, comment);

            Request reqs = (Request) request.clone();

            reqs.setData(messenger);
            reqs.setController("/notify");
            reqs.setAction("notify");
            reqs.setMethod("POST");
            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    private void showProduct(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            Request reqs = (Request) request.clone();

            reqs.setAction("show");
            reqs.setController("/product");
            reqs.setMethod("GET");
            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteNotify(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            notifyService = NotifyService.getInstance();
            Request reqs = (Request) request.clone();

            Notify notify = notifyService.find(GetValue.getInt("Nhập id thông báo cần xóa."));

            reqs.setData(notify);
            reqs.setAction("delete");
            reqs.setController("/notify");
            reqs.setMethod("DELETE");
            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateNotify(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            Request reps = (Request) request.clone();

            Notify notify = new Notify();
            PrintViews.printChooseEditNotify();
            int choice = GetValue.getInt("Enter your choice : ");
            switch (choice) {
                case 1:
                    notify.setName(GetValue.getString("Nhập nội dung thông báo."));
                    break;
                case 2:
                    notify.setComment(GetValue.generateRandomString(GetValue.getInt("Nhập mã số lượng chữ số giảm giá")));
                    notify.setStatus("CHSD");
                    break;
                case 3:
                    LocalDate dayStart= GetValue.chooseTime("Chọn thời gian bắt đầu sự kiện.") ;
                    LocalDate dayEnd= GetValue.getDateOut(dayStart,"Nhập thời gian kết thúc sự kiện.");
                    notify.setTimeStart(dayStart);
                    notify.setTimeEnd(dayEnd);
                    break;
                case 4:
                    notify.setPriceDiscount(GetValue.getFloat("Nhập chiết khấu"));
                    break;
                default:
                    updateNotify(is, os);
                    break;

            }
            reps.setData(notify);
            reps.setAction("update");
            reps.setController("/notify");
            reps.setMethod("PUT");
            os.writeObject(reps);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void addNotify(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            userService = UserService.getInstance();
            Notify notify = new Notify();
            notify.setName(GetValue.getString("Nhập nội dung thông báo"));
            notify.setComment(GetValue.generateRandomString(8));
            notify.setStatus("CHSD");
            LocalDate dayStart= GetValue.chooseTime("Chọn thời gian bắt đầu sự kiện.") ;
            LocalDate dayEnd= GetValue.getDateOut(dayStart,"Nhập thời gian kết thúc sự kiện.");
            notify.setTimeStart(dayStart);
            notify.setTimeEnd(dayEnd);
            //notify.setPriceDiscount(GetValue.getFloat("Nhập chiết khấu"));

            List<User> userList = userService.readFile();
            List<User> users = new ArrayList<>();
            PrintViews.printChooseAddNotifyUser();
            int choice = GetValue.getInt("Nhập lựa chọn :");
            switch (choice) {
                case 1:
                    users = userList.stream().filter(e -> e.getRole().equals("Client")).collect(Collectors.toList());
                    break;
                case 2:
                    users.add(userService.find(GetValue.getInt("Nhập id người dùng cần gửi thông báo.")));
                    break;
            }
            notify.setUser(users);

            Request reqs = (Request) request.clone();
            reqs.setData(notify);
            reqs.setAction("create");
            reqs.setController("/notify");
            reqs.setMethod("POST");
            os.writeObject(reqs);
            os.flush();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void showNotify(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            Request req = (Request) request.clone();
            req.setAction("show");
            req.setController("/notify");
            req.setMethod("GET");
            os.writeObject(req);
            os.flush();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        userService = UserService.getInstance();
        User user = userService.find(userService.checkIdUser(GetValue.getInt("Nhập id người dùng muốn xóa.")));

        try {
            Request req = (Request) request.clone();

            req.setData(user);
            req.setAction("delete");
            req.setController("/user");
            req.setMethod("DELETE");
            os.writeObject(req);
            os.flush();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void updateUser(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        userService = UserService.getInstance();
        User user = userService.update(userService.checkIdUser(GetValue.getInt("Nhập id người dùng cần sửa.")));

        try {
            Request req = (Request) request.clone();

            req.setData(user);
            req.setAction("update");
            req.setController("/user");
            req.setMethod("PUT");
            os.writeObject(req);
            os.flush();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void addUser(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        userService = UserService.getInstance();
        User user = new User();
        user.setName(GetValue.getString("Nhap ten khach hang"));
        user.setUsername(userService.checkUsername(GetValue.getString("Nhap username khach hang")));
        user.setPassword(GetValue.getString("Nhap password khach hang"));
        user.setGender(GetValue.getGender("Chon gioi tinh"));
        user.setPhone(GetValue.getPhone("Nhap so dien thoai khach hang"));
        user.setEmail(GetValue.getEmail("Nhap email khach hang"));
        user.setAddress(GetValue.getString("Nhap dia chi khach hang"));
        user.setRole(ERole.CLIENT.getName());
        try {
            Request req;
            if (request.getUser() != null) {
                req = (Request) request.clone();
            } else {
                req = new Request();
            }
            req.setData(user);
            req.setAction("create");
            req.setController("/user");
            req.setMethod("POST");
            os.writeObject(req);
            os.flush();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void showUser(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        try {
            Request req = (Request) request.clone();
            req.setAction("show");
            req.setController("/user");
            req.setMethod("GET");
            os.writeObject(req);
            os.flush();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private boolean loginView(ObjectInputStream is, ObjectOutputStream os) throws IOException {
        System.out.println("Dang nhap");
        String username = GetValue.getString("Nhap username");
        String password = GetValue.getString("Nhap password");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        request.setAction("login");
        request.setController("/login");
        request.setMethod("POST");
        request.setData(user);
        os.writeObject(request);
        os.flush();

        try {
            Object obj = is.readObject();
            if (obj != null) {
                Response resp = (Response) obj;
                if (resp.getStatusCode().equals("200")) {
                    request.setUser((User) resp.getData());
                    return true;
                }
                if (resp.getStatusCode().equals("401")) {
                    System.out.println("Tài khoản hoặc mật khẩu không chính xác.");
                    return false;
                }
            }
            return false;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void trans(Response resp, Object o) {
        if (resp.getAction().equals("show")) {
            List<?> dataList = (List<?>) resp.getData();
            dataList.forEach(System.out::println);
        }
        if (resp.getAction().equals("create")) {
            if (o.equals("User")) {
                System.out.println("Ngưởi dùng đã được thêm thành công. ");
            } else if (o.equals("Notify")) {
                System.out.println("Thông báo đã được thêm thành công. ");
            } else if (o.equals("Bill")) {
                System.out.println("Hóa đơn đã được thêm thành công. ");
            }
        }
        if (resp.getAction().equals("update")) {
            if (o.equals("User")) {
                System.out.println("Ngưởi dùng đã được chỉnh sửa thành công. ");
            } else if (o.equals("Notify")) {
                System.out.println("Thông báo đã được chỉnh sửa thành công. ");
            } else if (o.equals("Bill")) {
                System.out.println("Hóa đơn đã được sửa thành công. ");
            }
        }
        if (resp.getAction().equals("delete")) {
            if (o.equals("User")) {
                System.out.println("Ngưởi dùng đã được xóa thành công.");
            } else if (o.equals("Notify")) {
                System.out.println("Thông báo đã được xóa thành công.");
            } else if (o.equals("Bill")) {
                System.out.println("Hóa đơn đã được xóa thành công.");
            }
        }
        if (resp.getAction().equals("from")) {
            Messenger messenger = (Messenger) resp.getData();
            System.out.println("(" + messenger.getUser().getRole() + " : " + messenger.getClientID() + ") " + messenger.getUser().getName() + " : " + messenger.getComment());
        } else if (resp.getAction().equals("to")) {
            System.out.println("Gửi thông tin thành công.");
        } else if (resp.getAction().equals("pay")) {
            System.out.println("Thanh toán thành công");
        } else if (resp.getAction().equals("showUser")) {
            User user = (User) resp.getData();
            userService = UserService.getInstance();
            userService.showUser(user);
        } else if (resp.getAction().equals("showNotify")) {
            notifyService = NotifyService.getInstance();
            List<Notify> notifyList = (List<Notify>) resp.getData();
            notifyService.showNotify(notifyList);
        } else if (resp.getAction().equals("showBillClient")) {
            billService = BillService.getInstance();
            List<Bill> bills = (List<Bill>) resp.getData();
            billService.showBill(bills, request.getUser());
        } else if (resp.getAction().equals("showDTD")) {
            billService = BillService.getInstance();
            LocalDate date = (LocalDate) resp.getData();
            billService.getRevenueForSpecificDay(date);
        }   else if (resp.getAction().equals("showDTY")) {
            billService = BillService.getInstance();
            LocalDate date = (LocalDate) resp.getData();
            billService.getRevenueAllYear(date);
        }
    }

    class ReaderThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Object objRes = is.readObject();
                    if (objRes != null) {
                        Response resp = (Response) objRes;

                        System.out.println("NHẬN THÔNG TIN TỪ SERVER: ");
                        switch (resp.getController()) {
                            case "/user":
                            case "/notify":
                            case "/product":
                            case "/room":
                            case "/bill": {
                                trans(resp, resp.getData().getClass().getSimpleName());
                                break;
                            }
                            case "/authentication": {
                                if (resp.getAction().equals("authentication")) {
                                    System.out.println("Cashier vượt quá thẩm quyền truy câp.");
                                }
                                break;
                            }
                            case "/login": {
                                if (resp.getAction().equals("login") && resp.getStatusCode().equals("200")) {
                                    System.out.println("Đăng nhập thành công");
                                }
                                if (resp.getAction().equals("login") && resp.getStatusCode().equals("401")) {
                                    System.out.println("Tài khoản hoặc mật khẩu không chính xác");
                                    System.out.println("Mời nhập lại");
                                }
                                break;
                            }


                        }
                    }
                    sharedResource.setReceiveResponse();
                }
            } catch (IOException e) {
                if (socketOfClient.isClosed()) {
                    System.out.println("Disconnect...");
                } else {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    static class SharedResource {
        private boolean WAIT_RECEIVER_RESP = false;

        public synchronized void waitReceiveResponse() throws InterruptedException {
            while (!WAIT_RECEIVER_RESP) {
                wait(); // Chờ đến khi có thông báo
            }
        }

        public synchronized void setReceiveResponse() {
            WAIT_RECEIVER_RESP = true;
            notify(); // Báo thức một luồng đang chờ
        }

        public synchronized void setClientRequested() {
            WAIT_RECEIVER_RESP = false;
            notify(); // Báo thức một luồng đang chờ
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
