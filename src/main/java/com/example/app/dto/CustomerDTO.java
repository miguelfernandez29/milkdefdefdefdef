package com.example.app.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CustomerDTO {

    @NotNull(message = "ID is required")
    @Positive(message = "ID must be positive")
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address Line 1 is required")
    private String add1;

    @NotBlank(message = "Address Line 2 is required")
    private String add2;

    @NotNull(message = "Pincode is required")
    @Positive(message = "Pincode must be positive")
    private Long pincode;

    @NotNull(message = "Phone is required")
    @Positive(message = "Phone must be positive")
    private Long phone;

    public CustomerDTO() {
    }

    public CustomerDTO(Long id, String name, String add1, String add2, Long pincode, Long phone) {
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