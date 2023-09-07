package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import static org.example.untils.CurrencyFormat.covertPriceToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 3L;
    private int id;
    private String name;
    private double quantityInStock;
    private double quantity = 1.0;
    private int price;
    private String status;
    private String category;

    public Product(int id, String name, double quantityInStock, int price, String category) {
        this.id = id;
        this.name = name;
        this.quantityInStock = quantityInStock;
        this.price = price;
        this.category = category;
    }

    @Override
    public String toString() {
        String format = "| ID = %-5s| Tên sản phẩm : %-15s| Kho còn : %-7s| Giá : %-10s| Trạng thái : %-10s| Loại : %-10s |";
        return String.format(format, id, name, quantityInStock, covertPriceToString(price), getStatus(), category);
    }
    public String getStatus() {
        if (getQuantityInStock() > 15) {
            return "Còn hàng";
        } else if (getQuantityInStock() > 10){
            return "Sắp hết hàng";
        }
        return "Hết hàng";
    }
    public double getTotal() {
        return this.price * this.quantity;
    }
}
