package com.excelhk.openapi.demoservice.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author anita
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "loans")
public class Loan {

    private String prodId;
    private String product;
    private String type;
    private String subtype;
    private String interestRate;
   /** Product Info. 1*/
    private String prdinfo1;
    private String prdinfo2;
    private String prdinfo3;
    private String fee;
    private String remark;
	public String getProdId() {
        return prodId;
    }
	public void setProdId(String prodId) {
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
    public String getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }
    public String getPrdinfo1() {
        return prdinfo1;
    }
    public void setPrdinfo1(String prdinfo1) {
        this.prdinfo1 = prdinfo1;
    }
    public String getPrdinfo2() {
        return prdinfo2;
    }
    public void setPrdinfo2(String prdinfo2) {
        this.prdinfo2 = prdinfo2;
    }
    public String getPrdinfo3() {
        return prdinfo3;
    }
    public void setPrdinfo3(String prdinfo3) {
        this.prdinfo3 = prdinfo3;
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

    @Override
    public String toString() {
        return "Loan{" +
                "prodId='" + prodId + '\'' +
                ", product='" + product + '\'' +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                ", interestRate='" + interestRate + '\'' +
                ", prdinfo1='" + prdinfo1 + '\'' +
                ", prdinfo2='" + prdinfo2 + '\'' +
                ", prdinfo3='" + prdinfo3 + '\'' +
                ", fee='" + fee + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Loan)) {
            return super.equals(obj);
        }
        return obj.toString().equalsIgnoreCase(this.toString());

    }
}
