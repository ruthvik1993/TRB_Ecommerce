package com.thericebag.application.application.beans;

import java.io.Serializable;

public class ProductBean implements Serializable {
    int best_seller, off_price, id, act_price, quantity, numberOfItems, unitPrice, weight, sample_pr;
    String title, description, short_desc, packings, product_image, refund_policy;
    Boolean isFirstSample;
    boolean isOfferProduct = false;

    public String getRefund_policy() {
        return refund_policy;
    }

    public void setRefund_policy(String refund_policy) {
        this.refund_policy = refund_policy;
    }

    public Boolean getFirstSample() {
        return isFirstSample;
    }

    public void setFirstSample(Boolean firstSample) {
        isFirstSample = firstSample;
    }

    public boolean isOfferProduct() {
        return isOfferProduct;
    }

    public void setOfferProduct(boolean offerProduct) {
        isOfferProduct = offerProduct;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getPackings() {
        return packings;
    }

    public void setPackings(String packings) {
        this.packings = packings;
    }

    public int getSample_pr() {
        return sample_pr;
    }

    public void setSample_pr(int sample_pr) {
        this.sample_pr = sample_pr;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public int getBest_seller() {
        return best_seller;
    }

    public void setBest_seller(int best_seller) {
        this.best_seller = best_seller;
    }

    public int getOff_price() {
        return off_price;
    }

    public void setOff_price(int off_price) {
        this.off_price = off_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAct_price() {
        return act_price;
    }

    public void setAct_price(int act_price) {
        this.act_price = act_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
