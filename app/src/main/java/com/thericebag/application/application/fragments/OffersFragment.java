package com.thericebag.application.application.fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.thericebag.application.R;
import com.thericebag.application.application.Activities.checkOutActivity;
import com.thericebag.application.application.beans.OfferBean;
import com.thericebag.application.application.beans.ProductBean;
import com.thericebag.application.application.utility.AppConstants;

import java.util.ArrayList;


/**
 * Created by atchy on 26-07-2016.
 */
public class OffersFragment extends Fragment {

    public View mView;
    RadioGroup radioGroupOffers;
    RadioButton radioOffer1, radioOffer2, radioOffer3;
    TextView textOfferSelected;
    private ArrayList<OfferBean> offersObjList;
    private String offerString1, offerString2, offerString3;
    private String discountAvailed;
    private ProductBean productBeanObj, getProductBeanObjDummy;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_offers, container, false);
        progressDialog = new ProgressDialog(getContext());
        offersObjList = (ArrayList<OfferBean>) getArguments().getSerializable(AppConstants.OFFERS_DETAILS);
        System.out.println(offersObjList.size() + "");
        initializeViews();
        radioGroupOffers.clearCheck();

        setOffersToUI();

        setRadioButtonState();

        checkedChangeListener();

        return mView;
    }

    private void setRadioButtonState() {
        final Drawable drawableTop1Selected = getResources().getDrawable(R.drawable.offer_pricediscount_selected);
        final Drawable drawableTop2Selected = getResources().getDrawable(R.drawable.offer_newproduct_selected);
        final Drawable drawableTop3Selected = getResources().getDrawable(R.drawable.offer_specialoffers_selected);
        final Drawable drawableTop1UnSelected = getResources().getDrawable(R.drawable.offer_pricediscount_notselected);
        final Drawable drawableTop2UnSelected = getResources().getDrawable(R.drawable.offer_newproduct_notselected);
        final Drawable drawableTop3UnSelected = getResources().getDrawable(R.drawable.offer_specialoffers_notselected);

        StateListDrawable mState1 = new StateListDrawable();
        mState1.addState(new int[]{android.R.attr.state_checked}, drawableTop1Selected);
        mState1.addState(new int[]{}, drawableTop1UnSelected);
        radioOffer1.setCompoundDrawablesWithIntrinsicBounds(null, mState1, null, null);

        StateListDrawable mState2 = new StateListDrawable();
        mState2.addState(new int[]{android.R.attr.state_checked}, drawableTop2Selected);
        mState2.addState(new int[]{}, drawableTop2UnSelected);
        radioOffer2.setCompoundDrawablesWithIntrinsicBounds(null, mState2, null, null);

        StateListDrawable mState3 = new StateListDrawable();
        mState3.addState(new int[]{android.R.attr.state_checked}, drawableTop3Selected);
        mState3.addState(new int[]{}, drawableTop3UnSelected);
        radioOffer3.setCompoundDrawablesWithIntrinsicBounds(null, mState3, null, null);
    }

    private void setOffersToUI() {
        productBeanObj = new ProductBean();
        for (int i = 0; i < offersObjList.size(); i++) {
            OfferBean offerObj = new OfferBean();
            offerObj = offersObjList.get(i);
            if (offerObj.getType().equalsIgnoreCase(AppConstants.OFFER_DISCOUNT)) {
                discountAvailed = offerObj.getOff_amount();
                offerString1 = "Get Rs." + offerObj.getOff_amount() + "/- off on your cart value";
                radioOffer1.setVisibility(View.VISIBLE);
            } else if (offerObj.getType().equalsIgnoreCase(AppConstants.OFFEr_DISCOUNT_PRODUCT)) {
                offerString2 = "Get Rs.95/- worth basmati for Rs.50/-";
                radioOffer2.setVisibility(View.VISIBLE);
                productBeanObj = offerObj.getProductDetail();
                productBeanObj.setNumberOfItems(1);
                productBeanObj.setUnitPrice(productBeanObj.getOff_price());
                productBeanObj.setOfferProduct(true);
            } else if (offerObj.getType().equalsIgnoreCase(AppConstants.OFFER_FREE_PRODUCT)) {
                discountAvailed = "0";
                offerString3 = offerObj.getOffer_some();
                radioOffer3.setVisibility(View.VISIBLE);
            }
        }

        if (offersObjList.size() == 0) {
            radioGroupOffers.setVisibility(View.GONE);
            textOfferSelected.setText("Currently, there are no offers available for this product");
        } else {
            radioGroupOffers.setVisibility(View.VISIBLE);
        }
    }

    private void initializeViews() {
        radioGroupOffers = (RadioGroup) mView.findViewById(R.id.radioGroupOffers);
        radioOffer1 = (RadioButton) mView.findViewById(R.id.radioOffer1);
        radioOffer2 = (RadioButton) mView.findViewById(R.id.radioOffer2);
        radioOffer3 = (RadioButton) mView.findViewById(R.id.radioOffer3);
        textOfferSelected = (TextView) mView.findViewById(R.id.textOfferSelected);
    }

    private void checkedChangeListener() {
        radioGroupOffers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                progressDialog.setMessage("Selecting the offer...");
//                progressDialog.show();
                if (checkedId == R.id.radioOffer1) {
                    final Snackbar snackBar = Snackbar.make(radioGroupOffers, "Discount availed successfullly", Snackbar.LENGTH_SHORT);
                    snackBar.show();
                    textOfferSelected.setText(offerString1);
                    ((checkOutActivity) getActivity()).removeOfferProduct(productBeanObj, discountAvailed, 1);
                } else if (checkedId == R.id.radioOffer2) {
                    final Snackbar snackBar = Snackbar.make(radioGroupOffers, "Offer Product added successfully", Snackbar.LENGTH_SHORT);
                    snackBar.show();
                    textOfferSelected.setText(offerString2);
                    ((checkOutActivity) getActivity()).updateOfferProduct(productBeanObj);
                } else if (checkedId == R.id.radioOffer3) {
                    final Snackbar snackBar = Snackbar.make(radioGroupOffers, "Offer Product added successfully", Snackbar.LENGTH_SHORT);
                    snackBar.show();
                    textOfferSelected.setText(offerString3);
                    ((checkOutActivity) getActivity()).removeOfferProduct(productBeanObj, discountAvailed, 3);
                    ((checkOutActivity) getActivity()).disc_fully_product = offerString3;
                }
            }
        });
    }

}