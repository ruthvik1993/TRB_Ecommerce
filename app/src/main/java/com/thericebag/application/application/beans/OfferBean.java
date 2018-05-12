package com.thericebag.application.application.beans;

import java.io.Serializable;

/**
 * Created by abhil on 30-10-2016.
 */

public class OfferBean implements Serializable {

    public String act_pr_id;

    public String off_pr_id;

    public String off_amount;

    public String disc_img;

    public String offer_some;

    public String offer_kg;

    public String type;

    public ProductBean ProductDetail;

    public ProductBean getProductDetail() {
        return ProductDetail;
    }

    public void setProductDetail(ProductBean productDetail) {
        ProductDetail = productDetail;
    }

    public String getAct_pr_id() {
        return act_pr_id;
    }

    public void setAct_pr_id(String act_pr_id) {
        this.act_pr_id = act_pr_id;
    }

    public String getOff_pr_id() {
        return off_pr_id;
    }

    public void setOff_pr_id(String off_pr_id) {
        this.off_pr_id = off_pr_id;
    }

    public String getOff_amount() {
        return off_amount;
    }

    public void setOff_amount(String off_amount) {
        this.off_amount = off_amount;
    }

    public String getDisc_img() {
        return disc_img;
    }

    public void setDisc_img(String disc_img) {
        this.disc_img = disc_img;
    }

    public String getOffer_some() {
        return offer_some;
    }

    public void setOffer_some(String offer_some) {
        this.offer_some = offer_some;
    }

    public String getOffer_kg() {
        return offer_kg;
    }

    public void setOffer_kg(String offer_kg) {
        this.offer_kg = offer_kg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
