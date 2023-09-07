package org.example.models;

import lombok.Getter;

@Getter
public enum ECategory {
    DRINK("Đồ uống"),EAT(" Đồ ăn"),SERVICE("Dịch vụ");
    final String name;
    ECategory(String name) {
        this.name = name;
    }
}
