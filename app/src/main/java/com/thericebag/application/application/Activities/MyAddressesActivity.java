package com.thericebag.application.application.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thericebag.application.R;
import com.thericebag.application.application.beans.AddressBean;
import com.thericebag.application.application.beans.UserProfileAPIBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class MyAddressesActivity extends AppCompatActivity {

    private RecyclerView recyclerMyAddresses;

    private Context mContext = MyAddressesActivity.this;

    private String profileDetails;

    private ImageView backIcon;

    private RelativeLayout relativeAddAddress;

    private List<AddressBean> addressList;

    private UserProfileAPIBean userProfileObj;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();   // Hide Tool bar from layout

        setContentView(R.layout.activity_my_addresses);

        Utility.changeStatusbarColor(this, "#54ad54");  // Change Status bar color to green

        initializeViews();

        buttonOnClicks();
//        addDummyData();

        getMyAddresses();

    }

    private void buttonOnClicks() {

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        relativeAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditAddress.class);
                intent.putExtra(AppConstants.POSITION, position);
                startActivity(intent);
            }
        });

    }

    private void setAddressBean() {

        addressList = new ArrayList<>();

        AddressBean addressBeanObj = new AddressBean();

        if (userProfileObj.getProfile().getAddress1_line1() != null && userProfileObj.getProfile().getAddress1_line1().length() > 0) {

            addressBeanObj.setName(userProfileObj.getProfile().getAddress1_name());

            addressBeanObj.setAddress_line1(userProfileObj.getProfile().getAddress1_line1());

            addressBeanObj.setAddress_line2(userProfileObj.getProfile().getAddress1_line2());

            addressBeanObj.setPhoneNumber(userProfileObj.getProfile().getPhone());

            addressBeanObj.setLandmark(userProfileObj.getProfile().getAddress1_landmark());

            addressBeanObj.setZip(userProfileObj.getProfile().getAddress1_zip());

            addressBeanObj.setCity(userProfileObj.getProfile().getAddress1_city());

            addressBeanObj.setState(userProfileObj.getProfile().getAddress1_state());

            addressBeanObj.setCountry(userProfileObj.getProfile().getAddress1_country());

            addressList.add(addressBeanObj);

            position = 2;

        } else {

            position = 1;

        }

        if (userProfileObj.getProfile().getAddress2_line1() != null && userProfileObj.getProfile().getAddress2_line1().length() > 0) {

            addressBeanObj = new AddressBean();

            addressBeanObj.setName(userProfileObj.getProfile().getAddress2_name());

            addressBeanObj.setAddress_line1(userProfileObj.getProfile().getAddress2_line1());

            addressBeanObj.setAddress_line2(userProfileObj.getProfile().getAddress2_line2());

            addressBeanObj.setPhoneNumber(userProfileObj.getProfile().getAddress2_phone());

            addressBeanObj.setLandmark(userProfileObj.getProfile().getAddress2_landmark());

            addressBeanObj.setZip(userProfileObj.getProfile().getAddress2_zip());

            addressBeanObj.setCity(userProfileObj.getProfile().getAddress2_city());

            addressBeanObj.setState(userProfileObj.getProfile().getAddress2_state());

            addressBeanObj.setCountry(userProfileObj.getProfile().getAddress2_country());

            addressList.add(addressBeanObj);
        }

        if (userProfileObj.getProfile().getAddress1_line1() == null || userProfileObj.getProfile().getAddress1_line1().length() == 0
                || userProfileObj.getProfile().getAddress2_line1() == null || userProfileObj.getProfile().getAddress2_line1().length() == 0) {

            relativeAddAddress.setVisibility(View.VISIBLE);

        }
    }

    private void getMyAddresses() {

        if (getIntent().getExtras() != null) {

            profileDetails = getIntent().getExtras().getString(AppConstants.PROFILE_DETAILS);

            Gson gson = new Gson();

            userProfileObj = gson.fromJson(profileDetails, UserProfileAPIBean.class);

            setAddressBean();

            setAdapter();

        } else {

            Toast.makeText(mContext, "No Data to show", Toast.LENGTH_SHORT).show();

        }

    }

    private void setAdapter() {

        MyAddressesAdapter myAddressesAdapter = new MyAddressesAdapter(mContext, addressList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        recyclerMyAddresses.setLayoutManager(mLayoutManager);

        recyclerMyAddresses.setItemAnimator(new DefaultItemAnimator());

        recyclerMyAddresses.setAdapter(myAddressesAdapter);

    }

    private void initializeViews() {

        recyclerMyAddresses = (RecyclerView) findViewById(R.id.recyclerMyAddresses);

        relativeAddAddress = (RelativeLayout) findViewById(R.id.relativeAddAddress);

        backIcon = (ImageView) findViewById(R.id.backIcon);

    }


    //---------------------------------Recyler Adapter class--------------------------------------//
    public class MyAddressesAdapter extends RecyclerView.Adapter<MyAddressesAdapter.MyViewHolder> {

        private List<AddressBean> addressBeanArrayList = new ArrayList<>();

        public MyAddressesAdapter(Context context, List<AddressBean> addressBeanArrayList) {

            this.addressBeanArrayList = addressBeanArrayList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listview_myaddress_layout, parent, false);

            return new MyViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            if (addressBeanArrayList.get(position) != null) {

                if (addressBeanArrayList.get(position).getAddress_line1() != null) {

                    holder.txtName.setText(addressBeanArrayList.get(position).getName());

                    holder.txtAddress1.setText(addressBeanArrayList.get(position).getAddress_line1() + ", "
                            + addressBeanArrayList.get(position).getAddress_line2() + ", "
                            + addressBeanArrayList.get(position).getCity() + ", "
                            + addressBeanArrayList.get(position).getZip() + ", "
                            + addressBeanArrayList.get(position).getCountry() + ", "
                            + addressBeanArrayList.get(position).getPhoneNumber());

                }

            }

            holder.textView_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, EditAddress.class);

                    String addressDetailsString = new Gson().toJson(addressBeanArrayList.get(position));

                    intent.putExtra(AppConstants.ADDRESS_DETAILS, addressDetailsString);

                    intent.putExtra(AppConstants.POSITION, position);

                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return addressBeanArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView txtAddress1;

            public TextView textView_edit;

            public TextView txtName;

            public MyViewHolder(View view) {

                super(view);

                txtAddress1 = (TextView) view.findViewById(R.id.txtAddress1);

                textView_edit = (TextView) view.findViewById(R.id.textView_edit);

                txtName = (TextView) view.findViewById(R.id.txtName);

            }

        }

    }

}
