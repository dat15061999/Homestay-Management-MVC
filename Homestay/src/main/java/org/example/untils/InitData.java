package org.example.untils;


import org.example.models.*;
import org.example.services.ProductService;
import org.example.services.RoomService;
import org.example.services.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InitData {
    static UserService userService;
    static RoomService roomService;
    static ProductService productService;
    public static void initUser() {
        Bank vietcombank = new Bank("VIETCOMBANK","1232-4321-5555","312");
        Bank teckcombank = new Bank("TECHCOMBANK","1232-4321-2222","901");
        Bank mbbank = new Bank("MBBANK","1232-4321-3333","402");
        Bank vietcombank2 = new Bank("VIETCOMBANK","1232-4321-2630","111");


        List<User> userList = new ArrayList<>();
        userList.add(new User(1,"Linh Nguyen","linh123","123456","FeMale","84-0345677966","linh123@gmail.com","28 Nguyen Tri Phuong","Admin"));
        userList.add(new User(2,"Nam Pham","nam123","123456","Male","84-0345677978","nam23@gmail.com","28 Nguyen Tri Phuong","Cashier"));
        userList.add(new User(3,"Khanh Anh","khanh123","123456","FeMale","84-0345677999","khanh123@gmail.com","28 Nguyen Tri Phuong","Client",vietcombank));
        userList.add(new User(4,"Doan Vo","doan123","123456","FeMale","84-0345677999","doan123@gmail.com","28 Nguyen Tri Phuong","Client",vietcombank2));
        userList.add(new User(5,"Dung Le","dung123","123456","FeMale","84-0345677999","dung123@gmail.com","28 Nguyen Tri Phuong","Client",mbbank));
        userList.add(new User(6,"Yen Hai","yen123","123456","FeMale","84-0345677999","yen123@gmail.com","28 Nguyen Tri Phuong","Client",teckcombank));
        SerializationUtil.serialize(userList, "users.txt");
    }
    public static void innitNotify() {
        userService = new UserService();
        List<User> user = userService.readFile();
        List<User> list = new ArrayList<>();
        list.add(user.get(3));
        list.add(user.get(4));
        list.add(user.get(5));
        list.add(user.get(2));
        List<Notify> notifyList = new ArrayList<>();
        notifyList.add(new Notify(1,"Giam gia 20% ngay le tinh nhan cho cac khach hang la cac cap doi",list, GetValue.generateRandomString(8),"CHSD",0.2f, LocalDate.of(2023, 8, 23),GetValue.plusMonth(LocalDate.of(2023, 8, 23),1)));
        notifyList.add(new Notify(2,"Ngày 08-09-2023, sẽ diễn ra buổi tập huấn phòng chống cháy nổ tại khách sạn.",list, GetValue.generateRandomString(8),"CHSD",0.0f,LocalDate.of(2023, 9, 8),LocalDate.of(2023, 9, 8)));


        SerializationUtil.serialize(notifyList,"notify.txt");
    }

    public static void innitProduct() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1024,"Nước suối",100,5000, ECategory.DRINK.getName()));
        products.add(new Product(1025,"Nước ngọt",100,5000, ECategory.DRINK.getName()));
        products.add(new Product(1026,"Phở",100,25000, ECategory.EAT.getName()));
        products.add(new Product(1027,"Trái cây",100,50000, ECategory.EAT.getName()));
        products.add(new Product(1028,"Giặt sấy",100,30000, ECategory.SERVICE.getName()));
        products.add(new Product(1029,"Đấm lưng",100,300000, ECategory.SERVICE.getName()));

        SerializationUtil.serialize(products,"products.txt");
    }
    public static void innitRoom() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(101,200000,2,EType.CLASS.getName(),"Trống"));
        rooms.add(new Room(102,200000,2,EType.CLASS.getName(),"Trống"));
        rooms.add(new Room(201,200000,2,EType.CLASS.getName(),"Trống"));
        rooms.add(new Room(202,200000,2,EType.CLASS.getName(),"Trống"));
        rooms.add(new Room(301,250000,2,EType.CLASS.getName(),"Trống"));
        rooms.add(new Room(302,300000,2,EType.CLASS.getName(),"Trống"));
        rooms.add(new Room(401,800000,1,EType.VIP.getName(),"Trống"));
        rooms.add(new Room(402,1000000,2,EType.VIP.getName(),"Trống"));
        rooms.add(new Room(403,800000,1,EType.VIP.getName(),"Trống"));
        rooms.add(new Room(404,800000,1,EType.VIP.getName(),"Trống"));

        SerializationUtil.serialize(rooms,"rooms.txt");
    }
    public static void innitBill(){
        userService = new UserService();
        roomService = new RoomService();
        productService = new ProductService();
        List<Product> products =  productService.readFile();
        List<Product> products1 = new ArrayList<>();
        products1.add(products.get(0));
        products1.add(products.get(2));
        List<Product> products3= new ArrayList<>();
        products3.add(products.get(0));


        List<Product> products2 = new ArrayList<>();
        products2.add(products.get(3));
        products2.add(products.get(5));



        List<User> user = userService.readFile();
        List<Room> room = roomService.readFile();

        BookingDetail list1 = new BookingDetail(user.get(2),LocalDate.of(2023,6,15),LocalDate.of(2023,6,16),room.get(6),products1,830000);
        BookingDetail list2 = new BookingDetail(user.get(2),LocalDate.of(2023,7,15),LocalDate.of(2023,7,18),room.get(6),products2,2750000);
        BookingDetail list3 = new BookingDetail(user.get(2),LocalDate.of(2023,8,10),LocalDate.of(2023,8,15),room.get(6),products2,4750000);
        BookingDetail list4 = new BookingDetail(user.get(2),LocalDate.of(2023,9,5),LocalDate.of(2023,9,10),room.get(6),products3,4010000);
        List<Bill> billList = new ArrayList<>();
        billList.add(new Bill(1,list1,EStatusBill.PAYED.getName()));
        billList.add(new Bill(2,list2,EStatusBill.PAYED.getName()));
        billList.add(new Bill(3,list3,EStatusBill.PAYED.getName()));
        billList.add(new Bill(4,list4,EStatusBill.NONE.getName()));

        SerializationUtil.serialize(billList,"bills.txt");
    }

    public static void main(String[] args) {
        //initUser();
        innitNotify();
        innitProduct();
        //innitRoom();
        innitBill();

    }
}
