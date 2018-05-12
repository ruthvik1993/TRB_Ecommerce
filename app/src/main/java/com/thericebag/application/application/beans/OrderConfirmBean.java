package com.thericebag.application.application.beans;


import java.util.ArrayList;

public class OrderConfirmBean {
    private ArrayList<OrderConfirmedProducts> product_details;

    private String dt;

    private String waybill;

    private String base_price;

    private String phone;

    private String shipping_adrees;

    private String billingadd;

    private String status;

    private String id;

    private String purchase_price;

    private String expected;

    private String card_type;

    private String first_order;

    private String reference_no;

    private String email_id;

    private String image_order_confirmed;

    private String del_charges;

    public String getDel_charges() {
        return del_charges;
    }

    public void setDel_charges(String del_charges) {
        this.del_charges = del_charges;
    }

    public String getImage_order_confirmed() {
        return image_order_confirmed;
    }

    public void setImage_order_confirmed(String image_order_confirmed) {
        this.image_order_confirmed = image_order_confirmed;
    }

    public ArrayList<OrderConfirmedProducts> getProduct_details() {
        return product_details;
    }

    public void setProduct_details(ArrayList<OrderConfirmedProducts> product_details) {
        this.product_details = product_details;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public String getBase_price() {
        return base_price;
    }

    public void setBase_price(String base_price) {
        this.base_price = base_price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShipping_adrees() {
        return shipping_adrees;
    }

    public void setShipping_adrees(String shipping_adrees) {
        this.shipping_adrees = shipping_adrees;
    }

    public String getBillingadd() {
        return billingadd;
    }

    public void setBillingadd(String billingadd) {
        this.billingadd = billingadd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getFirst_order() {
        return first_order;
    }

    public void setFirst_order(String first_order) {
        this.first_order = first_order;
    }

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

}