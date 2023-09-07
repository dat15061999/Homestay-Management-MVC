package org.example.models;

public enum EType {
    CLASS("THƯỜNG"),VIP("THƯƠNG GIA");
    final String name;
    EType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
