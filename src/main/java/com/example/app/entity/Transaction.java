package com.example.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "MILK_TRANSACTIONS")
public class Transaction {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CUST_ID", nullable = false)
    private Long custId;

    @Column(name = "PROD_ID", nullable = false)
    private Long prodId;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "T_DATE", nullable = false)
    private LocalDate tDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUST_ID", insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROD_ID", insertable = false, updatable = false)
    private Product product;

    public Transaction() {
    }

    public Transaction(Long id, Long custId, Long prodId, Integer quantity, BigDecimal price, LocalDate tDate) {
        this.id = id;
        this.custId = custId;
        this.prodId = prodId;
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

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}