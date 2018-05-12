package com.thericebag.application.application.beans;

import java.util.ArrayList;

public class SubCategoryBean {
    ArrayList<ProductBean> subCategories_list = new ArrayList<ProductBean>();

    public ArrayList<ProductBean> getSubCategories_list() {
        return subCategories_list;
    }

    public void setSubCategories_list(ArrayList<ProductBean> subCategories_list) {
        this.subCategories_list = subCategories_list;
    }
}
