package com.tai06.dothetai.fooddrink.Object.OOP;

import java.io.Serializable;

public class Hoadon implements Serializable {
    private int id_hd;
    private int id_mh;
    private int id_person;
    private String ten_kh;
    private String ten_mh;
    private String diachi;
    private String sdt;
    private String image;
    private int gia_mh;
    private int sl_mh;
    private int thanhtien;
    private String ngaydat;
    private String ghichu;
    private String trangthai;

    public Hoadon(int id_hd, int id_mh, int id_person, String ten_kh, String ten_mh, String diachi, String sdt, String image, int gia_mh, int sl_mh, int thanhtien, String ngaydat, String ghichu, String trangthai) {
        this.id_hd = id_hd;
        this.id_mh = id_mh;
        this.id_person = id_person;
        this.ten_kh = ten_kh;
        this.ten_mh = ten_mh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.image = image;
        this.gia_mh = gia_mh;
        this.sl_mh = sl_mh;
        this.thanhtien = thanhtien;
        this.ngaydat = ngaydat;
        this.ghichu = ghichu;
        this.trangthai = trangthai;
    }

    public int getId_hd() {
        return id_hd;
    }

    public void setId_hd(int id_hd) {
        this.id_hd = id_hd;
    }

    public int getId_mh() {
        return id_mh;
    }

    public void setId_mh(int id_mh) {
        this.id_mh = id_mh;
    }

    public int getId_person() {
        return id_person;
    }

    public void setId_person(int id_person) {
        this.id_person = id_person;
    }

    public String getTen_kh() {
        return ten_kh;
    }

    public void setTen_kh(String ten_kh) {
        this.ten_kh = ten_kh;
    }

    public String getTen_mh() {
        return ten_mh;
    }

    public void setTen_mh(String ten_mh) {
        this.ten_mh = ten_mh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(int thanhtien) {
        this.thanhtien = thanhtien;
    }

    public String getNgaydat() {
        return ngaydat;
    }

    public void setNgaydat(String ngaydat) {
        this.ngaydat = ngaydat;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
}
