package com.thericebag.application.application.beans;

/**
 * Created by srivijay maddukuri on 3/21/2017.
 */

public class ShippingCharges {
    public String cart_val, deliver_charges, express_del_chrgs;

    public String getCart_val() {
        return cart_val;
    }

    public void setCart_val(String cart_val) {
        this.cart_val = cart_val;
    }

    public String getDeliver_charges() {
        return deliver_charges;
    }

    public void setDeliver_charges(String deliver_charges) {
        this.deliver_charges = deliver_charges;
    }

    public String getExpress_del_chrgs() {
        return express_del_chrgs;
    }

    public void setExpress_del_chrgs(String express_del_chrgs) {
        this.express_del_chrgs = express_del_chrgs;
    }
}
