package com.thericebag.application.application.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.beans.UserProfileAPIBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

public class EditProfileActivity extends Activity {

    public TextView txtOrderNumber;
    public TextView txtStatus;
    public TextView txtProductTitle;
    public TextView txtPurchasePrice;
    public TextView textQty;
    public TextView txtCardType;
    public TextView txtOrderDate;
    ImageView backIcon;
    private TextView textView_UserName;
    private TextView textView_mobile;
    private TextView textView_email;
    private TextView txtViewAll;
    private TextView txtName;
    private TextView txtAddress1;
    private TextView textView_viewAllAddresses;
    private String profileDetailsString;
    private RelativeLayout relativeAddAddress;
    private RelativeLayout relative_addressBody;
    private RelativeLayout relativeNoOrders;
    private RelativeLayout relative_Orders;
    private Context mContext;
    private UserProfileAPIBean userProfileObj;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.editprofile_layout);

        mContext = EditProfileActivity.this;

        Utility.StartTrackingScreen(mContext, "5", "Profile Screen");

        progressDialog = new ProgressDialog(mContext);

        initializeViews();

        buttonClicks();

        getProfileData();

    }

    private void getProfileData() {

        String email = Utility.getEmail(mContext);

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,

                ApiConstants.ApiName.GETPROFILEDETAILS + "email=" + email, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {

                progressDialog.dismiss();

                if (response.isSuccess()) {

                    profileDetailsString = response.getResponseBody().toString();

                    Gson gson = new Gson();

                    userProfileObj = gson.fromJson(response.getResponseBody().toString(), UserProfileAPIBean.class);

                    setUIData();

                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);

                    networkError_dialog.showDialog(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            networkError_dialog.dismissDialog();

                            getProfileData();

                        }

                    });

                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }

            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);

        progressDialog.setMessage(AppConstants.GETTING_PROFILE);

        progressDialog.setCancelable(false);

        progressDialog.show();

    }

    private void setUIData() {

        textView_UserName.setText(userProfileObj.getProfile().getFname());

        textView_mobile.setText(userProfileObj.getProfile().getPhone());

        textView_email.setText(userProfileObj.getProfile().getEmail());

        if (userProfileObj.getProfile().getAddress1_line1() != null && userProfileObj.getProfile().getAddress1_line1().length() > 0) {

            txtName.setText(userProfileObj.getProfile().getAddress1_name());

            txtAddress1.setText(userProfileObj.getProfile().getAddress1_line1() + ", "
                    + userProfileObj.getProfile().getAddress1_line2() + ", "
                    + userProfileObj.getProfile().getAddress1_landmark() + ", "
                    + userProfileObj.getProfile().getAddress1_zip() + ", "
                    + userProfileObj.getProfile().getAddress1_city() + ", "
                    + userProfileObj.getProfile().getAddress1_state() + ", "
                    + userProfileObj.getProfile().getPhone());

        } else {
            relativeAddAddress.setVisibility(View.VISIBLE);
            relative_addressBody.setVisibility(View.GONE);
            textView_viewAllAddresses.setVisibility(View.GONE);
        }

        setPreviousOrderCard();

    }

    private void setPreviousOrderCard() {

        if (userProfileObj.getProfile().getPreviousOrders().size() > 0) {

            relative_Orders.setVisibility(View.VISIBLE);

            relativeNoOrders.setVisibility(View.GONE);

            txtOrderNumber.setText(userProfileObj.getProfile().getPreviousOrders().get(0).getReference_no());

            txtStatus.setText(userProfileObj.getProfile().getPreviousOrders().get(0).getDispatched_st());

            txtProductTitle.setText(userProfileObj.getProfile().getPreviousOrders().get(0).getTitle());

            txtPurchasePrice.setText("₹ " + userProfileObj.getProfile().getPreviousOrders().get(0).getPurchase_price());

            textQty.setText(userProfileObj.getProfile().getPreviousOrders().get(0).getSizes_quantity());

            txtCardType.setText(userProfileObj.getProfile().getPreviousOrders().get(0).getCard_type());

            txtOrderDate.setText(userProfileObj.getProfile().getPreviousOrders().get(0).getDt());

        } else {

            relative_Orders.setVisibility(View.GONE);

            relativeNoOrders.setVisibility(View.VISIBLE);

        }

    }

    private void buttonClicks() {

        relativeAddAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditAddress.class);

                intent.putExtra(AppConstants.ADDRESS_DETAILS, new Gson().toJson(userProfileObj));

                intent.putExtra(AppConstants.POSITION, 0);

                startActivity(intent);
            }
        });

        backIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();

            }

        });

        txtViewAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MyOrdersActivity.class);

                startActivity(intent);

            }

        });

        textView_viewAllAddresses.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MyAddressesActivity.class);

                intent.putExtra(AppConstants.PROFILE_DETAILS, profileDetailsString);

                startActivity(intent);

            }
        });

    }

    private void initializeViews() {

        txtName = (TextView) findViewById(R.id.txtName);

        txtAddress1 = (TextView) findViewById(R.id.txtAddress1);

        textView_email = (TextView) findViewById(R.id.textView_email);

        textView_mobile = (TextView) findViewById(R.id.textView_mobile);

        textView_UserName = (TextView) findViewById(R.id.textView_UserName);

        backIcon = (ImageView) findViewById(R.id.backIcon);

        txtViewAll = (TextView) findViewById(R.id.txtViewAll);

        textView_viewAllAddresses = (TextView) findViewById(R.id.textView_viewAllAddresses);

        relativeAddAddress = (RelativeLayout) findViewById(R.id.relativeAddAddress);

        relative_addressBody = (RelativeLayout) findViewById(R.id.relative_addressBody);

        txtOrderNumber = (TextView) findViewById(R.id.txtOrderNumber);

        txtStatus = (TextView) findViewById(R.id.txtStatus);

        txtProductTitle = (TextView) findViewById(R.id.txtProductTitle);

        txtPurchasePrice = (TextView) findViewById(R.id.txtPurchasePrice);

        textQty = (TextView) findViewById(R.id.textQty);

        txtCardType = (TextView) findViewById(R.id.txtCardType);

        txtOrderDate = (TextView) findViewById(R.id.txtOrderDate);

        relativeNoOrders = (RelativeLayout) findViewById(R.id.relativeNoOrders);

        relative_Orders = (RelativeLayout) findViewById(R.id.relative_Orders);

        relativeNoOrders.setVisibility(View.GONE);

    }

}
