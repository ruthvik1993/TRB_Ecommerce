package com.thericebag.application.application.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.adapters.RecyclerAdapter;
import com.thericebag.application.application.beans.OrderConfirmBean;
import com.thericebag.application.application.utility.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationToUserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> list = new ArrayList<>();
    TextView txtContinue;
    TextView txtTotalAmount, txtDeliveryAmount;
    Toolbar toolbar;

    private Context mContext;

    private ProgressDialog progressDialog;

    private OrderConfirmBean orderConfirmObj;

    private ImageView imgConfirmationScreen;

    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confirmationtouser_layout);

        mContext = ConfirmationToUserActivity.this;

        progressDialog = new ProgressDialog(mContext);

//        analyticsFunction();

        initializeViews();

        buttonOnClicks();

        getData();

//        addDummyData();

    }


    /*private void analyticsFunction() {
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();

        mTracker.setScreenName("Image~" + "CategorySpecificActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());
    }*/

    private void getData() {

        if (getIntent().getExtras() != null) {
            orderID = getIntent().getExtras().getString(AppConstants.ORDER_NUMBER);

            TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                    ApiConstants.ApiName.ORDER_CONFIRMED + "orderID=" + orderID, new Response.Listener<TheRiceBagResponse>() {
                @Override
                public void onResponse(TheRiceBagResponse response) {
                    progressDialog.dismiss();

                    if (response.getResponseBody() != null && !response.getResponseBody().equalsIgnoreCase("null") && response.isSuccess()) {
                        orderConfirmObj = new Gson().fromJson(response.getResponseBody(), OrderConfirmBean.class);
                        updateUI();
                        System.out.print(orderConfirmObj.getBillingadd());
                    } else if (response.getErrorMessage() != null && response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                        final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                        networkError_dialog.showDialog(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                networkError_dialog.dismissDialog();
                                getData();
                            }
                        });

                    } else {
                        Toast.makeText(mContext, "Your order is not placed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, Home.class);
                        startActivity(intent);
                    }
                }
            }, null);

            NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
            progressDialog.setMessage("Confirming your order");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {
            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUI() {

        Picasso.with(mContext)
                .load(orderConfirmObj.getImage_order_confirmed())
                //.placeholder(R.drawable.progress_animation)
                .into(imgConfirmationScreen);

        int totalPrice = 0;
        if (orderConfirmObj != null && orderConfirmObj.getDel_charges() != null) {
            totalPrice = Integer.parseInt(orderConfirmObj.getDel_charges());
            txtDeliveryAmount.setText("₹ " + orderConfirmObj.getDel_charges());
        } else {
            txtDeliveryAmount.setText("₹0");
        }
        for (int i = 0; i < orderConfirmObj.getProduct_details().size(); i++) {
            totalPrice = totalPrice + Integer.parseInt(orderConfirmObj.getProduct_details().get(i).getPurchase_price());
        }
        txtTotalAmount.setText("₹ " + totalPrice);
        addCustomAdapter();
    }

    private void buttonOnClicks() {
        txtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationToUserActivity.this, Home.class);
                startActivity(intent);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationToUserActivity.this, Home.class);
                startActivity(intent);
            }
        });
    }

    private void addDummyData() {
        for (int i = 1; i <= 10; i++) {
            list.add("Example " + i);
        }
    }

    private void addCustomAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(orderConfirmObj.getProduct_details(), orderConfirmObj);
        recyclerView.setAdapter(adapter);
    }

    private void initializeViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtContinue = (TextView) findViewById(R.id.txtContinue);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        txtDeliveryAmount = (TextView) findViewById(R.id.txtDeliveryAmount);
        imgConfirmationScreen = (ImageView) findViewById(R.id.imgConfirmationScreen);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }
}
