package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Notify implements Serializable {
    private static final long serialVersionUID = 2L;
    private int id;
    private String name;      // tên quảng cáo
    private List<User> user;  // người được nhận
    private String comment;   // mã giảm giá
    private String status;
    private double priceDiscount;
    private LocalDate timeStart ;
    private LocalDate timeEnd ;

    @Override
    public String toString() {
//        String users = user.stream()
//                .map(User::getName)
//                .collect(Collectors.joining(", "));
//
//        return String.format("| ID Notify: %2d |  Tên thông báo: %110s | Khách hàng: %50s | Ngày nhận: %10s |", id, name, users, timeStart);
        String formattedId = String.format("%-3s", id); // Đảm bảo rằng ID luôn có 3 ký tự và căn trái
        String formattedName = String.format("%-110s", name); // Chiều rộng cố định cho Tên thông báo (100 ký tự) và căn trái
        String formattedUsers = String.format("%-50s", user.stream().map(User::getName).collect(Collectors.joining(", "))); // Chiều rộng cố định cho danh sách khách hàng (100 ký tự) và căn trái
        String formattedTimeStart = String.format("%s", timeStart); // Không cần định dạng cho ngày nhận

        // Đảm bảo căn trái cho các cột bằng cách thêm dấu cách vào sau chuỗi
        return String.format("| ID Notify: %-3s | Tên thông báo: %-102s | Khách hàng: %-50s | Ngày nhận: %s |", formattedId, formattedName, formattedUsers, formattedTimeStart);
    }

    public String getStatus() {
        LocalDate localDate = LocalDate.now();
        Period period = Period.between(localDate,timeEnd);
        if (period.getDays() == 0) {
            return "HHSD";
        }
        return status;
    }
}
