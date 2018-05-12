package com.thericebag.application.application.beans;

import java.util.ArrayList;

public class CheckOutBean {

    private String orderNumber;

    private String dt;

    private String shippingaddress;

    private String base_price;

    private String phone;

    private ArrayList<ProductBean> Products;

    private String billingadd;

    private String purchase_price;

    private String expected;

    private String email;

    private String first_order;

    private String disc_fully_prd;

    private int disc_kg, disc_type, disc_pr_id, disc_price, del_charges;

    public int getDel_charges() {
        return del_charges;
    }

    public void setDel_charges(int del_charges) {
        this.del_charges = del_charges;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getShippingaddress() {
        return shippingaddress;
    }

    public void setShippingaddress(String shippingaddress) {
        this.shippingaddress = shippingaddress;
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

    public ArrayList<ProductBean> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<ProductBean> products) {
        Products = products;
    }

    public String getBillingadd() {
        return billingadd;
    }

    public void setBillingadd(String billingadd) {
        this.billingadd = billingadd;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_order() {
        return first_order;
    }

    public void setFirst_order(String first_order) {
        this.first_order = first_order;
    }

    public int getDisc_kg() {
        return disc_kg;
    }

    public void setDisc_kg(int disc_kg) {
        this.disc_kg = disc_kg;
    }

    public int getDisc_type() {
        return disc_type;
    }

    public void setDisc_type(int disc_type) {
        this.disc_type = disc_type;
    }

    public int getDisc_pr_id() {
        return disc_pr_id;
    }

    public void setDisc_pr_id(int disc_pr_id) {
        this.disc_pr_id = disc_pr_id;
    }

    public int getDisc_price() {
        return disc_price;
    }

    public void setDisc_price(int disc_price) {
        this.disc_price = disc_price;
    }

    public String getDisc_fully_prd() {
        return disc_fully_prd;
    }

    public void setDisc_fully_prd(String disc_fully_prd) {
        this.disc_fully_prd = disc_fully_prd;
    }

    @Override
    public String toString() {
        return "ClassPojo [dt = " + dt + ", shippingaddress = " + shippingaddress + ", base_price = " + base_price + ", phone = " + phone + ", Products = " + Products + ", billingadd = " + billingadd + ", disc_kg = " + disc_kg + ", disc_type = " + disc_type + ", disc_pr_id = " + disc_pr_id + ", purchase_price = " + purchase_price + ", expected = " + expected + ", email = " + email + ", first_order = " + first_order + ", disc_price = " + disc_price + ", disc_fully_prd = " + disc_fully_prd + "]";
    }
}