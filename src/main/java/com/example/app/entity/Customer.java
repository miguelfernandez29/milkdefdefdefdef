package com.example.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MILK_CUSTOMERS")
public class Customer {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ADD1", nullable = false)
    private String add1;

    @Column(name = "ADD2", nullable = false)
    private String add2;

    @Column(name = "PINCODE", nullable = false)
    private Long pincode;

    @Column(name = "PHONE", nullable = false)
    private Long phone;

    public Customer() {
    }

    public Customer(Long id, String name, String add1, String add2, Long pincode, Long phone) {
        this.id = id;
        this.name = name;
        this.add1 = add1;
        this.add2 = add2;
        this.pincode = pincode;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd1() {
        return add1;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public String getAdd2() {
        return add2;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public Long getPincode() {
        return pincode;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}