package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.untils.GetValue;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BookingDetail implements Serializable {
    private static final long serialVersionUID = 8481691311194289444L;
    private User user;
    private LocalDate timeCheckin;
    private LocalDate timeCheckout;
    private Room room;
    private List<Product> product;
    private double roomChangeFee;
    private double total;  //prepay + service.getPrice()

    public BookingDetail(User user, LocalDate timeCheckin, LocalDate timeCheckout, Room room, List<Product> product, double total) {
        this.user = user;
        this.timeCheckin = timeCheckin;
        this.timeCheckout = timeCheckout;
        this.room = room;
        this.product = product;
        this.total = total;
    }

    @Override
    public String toString() {
        return "BookingDetail{" +
                ", user=" + user +
                ", timeCheckin=" + timeCheckin +
                ", timeCheckout=" + timeCheckout +
                ", room=" + room +
                ", product=" + product +
                ", total=" + total +
                '}';
    }

    public double getTime() {
        LocalDate timeOut = GetValue.getDateNow();
        Period period = Period.between(timeCheckin,timeOut);
        if (period.getDays() < 1) {
            return this.room.getPrice() * 0.2;
        }
        return period.getDays();
    }
    public double getTimeReal() {
        Period period = Period.between(timeCheckin,timeCheckout);
        return period.getDays();
    }
    public double getTotal() {
        double roomPrice = this.room.getPrice()*getTimeReal();
        double productPrice = (getProduct() != null ) ?
                getProduct().stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum()
                : 0.0;
        return roomPrice + productPrice + getRoomChangeFee() ;
    }
}
