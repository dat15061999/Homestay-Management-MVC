package org.example.untils;

public class PrintViews {
    public static void printChooseEditUser() {
        System.out.println("Khach hang can thay doi: ");
        System.out.println("Nhập 1. Thông tin tai khoan");
        System.out.println("Nhập 2. Thông tin khach hang");
    }
    public static void printChooseAddNotifyUser() {
        System.out.println("Thông báo đến: ");
        System.out.println("Nhập 1. Tất cả người dùng");
        System.out.println("Nhập 2. Một người dùng");

    }
    public static void printChooseChangeProductInBill() {
        System.out.println("Sản phẩm: ");
        System.out.println("Nhập 1. Điều chỉnh số lượng");
        System.out.println("Nhập 2. Xóa sản phẩm");

    }

    public static void printChooseEditNotify() {
        System.out.println("Thông báo cần thay đổi");
        System.out.println("Nhập 1. Nội dung thông báo.");
        System.out.println("Nhập 2. Mã giảm giá.");
        System.out.println("Nhập 3. Ngày giờ kết thúc sự kiện.");
        System.out.println("Nhập 4. Chiết khấu.");
    }
    public static void loginView() {
        System.out.println("ĐĂNG NHẬP ĐĂNG KÍ");
        System.out.println("Nhập 1. Login");
        System.out.println("Nhập 2. Register");
    }
    public static void printAdminViewPage() {
        System.out.println("MENU chương trình");
        System.out.println("Nhập 1. Quản lý người dùng.");
        System.out.println("Nhập 2. Xem sản phẩm.");
        System.out.println("Nhập 3. Xem phòng.");
        System.out.println("Nhập 4. Quản lý thông báo.");
        System.out.println("Nhập 5. Doanh thu.");
        System.out.println("Nhập 6. Trang thanh toán.");
        System.out.println("Nhập 7: Chat.");

    }
    public static void printClientViewPage() {
        System.out.println("MENU chương trình");
        System.out.println("Nhập 1. Xem thông tin cá nhân.");
        System.out.println("Nhập 2. Dịch vụ.");
        System.out.println("Nhập 3. Thanh toán.");
        System.out.println("Nhập 4. Trang thông báo.");
    }
    public static void printMenuUserView(){
        System.out.println("Trang quản lý người dùng");
        System.out.println("            Nhập 1: Xem");                  // GET|product|
        System.out.println("            Nhập 2: Thêm");                 // POST|product|create
        System.out.println("            Nhập 3: Sửa");                  // PUT|product|create
        System.out.println("            Nhập 4: Xóa");
    }
    public static void printMenuNotifyView(){
        System.out.println("Trang quản lý thông báo");
        System.out.println("           Nhập 1: Xem");                  // GET|product|
        System.out.println("           Nhập 2: Thêm");                 // POST|product|create
        System.out.println("           Nhập 3: Sửa");                  // PUT|product|create
        System.out.println("           Nhập 4: Xóa");
    }
    public static void printMenuNotifyChoose(){
        System.out.println("Thêm thông báo");
        System.out.println("Nhập 1: Thêm thông báo");                  // GET|product|
        System.out.println("Nhập 2: Xác nhận đặt phòng");                // POST|product|create

    }
    public static void printMenuDTView(){
        System.out.println("Trang quản lý Doanh thu (ADMIN)");
        System.out.println("Nhập 1: Xem doanh thu ngày");                  // PUT|product|create
        System.out.println("Nhập 2: Xem doanh thu năm");                 // POST|product|create

    }
    public static void printMenuPayView(){
        System.out.println("Trang quản lý thanh toán");
        System.out.println("Nhập 1: Xem danh sách hóa đơn.");                  // GET|product|
        System.out.println("Nhập 2: Thêm hóa đơn.");
        System.out.println("Nhập 3: Thanh toán hóa đơn.");
        System.out.println("Nhập 4: Sửa hóa đơn.");
        System.out.println("Nhập 5: Xóa hóa đơn. (ADMIN)");
    }

    public static void printMenuUpdateBill() {
        System.out.println("Trang chỉnh sửa hóa đơn");
        System.out.println("Nhập 1: Đổi phòng.");                              // GET|product|
        System.out.println("Nhập 2: Xác nhận đặt phòng.");                              // GET|product|
        System.out.println("Nhập 3: Thêm dịch vụ, sản phẩm.");
        System.out.println("Nhập 4: Cập nhật sản phẩm.");
        System.out.println("Nhập 5: Cập nhật ngày ở, ngày đi.");
    }

    public static void printChoosePay() {
        System.out.println("Xác nhận thanh toán: ");
        System.out.println("Nhập 1: Xác nhận.");                              // GET|product|
        System.out.println("Nhập 2: Chưa xác nhận.");
    }

    public static void printMenuService() {
        System.out.println("Trang dịch vụ: ");
        System.out.println("Nhập 1: Đặt phòng và Xác nhận đặt phòng.");
        System.out.println("Nhập 2: Xem danh sách sản phẩm.");
        System.out.println("Nhập 3: Gọi dịch vụ. (Liên hệ trước tránh lỗi)");
        System.out.println("Nhập 4: Liên hệ và hỗ trợ.");
    }

    public static void printChooseServiceRoomNotify() {
        System.out.println("Dịch vụ phòng: ");
        System.out.println("Nhập 1: Đặt phòng.");                              // GET|product|
        System.out.println("Nhập 2: Xác nhận đặt phòng.");
        System.out.println("Nhập 3: HỦy đặt phòng.");
    }

    public static void printChooseMenuUpdateDate() {
        System.out.println("Chỉnh sữa thời gian vào ở: ");
        System.out.println("Nhập 1: TIME CHECK IN.");                              // GET|product|
        System.out.println("Nhập 2: TIME CHECH OUT.");
    }
}
