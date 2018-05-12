package com.thericebag.application.application.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.beans.PreviousOrderBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerMyOrders;

    private TextView txtNoOrders;

    private ImageView backIcon, imgCall, imgKart;

    private Context mContext = MyOrdersActivity.this;

    private ArrayList<PreviousOrderBean> previousOrdersList = new ArrayList<>();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();   // Hide Tool bar from layout

        setContentView(R.layout.activity_my_orders);

        progressDialog = new ProgressDialog(mContext);

        Utility.changeStatusbarColor(this, "#54ad54");  // Change Status bar color to green

        initializeViews();

        onClicks();

//        addDummyData();

        getMyOrders();

    }

    private void onClicks() {

        backIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();

            }

        });

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch();
            }
        });

        imgKart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, checkOutActivity.class);
                startActivity(intent);
            }
        });

    }

    private void launch() {
        imgCall.setClickable(false);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + AppConstants.PHONE_NUMBER));//change the number
        startActivity(callIntent);
    }

    private void addDummyData() {

        PreviousOrderBean previousOrderObj = new PreviousOrderBean();

        previousOrderObj.setCard_type("NETBANKING");

        previousOrderObj.setDispatched_st("0");

        previousOrderObj.setDt("10-10-2016");

        previousOrderObj.setTitle("Kurnool Sona");

        previousOrdersList.add(previousOrderObj);

        previousOrderObj = new PreviousOrderBean();

        previousOrderObj.setCard_type("NETBANKING");

        previousOrderObj.setDispatched_st("0");

        previousOrderObj.setDt("10-10-2016");

        previousOrderObj.setTitle("Kurnool Sona");

        previousOrdersList.add(previousOrderObj);

    }

    private void getMyOrders() {

        String email = Utility.getEmail(mContext);

        progressDialog.setMessage("Loading...");

        progressDialog.setCancelable(false);

        progressDialog.show();

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,

                ApiConstants.ApiName.GET_MYORDERS + "email=" + email, new Response.Listener<TheRiceBagResponse>() {

            @Override
            public void onResponse(TheRiceBagResponse response) {

                progressDialog.dismiss();

                if (response.isSuccess()) {

                    Gson gson = new Gson();

                    previousOrdersList = gson.fromJson(response.getResponseBody(), new TypeToken<List<PreviousOrderBean>>() {
                    }.getType());

                    setAdapter();   // Set previous orders to list using reecyler view

                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            getMyOrders();
                        }
                    });

                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }

            }

        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);

    }

    private void setAdapter() {

        if (previousOrdersList.size() == 0) {

            txtNoOrders.setVisibility(View.VISIBLE);

            recyclerMyOrders.setVisibility(View.GONE);

        } else {

            txtNoOrders.setVisibility(View.GONE);

            recyclerMyOrders.setVisibility(View.VISIBLE);

            MyOrdersAdapter myOrdersAdapter = new MyOrdersAdapter(mContext, previousOrdersList);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

            recyclerMyOrders.setLayoutManager(mLayoutManager);

            recyclerMyOrders.setItemAnimator(new DefaultItemAnimator());

            recyclerMyOrders.setAdapter(myOrdersAdapter);

        }
    }

    private void initializeViews() {

        recyclerMyOrders = (RecyclerView) findViewById(R.id.recyclerMyOrders);

        backIcon = (ImageView) findViewById(R.id.backIcon);

        txtNoOrders = (TextView) findViewById(R.id.txtNoOrders);

        imgCall = (ImageView) findViewById(R.id.imgCall);

        imgKart = (ImageView) findViewById(R.id.imgKart);

    }


    //---------------------------------Recyler Adapter class--------------------------------------//
    public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {

        private ArrayList<PreviousOrderBean> previousOrdersList = new ArrayList<>();

        private Context mcontext;

        public MyOrdersAdapter(Context context, ArrayList<PreviousOrderBean> previousOrdersList) {

            this.previousOrdersList = previousOrdersList;

            this.mcontext = context;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listview_myorders_layout, parent, false);

            return new MyViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            holder.txtProductTitle.setText(previousOrdersList.get(position).getTitle());

            holder.txtOrderNumber.setText(previousOrdersList.get(position).getReference_no());

            holder.txtStatus.setText(previousOrdersList.get(position).getDispatched_st());

            holder.txtProductTitle.setText(previousOrdersList.get(position).getTitle());

            holder.txtPurchasePrice.setText("₹ " + previousOrdersList.get(position).getPurchase_price());

            holder.textQty.setText(previousOrdersList.get(position).getSizes_quantity());

            holder.txtCardType.setText(previousOrdersList.get(position).getCard_type());

            holder.txtOrderDate.setText(previousOrdersList.get(position).getDt());

        }

        @Override
        public int getItemCount() {
            return previousOrdersList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView txtOrderNumber;

            public TextView txtStatus;

            public TextView txtProductTitle;

            public TextView txtPurchasePrice;

            public TextView textQty;

            public TextView txtCardType;

            public TextView txtOrderDate;

            public MyViewHolder(View view) {

                super(view);

                txtOrderNumber = (TextView) view.findViewById(R.id.txtOrderNumber);

                txtStatus = (TextView) view.findViewById(R.id.txtStatus);

                txtProductTitle = (TextView) view.findViewById(R.id.txtProductTitle);

                txtPurchasePrice = (TextView) view.findViewById(R.id.txtPurchasePrice);

                textQty = (TextView) view.findViewById(R.id.textQty);

                txtCardType = (TextView) view.findViewById(R.id.txtCardType);

                txtOrderDate = (TextView) view.findViewById(R.id.txtOrderDate);

            }

        }

    }

}
