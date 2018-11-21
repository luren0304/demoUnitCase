package com.excel.gradle.demo.bean;

import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection="deposits")
public class Deposit {
    //Product ID

    private String prodId;
    private String product;
    private String type;
    private String subtype;
    private String currency;
    private String interestRate;
    private String minamount;
    private String fee;
    private String remark;


    public String getprodId() {
        return prodId;
    }
    public void setprodId(String prodId) {
        this.prodId = prodId;
    }
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSubtype() {
        return subtype;
    }
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }
    public String getMinamount() {
        return minamount;
    }
    public void setMinamount(String minamount) {
        this.minamount = minamount;
    }
    public String getFee() {
        return fee;
    }
    public void setFee(String fee) {
        this.fee = fee;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
