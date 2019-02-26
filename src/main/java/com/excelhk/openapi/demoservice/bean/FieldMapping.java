package com.excelhk.openapi.demoservice.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author anita
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "fieldMapping")
public class FieldMapping {
    private String product;
    private int order;
    private String field;
    private String fieldDesc;
    private boolean show;

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
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

    @Override
    public String toString() {
        return "FieldMapping{" +
                "product='" + product + '\'' +
                ", order=" + order +
                ", field='" + field + '\'' +
                ", fieldDesc='" + fieldDesc + '\'' +
                ", show=" + show +
                '}';
    }
}
