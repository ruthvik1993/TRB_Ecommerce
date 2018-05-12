package com.thericebag.application.application.beans;

/**
 * Created by abhil on 10-10-2016.
 */

public class PreviousOrderBean {
    String reference_no, dt, dispatched_st, card_type, purchase_price, sizes_quantity, title;

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDispatched_st() {
        return dispatched_st;
    }

    public void setDispatched_st(String dispatched_st) {
        this.dispatched_st = dispatched_st;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getSizes_quantity() {
        return sizes_quantity;
    }

    public void setSizes_quantity(String sizes_quantity) {
        this.sizes_quantity = sizes_quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
