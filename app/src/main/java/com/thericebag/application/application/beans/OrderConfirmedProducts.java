package com.thericebag.application.application.beans;

/**
 * Created by abhil on 03-11-2016.
 */

public class OrderConfirmedProducts {

    String product_id, sizes_quantity, purchase_quantity, purchase_price, title, description, short_desc;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSizes_quantity() {
        return sizes_quantity;
    }

    public void setSizes_quantity(String sizes_quantity) {
        this.sizes_quantity = sizes_quantity;
    }

    public String getPurchase_quantity() {
        return purchase_quantity;
    }

    public void setPurchase_quantity(String purchase_quantity) {
        this.purchase_quantity = purchase_quantity;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }
}
