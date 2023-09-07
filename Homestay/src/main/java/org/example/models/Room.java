package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.untils.CurrencyFormat;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Serializable {
    private long id;
    private long price;
    private int quantityBeg;
    private String roomClass;
    private String status;         //con trong - bao tri - da dat - dang o

    @Override
    public String toString() {
        String formattedId = String.format("%-10s", id); // Đảm bảo rằng ID luôn có ít nhất 10 ký tự và căn trái
        String formattedPrice = String.format("%-15s", CurrencyFormat.covertPriceToString(price)); // Căn trái cho giá phòng
        String formattedQuantityBeg = String.format("%-10s", quantityBeg); // Căn trái cho số lượng ban đầu
        String formattedRoomClass = String.format("%-20s", roomClass); // Căn trái cho loại phòng
        String formattedStatus = String.format("%-15s", status); // Căn trái cho trạng thái phòng

        // Kết hợp tất cả các giá trị vào chuỗi định dạng
        return String.format("ROOM { ID Room : %s | Price : %s | QuantityBeg : %s | RoomClass : %s | Status : %s }",
                formattedId, formattedPrice, formattedQuantityBeg, formattedRoomClass, formattedStatus);
    }
}
