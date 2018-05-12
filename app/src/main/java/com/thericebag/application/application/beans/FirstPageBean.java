package com.thericebag.application.application.beans;

import java.util.ArrayList;

public class FirstPageBean {
    ArrayList<ProductBean> allProducts = new ArrayList<ProductBean>();
    ArrayList<CategoryNameBean> categories_list = new ArrayList<CategoryNameBean>();
    ArrayList<ProductBean> best_seller_products = new ArrayList<ProductBean>();
    String slider_images;
//    ArrayList<String> slider_images = new ArrayList<String>();

    /*public ArrayList<String> getSlider_images() {
        return slider_images;
    }

    public void setSlider_images(ArrayList<String> slider_images) {
        this.slider_images = slider_images;
    }*/

    public String getSlider_images() {
        return slider_images;
    }

    public void setSlider_images(String slider_images) {
        this.slider_images = slider_images;
    }

    public ArrayList<ProductBean> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(ArrayList<ProductBean> allProducts) {
        this.allProducts = allProducts;
    }

    public ArrayList<CategoryNameBean> getCategories_list() {
        return categories_list;
    }

    public void setCategories_list(ArrayList<CategoryNameBean> categories_list) {
        this.categories_list = categories_list;
    }

    public ArrayList<ProductBean> getBest_seller_products() {
        return best_seller_products;
    }

    public void setBest_seller_products(ArrayList<ProductBean> best_seller_products) {
        this.best_seller_products = best_seller_products;
    }
}
