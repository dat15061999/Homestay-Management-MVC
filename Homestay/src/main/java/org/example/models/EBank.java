package org.example.models;

import lombok.Getter;

@Getter
public enum EBank {
    MBBANK("MB BANK"),TECHCOMBANK(" TECHCOMBANK"),VIETCOMBANK("VIETCOMBANK"),AGRIBANK("AGRIBANK");
    final String name;
    EBank(String name) {
        this.name = name;
    }

}
