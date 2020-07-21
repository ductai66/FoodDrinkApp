package com.tai06.dothetai.fooddrink.Object.OOP;

import java.io.Serializable;

public class Phuong implements Serializable {
    private int id_phuong;
    private int id_quan;
    private String ten_phuong;

    public Phuong(int id_phuong, int id_quan, String ten_phuong) {
        this.id_phuong = id_phuong;
        this.id_quan = id_quan;
        this.ten_phuong = ten_phuong;
    }

    public int getId_phuong() {
        return id_phuong;
    }

    public void setId_phuong(int id_phuong) {
        this.id_phuong = id_phuong;
    }

    public int getId_quan() {
        return id_quan;
    }

    public void setId_quan(int id_quan) {
        this.id_quan = id_quan;
    }

    public String getTen_phuong() {
        return ten_phuong;
    }

    public void setTen_phuong(String ten_phuong) {
        this.ten_phuong = ten_phuong;
    }
}
