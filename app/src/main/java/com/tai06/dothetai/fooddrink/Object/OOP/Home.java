package com.tai06.dothetai.fooddrink.Object.OOP;

import java.io.Serializable;
import java.util.Objects;

public class Home implements Serializable {
    private int id_mh;
    private int id_lh;
    private String ten_mh;
    private int gia_mh;
    private int sl_mh;
    private String image;

    public Home() {
    }

    public Home(int id_mh, int id_lh, String ten_mh, int gia_mh, int sl_mh, String image) {
        this.id_mh = id_mh;
        this.id_lh = id_lh;
        this.ten_mh = ten_mh;
        this.gia_mh = gia_mh;
        this.sl_mh = sl_mh;
        this.image = image;
    }

    public int getId_mh() {
        return id_mh;
    }

    public void setId_mh(int id_mh) {
        this.id_mh = id_mh;
    }

    public int getId_lh() {
        return id_lh;
    }

    public void setId_lh(int id_lh) {
        this.id_lh = id_lh;
    }

    public String getTen_mh() {
        return ten_mh;
    }

    public void setTen_mh(String ten_mh) {
        this.ten_mh = ten_mh;
    }

    public int getGia_mh() {
        return gia_mh;
    }

    public void setGia_mh(int gia_mh) {
        this.gia_mh = gia_mh;
    }

    public int getSl_mh() {
        return sl_mh;
    }

    public void setSl_mh(int sl_mh) {
        this.sl_mh = sl_mh;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
