package com.excelhk.openapi.demoservice.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "fieldMapping")
public class FieldMapping {
    private String product;
    private int order;
    private String field;
    private String fielddesc;
    private boolean show;

    public String getFielddesc() {
        return fielddesc;
    }

    public void setFielddesc(String fielddesc) {
        this.fielddesc = fielddesc;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
