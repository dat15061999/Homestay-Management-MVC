package org.example.models;

public enum EGender {
    MALE("Nam"),FEMALE("Nữ"),OTHERS("Khác");
    final String name;
    EGender(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
