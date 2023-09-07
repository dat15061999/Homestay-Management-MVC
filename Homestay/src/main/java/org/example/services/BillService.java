package org.example.services;

import org.example.models.Bank;
import org.example.models.Bill;
import org.example.models.Product;
import org.example.models.User;

import org.example.untils.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.untils.CurrencyFormat.covertPriceToString;


public class BillService implements CRUD<Bill> {
    private List<Bill> billList;
    private static long nextIdUBill;

    private final String fileName = "bills.txt";

    public BillService() {
        billList = (List<Bill>) SerializationUtil.deserialize(fileName);
        if (billList == null) {
            billList = new ArrayList<>();
        }
        nextIdUBill = AppUtils.findNext2(billList.stream().map(Bill::getId).collect(Collectors.toList()));
    }

    private static BillService INSTANCE_BILL_SERVICE;

    public static BillService getInstance() {
        if (INSTANCE_BILL_SERVICE == null) {
            INSTANCE_BILL_SERVICE = new BillService();
        }
        return INSTANCE_BILL_SERVICE;
    }

    @Override
    public void save() {
        SerializationUtil.serialize(billList, fileName);
    }

    @Override
    public Bill create(Bill bill) {
        bill.setId(nextIdUBill);
        nextIdUBill++;
        bill.setStatus("CHƯA THANH TOÁN");
        billList.add(bill);
        save();
        return bill;
    }

    @Override
    public void delete(Bill bill) {
        Bill billDelete = null;
        for (Bill user1 : billList) {
            if (user1.getId() == (bill.getId())) {
                billDelete = user1;
                break;
            }
        }
        billList.remove(billDelete);
        save();
    }

    @Override
    public void update(Bill bill) {
        List<Bill> bills = (List<Bill>) BillService.getInstance().readFile();
        for (Bill bil : bills) {
            if (bil.getId() == bill.getId()) {
                bil.setStatus(bill.getStatus());
                bil.setBookingDetail(bill.getBookingDetail());
                break;
            }
        }
        SerializationUtil.serialize(bills, fileName);
    }

    @Override
    public List<Bill> readFile() {
        return (List<Bill>) SerializationUtil.deserialize(fileName);
    }

    public void showBill(Bill bill, User user) {
        System.out.println("            HÓA ĐƠN THANH TOÁN");
        System.out.println("ID BILL : " + bill.getId());
        System.out.println("CUSTOMER :" + bill.getBookingDetail().getUser().getName());
        System.out.println("ROOM : " + bill.getBookingDetail().getRoom().getId());
        System.out.println("PRICE ROOM : " + CurrencyFormat.covertPriceToString(bill.getBookingDetail().getRoom().getPrice()));
        System.out.println("FEE CHANGE ROOM : " + CurrencyFormat.covertPriceToString(bill.getBookingDetail().getRoomChangeFee()));
        System.out.println("CHECK IN: " + bill.getBookingDetail().getTimeCheckin());
        System.out.println("CHECK OUT: " + bill.getBookingDetail().getTimeCheckout());
        System.out.println("DAY IN : " + bill.getBookingDetail().getTimeReal());
        System.out.println("PRODUCT : ");
        System.out.println("Product:");
        for (Product product : bill.getBookingDetail().getProduct()) {
            System.out.println("  Name: " + product.getName() + " ,Price: " + CurrencyFormat.covertPriceToString(product.getPrice())
                    + " ,Quantity: " + product.getQuantity() + " ,Total: " + CurrencyFormat.covertPriceToString(product.getTotal()));
        }
        Bank bank = bill.getBookingDetail().getUser().getBank();
        if (bank != null) {
            System.out.println("Bank: " + ((bill.getBookingDetail().getUser().getBank().getNameBank() != null) ? bill.getBookingDetail().getUser().getBank().getNameBank() : "NONE"));
            System.out.println("Number card: " + ((bill.getBookingDetail().getUser().getBank().getCardID() != null) ? bill.getBookingDetail().getUser().getBank().getCardID() : "NONE"));
        } else {
            System.out.println("Bank: NONE");
            System.out.println("Number card: NONE");
        }
        System.out.println("TOTAL : " + CurrencyFormat.covertPriceToString(bill.getBookingDetail().getTotal()));
        System.out.println("CASHIER : (" + user.getRole() + " ) " + user.getName() );
    }

    @Override
    public Bill find(int idT) {
        return null;
    }

    public Bill find(long idBill, String str) {
        List<Bill> bills = BillService.INSTANCE_BILL_SERVICE.readFile();
        if (bills.stream().anyMatch(u -> u.getId() == idBill && u.getStatus().equals(str))) {
            return bills.stream()
                    .filter(u -> u.getId() == idBill && u.getStatus().equals(str))
                    .findFirst()
                    .orElseThrow();
        }
        return null;
    }

    public Bill findBillByIdWithRetry(long idBill, String str) {
        Bill foundBill = null;
        foundBill = find(idBill, str);
        if (foundBill == null) {
            System.out.println("Bill with the provided ID not found. Please try again.");
            return findBillByIdWithRetry(GetValue.getLong("Nhập lại ID: "), str);
        }
        return foundBill;
    }

    public List<Bill> findBillByIdWithRetry(User user) {
        List<Bill> bills = BillService.INSTANCE_BILL_SERVICE.readFile();
        if (bills.stream().anyMatch(bill -> bill.getBookingDetail().getUser().getId() == user.getId())) {
            return bills.stream()
                    .filter(bill -> bill.getBookingDetail().getUser().getId() == user.getId())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public Bill findBill(User user, String str) {
        List<Bill> bills = BillService.INSTANCE_BILL_SERVICE.readFile();
        if (bills.stream()
                .anyMatch(bill -> bill.getBookingDetail().getUser().getId() == user.getId()
                        && bill.getStatus().equals(str))) {
            return bills.stream()
                    .filter(bill -> bill.getBookingDetail().getUser().getId() == user.getId()).findFirst().orElseThrow();
        }
        return null;
    }


    public double calculateDailyRevenue(List<Bill> billList, LocalDate date) {
        double totalRevenue = 0.0;
        for (Bill bill : billList) {
            LocalDate billDate = bill.getBookingDetail().getTimeCheckin();
            if (billDate.equals(date)) {
                totalRevenue += bill.getBookingDetail().getTotal();
            }
        }
        return totalRevenue;
    }
    public void getRevenueForSpecificDay(LocalDate date) {
        List<Bill> bills = BillService.INSTANCE_BILL_SERVICE.readFile();
        double revenue = calculateDailyRevenue(bills, date);
        System.out.printf("Doanh thu ngày %d/tháng %d/năm %d: %s\n", date.getDayOfMonth(), date.getMonthValue(), date.getYear(), revenue);
    }

    public void getRevenueAllYear(LocalDate date) {
        List<Bill> bills = BillService.INSTANCE_BILL_SERVICE.readFile();
        double[] monthlyRevenues = new double[12];
        for (int i = 1; i <= 12; i++) {
            monthlyRevenues[i - 1] = calculateMonthlyRevenue(bills, date.getYear(), i);
        }
        System.out.println("Doanh thu từng tháng trong năm 2023:");
        for (int i = 1; i <= 12; i++) {
            System.out.printf("Tháng %-5d: %-20s\n", i, covertPriceToString(monthlyRevenues[i - 1]));
        }
    }

    public double calculateMonthlyRevenue(List<Bill> billList, int year, int month) {
        double monthlyRevenue = 0.0;
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        for (Bill bill : billList) {
            LocalDate billDate = bill.getBookingDetail().getTimeCheckin();
            if (billDate.isEqual(startDate) || (billDate.isAfter(startDate) && billDate.isBefore(endDate))) {
                monthlyRevenue += bill.getBookingDetail().getTotal();
            }
        }
        return monthlyRevenue;
    }

    public void showBill(List<Bill> bill, User user) {
        int number = 1;
        double sum = 0d;
        System.out.println("         HÓA ĐƠN THANH TOÁN  ");
        System.out.println("Tên khách hàng: " + user.getName());
        for (Bill item : bill) {
            if (item.getBookingDetail().getUser().getId() == user.getId() && item.getStatus().equals("CHƯA THANH TOÁN")) {
                System.out.println("         HÓA ĐƠN " + number);
                System.out.println("Room:" + item.getBookingDetail().getRoom().getId());
                System.out.println("Price room:" + CurrencyFormat.covertPriceToString(item.getBookingDetail().getRoom().getPrice()));
                System.out.println("Fee change room:" + CurrencyFormat.covertPriceToString(item.getBookingDetail().getRoomChangeFee()));
                System.out.println("Check in: " + item.getBookingDetail().getTimeCheckin());
                System.out.println("Check out: " + item.getBookingDetail().getTimeCheckout());
                System.out.println("Date:" + item.getBookingDetail().getTimeReal() + " of day.");
                System.out.println("Product:");
                for (Product product : item.getBookingDetail().getProduct()) {
                    System.out.println("  Name: " + product.getName() + " ,Price: " + CurrencyFormat.covertPriceToString(product.getPrice())
                            + " ,Quantity: " + product.getQuantity() + " ,Total: " + CurrencyFormat.covertPriceToString(product.getTotal()));
                }
                System.out.println("Tổng hóa đơn " + number + " : " + CurrencyFormat.covertPriceToString(item.getBookingDetail().getTotal()));
                number++;
                System.out.println();
                sum += item.getBookingDetail().getTotal();
            }
        }
        Bank bank = user.getBank();
        if (bank != null) {
            System.out.println("Bank: " + ((user.getBank().getNameBank() != null) ? user.getBank().getNameBank() : "NONE"));
            System.out.println("Number card: " + ((user.getBank().getCardID() != null) ? user.getBank().getCardID() : "NONE"));
        } else {
            System.out.println("Bank: NONE");
            System.out.println("Number card: NONE");
        }
        System.out.println("TOTAL ALl BILL: " + CurrencyFormat.covertPriceToString(sum));

    }
}
