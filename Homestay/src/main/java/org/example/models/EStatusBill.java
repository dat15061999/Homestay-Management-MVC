package org.example.models;

public enum EStatusBill {
    NONE("CHƯA THANH TOÁN"),PAYED("ĐÃ THANH TOÁN");
    final String name;
    EStatusBill(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
