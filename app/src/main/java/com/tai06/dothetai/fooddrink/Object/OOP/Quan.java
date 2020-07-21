package com.tai06.dothetai.fooddrink.Object.OOP;

import java.io.Serializable;

public class Quan implements Serializable {
    private int id_quan;
    private String ten_quan;

    public Quan(int id_quan, String ten_quan) {
        this.id_quan = id_quan;
        this.ten_quan = ten_quan;
    }

    public int getId_quan() {
        return id_quan;
    }

    public void setId_quan(int id_quan) {
        this.id_quan = id_quan;
    }

    public String getTen_quan() {
        return ten_quan;
    }

    public void setTen_quan(String ten_quan) {
        this.ten_quan = ten_quan;
    }
}
