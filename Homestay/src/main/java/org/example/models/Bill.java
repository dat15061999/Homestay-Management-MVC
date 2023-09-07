package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.untils.CurrencyFormat;

import java.io.Serializable;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bill implements Serializable {
    private static final long serialVersionUID = -4855084935844290115L;
    private long id;
    private BookingDetail bookingDetail;
    private String status;  //Trang thai: Da thanh toan, chua thanh toan;

    @Override
    public String toString() {
        String formattedId = String.format("%-4s", id);
        String formattedUserName = String.format("%-20s", bookingDetail.getUser().getName());
        String formattedRoomId = String.format("%-10s", bookingDetail.getRoom().getId());
        String formattedRoomPrice = String.format("%-10s", CurrencyFormat.covertPriceToString(bookingDetail.getRoom().getPrice()));
        String formattedRoomChangeFee = String.format("%-10s", CurrencyFormat.covertPriceToString(bookingDetail.getRoomChangeFee()));
        String formattedTimeCheckin = String.format("%-15s", bookingDetail.getTimeCheckin());
        String formattedTimeCheckout = String.format("%-15s", bookingDetail.getTimeCheckout());
        String productInfo = (bookingDetail.getProduct() != null ?
                bookingDetail.getProduct()
                        .stream()
                        .map(product -> " (Name: " + product.getName() +
                                " Quantity: " + product.getQuantity() +
                                " Price: " + CurrencyFormat.covertPriceToString(product.getPrice()) + " )")
                        .collect(Collectors.joining(", ")) :
                "Trong");
        String formattedTotal = String.format("%-10s", CurrencyFormat.covertPriceToString(bookingDetail.getTotal()));
        String formattedStatus = String.format("%-12s", status);

        return String.format("  ID Bill: %-4s | Khách hàng: %-20s | Phòng: %-10s | Giá phòng: %-10s | Phí đổi phòng: %-10s | Thời gian vào: %-15s | Thời gian ra: %-15s | Danh sách dịch vụ sử dụng: %s | Tổng: %-10s | Trạng thái thanh toán: %s",
                formattedId, formattedUserName, formattedRoomId, formattedRoomPrice, formattedRoomChangeFee, formattedTimeCheckin, formattedTimeCheckout, productInfo, formattedTotal, formattedStatus);

    }

}
