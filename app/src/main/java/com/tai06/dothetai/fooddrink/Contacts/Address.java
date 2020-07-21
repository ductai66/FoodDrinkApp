package com.tai06.dothetai.fooddrink.Contacts;

import java.util.regex.Pattern;

public class Address {

    public static final String url_getNameQuan = "https://dothetaind00.000webhostapp.com/FoodDrink/Query/getNameQuan.php";
    public static final String url_getTenPhuong = "https://dothetaind00.000webhostapp.com/FoodDrink/Query/getTenPhuong.php";

    //id_quận
    public static final int id_hoankiem = 1;
    public static final int id_dongda = 2;
    public static final int id_badinh = 3;
    public static final int id_haibatrung = 4;
    public static final int id_hoangmai = 5;
    public static final int id_thanhxuan = 6;
    public static final int id_longbien = 7;
    public static final int id_namtuliem = 8;
    public static final int id_bactuliem = 9;
    public static final int id_tayho = 10;
    public static final int id_caugiay = 11;
    public static final int id_hadong = 12;


    //check email
    public static final Pattern PATTERN_EMAIL = Pattern.compile("^[a-zA-Z0-9_[^!@#%^&$*()\\s-+=]]+@[ge]mail.com$");
    public static final Pattern PATTERN_PASSWORD = Pattern.compile("^[a-zA-Z0-9[^_!@#%^&$*()\\s-+=]]{8,}$");
    public static final Pattern PATTERN_PHONE = Pattern.compile("^(03|05|07|08|09)+[0-9]{8,}$");
    public static final String SUBJECT_SEND = "FoodD & Drink";
}
