package com.aninfo.model;

import com.aninfo.service.TransactionService;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long associatedCbu;
    private String type;

    private double amount;

    public Transaction(){}

    public Transaction(Long associatedCbu,String type, double amount){
        this.type = type;
        this.amount = amount;
        this.associatedCbu = associatedCbu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAssociatedCbu() {
        return associatedCbu;
    }

    public void setAssociatedCbu(Long associatedCbu) {
        this.associatedCbu = associatedCbu;
    }




}
