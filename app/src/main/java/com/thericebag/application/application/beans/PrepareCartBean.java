package com.thericebag.application.application.beans;

import java.util.ArrayList;

/**
 * Created by abhil on 30-10-2016.
 */

public class PrepareCartBean {

    UserProfileBean address;

    ArrayList<OfferBean> offer_details;

    ArrayList<ProductBean> product_details;

    ShippingCharges shippingCharges;

    public ShippingCharges getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(ShippingCharges shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public UserProfileBean getAddress() {
        return address;
    }

    public void setAddress(UserProfileBean address) {
        this.address = address;
    }

    public ArrayList<OfferBean> getOffer_details() {
        return offer_details;
    }

    public void setOffer_details(ArrayList<OfferBean> offer_details) {
        this.offer_details = offer_details;
    }

    public ArrayList<ProductBean> getProduct_details() {
        return product_details;
    }

    public void setProduct_details(ArrayList<ProductBean> product_details) {
        this.product_details = product_details;
    }
}
