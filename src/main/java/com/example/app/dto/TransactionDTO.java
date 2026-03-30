package com.example.app.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDTO {

    @NotNull(message = "ID is required")
    @Positive(message = "ID must be positive")
    private Long id;

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be positive")
    private Long custId;

    private String custName;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long prodId;

    private String prodName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    private BigDecimal price;

    @NotNull(message = "Transaction Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tDate;

    public TransactionDTO() {
    }

    public TransactionDTO(Long id, Long custId, String custName, Long prodId, String prodName,
                          Integer quantity, BigDecimal price, LocalDate tDate) {
        this.id = id;
        this.custId = custId;
        this.custName = custName;
        this.prodId = prodId;
        this.prodName = prodName;
        this.quantity = quantity;
        this.price = price;
        this.tDate = tDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getTDate() {
        return tDate;
    }

    public void setTDate(LocalDate tDate) {
        this.tDate = tDate;
    }
}