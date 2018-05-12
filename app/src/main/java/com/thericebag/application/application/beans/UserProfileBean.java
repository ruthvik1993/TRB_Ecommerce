package com.thericebag.application.application.beans;

import java.util.ArrayList;

/**
 * Created by abhil on 22-09-2016.
 */
public class UserProfileBean {

    String id, display_name, fname, mname, lname, email, phone, company, address1_line1, address1_phone, address2_phone,
            address1_line2, address2_landmark, address1_zip, address1_city, address1_state, address1_country,
            address2_line1, address2_line2, address2_zip, address2_city, address2_state,
            address2_country, address1_name, address2_name, address1_landmark;

    ArrayList<PreviousOrderBean> previousOrders = new ArrayList<PreviousOrderBean>();

    public String getAddress1_phone() {
        return address1_phone;
    }

    public void setAddress1_phone(String address1_phone) {
        this.address1_phone = address1_phone;
    }

    public String getAddress2_phone() {
        return address2_phone;
    }

    public void setAddress2_phone(String address2_phone) {
        this.address2_phone = address2_phone;
    }

    public String getAddress1_landmark() {
        return address1_landmark;
    }

    public void setAddress1_landmark(String address1_landmark) {
        this.address1_landmark = address1_landmark;
    }

    public String getAddress2_landmark() {
        return address2_landmark;
    }

    public void setAddress2_landmark(String address2_landmark) {
        this.address2_landmark = address2_landmark;
    }

    public String getAddress1_name() {
        return address1_name;
    }

    public void setAddress1_name(String address1_name) {
        this.address1_name = address1_name;
    }

    public String getAddress2_name() {
        return address2_name;
    }

    public void setAddress2_name(String address2_name) {
        this.address2_name = address2_name;
    }

    public String getAddress2_city() {
        return address2_city;
    }

    public void setAddress2_city(String address2_city) {
        this.address2_city = address2_city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress1_line1() {
        return address1_line1;
    }

    public void setAddress1_line1(String address1_line1) {
        this.address1_line1 = address1_line1;
    }

    public String getAddress1_line2() {
        return address1_line2;
    }

    public void setAddress1_line2(String address1_line2) {
        this.address1_line2 = address1_line2;
    }

    public String getAddress1_zip() {
        return address1_zip;
    }

    public void setAddress1_zip(String address1_zip) {
        this.address1_zip = address1_zip;
    }

    public String getAddress1_city() {
        return address1_city;
    }

    public void setAddress1_city(String address1_city) {
        this.address1_city = address1_city;
    }

    public String getAddress1_state() {
        return address1_state;
    }

    public void setAddress1_state(String address1_state) {
        this.address1_state = address1_state;
    }

    public String getAddress1_country() {
        return address1_country;
    }

    public void setAddress1_country(String address1_country) {
        this.address1_country = address1_country;
    }

    public String getAddress2_line1() {
        return address2_line1;
    }

    public void setAddress2_line1(String address2_line1) {
        this.address2_line1 = address2_line1;
    }

    public String getAddress2_line2() {
        return address2_line2;
    }

    public void setAddress2_line2(String address2_line2) {
        this.address2_line2 = address2_line2;
    }

    public String getAddress2_zip() {
        return address2_zip;
    }

    public void setAddress2_zip(String address2_zip) {
        this.address2_zip = address2_zip;
    }

    public String getAddress2_state() {
        return address2_state;
    }

    public void setAddress2_state(String address2_state) {
        this.address2_state = address2_state;
    }

    public String getAddress2_country() {
        return address2_country;
    }

    public void setAddress2_country(String address2_country) {
        this.address2_country = address2_country;
    }

    public ArrayList<PreviousOrderBean> getPreviousOrders() {
        return previousOrders;
    }

    public void setPreviousOrders(ArrayList<PreviousOrderBean> previousOrders) {
        this.previousOrders = previousOrders;
    }
}
