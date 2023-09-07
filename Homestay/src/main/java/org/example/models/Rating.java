package org.example.models;

public class Rating {
    private int id;
    private float math; //thang diem 10
    private float rate; // thang diem 5
    private String comment;

    public Rating(int id, float math, String comment) {
        this.id = id;
        this.math = math;
        this.comment = comment;
    }
}
