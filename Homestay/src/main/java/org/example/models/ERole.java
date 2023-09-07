package org.example.models;

public enum ERole {
    ADMIN("Admin"),CASHIER("Cashier"),CLIENT("Client");
    final String name;
    ERole(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
