package com.tai06.dothetai.fooddrink.Object.OOP;

import java.io.Serializable;

public class Person implements Serializable {
    private int id_person;
    private String email;
    private String password;
    private String name;
    private String sdt;

    public Person() {
    }

    public Person(int id_person, String email, String password, String name, String sdt) {
        this.id_person = id_person;
        this.email = email;
        this.password = password;
        this.name = name;
        this.sdt = sdt;
    }

    public int getId_person() {
        return id_person;
    }

    public void setId_person(int id_person) {
        this.id_person = id_person;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
