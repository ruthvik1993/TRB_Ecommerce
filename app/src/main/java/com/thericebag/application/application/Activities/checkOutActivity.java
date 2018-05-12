package com.thericebag.application.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.avenues.lib.utility.AvenuesParams;
import com.avenues.lib.utility.ServiceUtility;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.adapters.PagerAdapter;
import com.thericebag.application.application.beans.AddressBean;
import com.thericebag.application.application.beans.CheckOutBean;
import com.thericebag.application.application.beans.OfferBean;
import com.thericebag.application.application.beans.PaymentBean;
import com.thericebag.application.application.beans.PrepareCartBean;
import com.thericebag.application.application.beans.ProductBean;
import com.thericebag.application.application.beans.ShippingCharges;
import com.thericebag.application.application.beans.UserProfileBean;
import com.thericebag.application.application.fragments.OffersFragment;
import com.thericebag.application.application.fragments.SlotsFragment;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.AppUtil;
import com.thericebag.application.application.utility.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class checkOutActivity extends AppCompatActivity {

    public String disc_fully_product;
    private ArrayList<AddressBean> addressBeenList = new ArrayList<AddressBean>();
    private ListView listViewCheckOutProducts;
    private LinearLayout linearExpectedTime;
    private TextView txtSubTotalValue;
    private TextView txtTotalValue;
    private ImageView imgCall;
    private ImageView backIcon;
    private TextView txtContinue;
    private TextView txtDiscountChargesValue;
    private TextView txtDeliveryChargesValue;
    private int deliveryCharge = 0;
    private ArrayList<ProductBean> kartProductsList = new ArrayList<ProductBean>();
    private ArrayList<ProductBean> kartProductsListFromPreference = new ArrayList<ProductBean>();
    private int discountAvailed = 0;
    private int totalSellingPrice = 0;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Context mContext = checkOutActivity.this;
    private PaymentBean paymentBeanObj;
    private int totalAmount;
    private ViewPagerAdapter viewPagerAdapter;
    private String addressSelected;
    private ProgressDialog progressDialog;
    private String email;
    private ArrayList<OfferBean> offersObjList;
    private CustomProductsListAdapter adapter;
    private PrepareCartBean cartBeanObj;
    private boolean isCOD = false;
    private RelativeLayout relativeNoProducts;
    private RelativeLayout relativeProducts;
    private TextView txtStartShopping;
    private boolean isAddressSelected = false;
    private int disc_kg, disc_type, disc_price, disc_pr_id;
    private String expectedTime = "";
    private boolean isSlotsVisited = false;
    private int addressSelectedposition = 100, addAddressPosition = 100;
    private boolean isAddressShownOnce = false;
    private boolean isLessQty = false, isSameDay = false;
    private ShippingCharges shippingCharges = new ShippingCharges();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_check_out);

        progressDialog = new ProgressDialog(mContext);
        Utility.changeStatusbarColor(this, "#54ad54");
        email = Utility.getEmail(mContext);
        Utility.StartTrackingScreen(mContext, "4", "Check-Out Screen");

//        analyticsFunction();
        initializeViews();

//		addData();


        buttonOnClicks();
        getFromSharedPreference();

    }

    @Override
    protected void onResume() {
        super.onResume();
        imgCall.setClickable(true);


    }

   /* private void analyticsFunction() {
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

    private void getFromSharedPreference() {
        kartProductsList = new ArrayList<ProductBean>();
        kartProductsList = Utility.getKartProducts(mContext);
        kartProductsListFromPreference = kartProductsList;

        if (kartProductsList != null && kartProductsList.size() > 0) {
            relativeProducts.setVisibility(View.VISIBLE);
            relativeNoProducts.setVisibility(View.GONE);
            updateDataFromService();
        } else {
            relativeProducts.setVisibility(View.GONE);
            relativeNoProducts.setVisibility(View.VISIBLE);
        }
    }

    private void updateDataFromService() {
        String productsAppend = "";
        for (int i = 0; i < kartProductsList.size(); i++) {
            if (i == 0) {
                productsAppend = kartProductsList.get(i).getId() + "";
            } else {
                productsAppend = productsAppend + "," + kartProductsList.get(i).getId();
            }
        }

        progressDialog.setMessage("Preparing your Cart");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //Update Amount and offers from server
        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.UPDATECHECKOUTPRODUCTS + "products_String=" + productsAppend + "&email=" + email, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                if (response.isSuccess()) {
                    progressDialog.dismiss();
                    cartBeanObj = new Gson().fromJson(response.getResponseBody().toString(), PrepareCartBean.class);
                    kartProductsList = cartBeanObj.getProduct_details();
                    offersObjList = new ArrayList<OfferBean>();
                    offersObjList = cartBeanObj.getOffer_details();
                    shippingCharges = cartBeanObj.getShippingCharges();
                    updateCartProductsAndOffers();
                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            updateDataFromService();
                        }
                    });

                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
    }

    private void updateCartProductsAndOffers() {

        for (int i = 0; i < kartProductsList.size(); i++) {
            kartProductsList.get(i).setUnitPrice(kartProductsListFromPreference.get(i).getUnitPrice());
            kartProductsList.get(i).setWeight(kartProductsListFromPreference.get(i).getWeight());
            kartProductsList.get(i).setNumberOfItems(kartProductsListFromPreference.get(i).getNumberOfItems());
        }

        // ---------------------------List Adapter----------------------------------//
        adapter = new CustomProductsListAdapter(checkOutActivity.this, kartProductsList);
        listViewCheckOutProducts.setAdapter(adapter);

        addFooterView();

        totalSellingPrice = 0;
        for (int i = 0; i < kartProductsList.size(); i++) {
            totalSellingPrice = (kartProductsList.get(i).getNumberOfItems() * kartProductsList.get(i).getUnitPrice()) + totalSellingPrice;
        }

        updateTotalAmount();
    }

	/*private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

		Bundle bundle = new Bundle();
		OffersFragment offersFragment = new OffersFragment();
		offersFragment.setArguments(bundle);

		Bundle paymentBundle = new Bundle();
		SlotsFragment slotsFragment = new SlotsFragment();
		slotsFragment.setArguments(paymentBundle);

		adapter.addFragment(offersFragment, "Offers");
		adapter.addFragment(slotsFragment, "Delivery");
		viewPager.setAdapter(adapter);
	}*/

    private void addFooterView() {
        View footerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_check_out_footer, null, false);
        listViewCheckOutProducts.addFooterView(footerView);
        txtDeliveryChargesValue = (TextView) footerView.findViewById(R.id.txtDeliveryChargesValue);
        txtSubTotalValue = (TextView) footerView.findViewById(R.id.txtSubTotalValue);
        txtTotalValue = (TextView) footerView.findViewById(R.id.txtTotalValue);
        txtDiscountChargesValue = (TextView) footerView.findViewById(R.id.txtDiscountChargesValue);
        final TextView textOfferSelected = (TextView) footerView.findViewById(R.id.textOfferSelected);
        RadioGroup radioGroupPaymentMode = (RadioGroup) footerView.findViewById(R.id.radioGroupPaymentMode);

        radioGroupPaymentMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioCOD) {
                    isCOD = true;
                } else {
                    isCOD = false;
                }
            }
        });

        tabLayout = (TabLayout) footerView.findViewById(R.id.tabs);
        viewPager = (ViewPager) footerView.findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont();
    }

    private void changeTabsFont() {
        Typeface gothamMedium = Typeface.createFromAsset(getAssets(), "fonts/roboto_medium.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(gothamMedium, Typeface.NORMAL);

                }
            }
        }
    }

    private void buttonOnClicks() {
        /*listViewCheckOutProducts.setOnItemClickListener(new OnItemClickListener() {
            @Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mContext, ProductDetailActivity.class);
				intent.putExtra(AppConstants.ID, kartProductsList.get(position).getId());
				intent.putExtra(AppConstants.TITLE, kartProductsList.get(position).getTitle());
				startActivity(intent);
			}
		});*/

        txtStartShopping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        txtContinue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (email != null) {
                    if (isSlotsVisited) {
                        popupAddress(email);
                    } else {
                        viewPager.setCurrentItem(1);
                        setSlotsVisited();
                    }
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra(AppConstants.FROM_ACTIVITY, AppConstants.FROM_CHECKOUT_ACTIVITY);
                    startActivity(intent);
                }
            }
        });

        imgCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                launch();
                //requestforCallPhonePermission();
            }
        });

        backIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setSlotsVisited() {
        isSlotsVisited = true;
        txtContinue.setText("Continue to Pay Rs." + totalAmount);
    }

    private boolean validate() {
        boolean flag = true;
        if (!isAddressSelected) {
            flag = false;
        }
        return flag;
    }

    protected void popupAddress(String email) {
        isAddressSelected = false;

        final Dialog addressDialogue = new Dialog(checkOutActivity.this, android.R.style.Theme_Black_NoTitleBar);
        addressDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        addressDialogue.setContentView(R.layout.address_dialogue);
        // addressDialogue.setCancelable(false);
        addressDialogue.show();
        ListView listView = (ListView) addressDialogue.findViewById(R.id.listView);
        Button txtDone = (Button) addressDialogue.findViewById(R.id.txtDone);
        RelativeLayout relativeAddAddress = (RelativeLayout) addressDialogue.findViewById(R.id.relativeAddAddress);
        ImageView imgEditAddress = (ImageView) addressDialogue.findViewById(R.id.imgEditAddress);

        addressBeenList = new ArrayList<AddressBean>();

        if (cartBeanObj.getAddress().getAddress1_line1() != null && cartBeanObj.getAddress().getAddress1_line1().length() > 0) {
            addAddressPosition = 1;
            AddressBean addressObj = new AddressBean();
            UserProfileBean profileBean = cartBeanObj.getAddress();
            addressObj.setAddress_line1(profileBean.getAddress1_line1());
            addressObj.setAddress_line2(profileBean.getAddress1_line2());
            addressObj.setCountry(profileBean.getAddress1_country());
            addressObj.setState(profileBean.getAddress1_state());
            addressObj.setZip(profileBean.getAddress1_zip());
            addressObj.setCity(profileBean.getAddress1_city());
            addressObj.setName(profileBean.getAddress1_name());
            addressObj.setLandmark(profileBean.getAddress1_landmark());
            addressObj.setPhoneNumber(profileBean.getAddress1_phone());
            addressObj.setSelected(false);
            addressBeenList.add(addressObj);

        }

        if (cartBeanObj.getAddress().getAddress2_line1() != null && cartBeanObj.getAddress().getAddress2_line1().length() > 0) {
            addAddressPosition = 2;
            AddressBean addressObj = new AddressBean();
            UserProfileBean profileBean = cartBeanObj.getAddress();
            addressObj.setAddress_line1(profileBean.getAddress2_line1());
            addressObj.setAddress_line2(profileBean.getAddress2_line2());
            addressObj.setCountry(profileBean.getAddress2_country());
            addressObj.setState(profileBean.getAddress2_state());
            addressObj.setZip(profileBean.getAddress2_zip());
            addressObj.setCity(profileBean.getAddress2_city());
            addressObj.setName(profileBean.getAddress2_name());
            addressObj.setLandmark(profileBean.getAddress2_landmark());
            addressObj.setPhoneNumber(profileBean.getAddress2_phone());
            addressObj.setSelected(false);
            addressBeenList.add(addressObj);

        }

        if (cartBeanObj.getAddress().getAddress1_line1() == null || cartBeanObj.getAddress().getAddress2_line1() == null
                || cartBeanObj.getAddress().getAddress1_line1().length() == 0 || cartBeanObj.getAddress().getAddress2_line1().length() == 0) {
            relativeAddAddress.setVisibility(View.VISIBLE);
        }


//        if (!isAddressShownOnce) {
        isAddressShownOnce = true;
        CustomAdapter adapter = new CustomAdapter(this, addressBeenList);
        listView.setAdapter(adapter);
//        } else {
////            adapter.notifyDataSetChanged();
//        }
       /* listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                isAddressSelected = true;
                addressSelected = addressBeenList.get(position).getName() +
                        addressBeenList.get(position).getAddress_line1() +
                        addressBeenList.get(position).getAddress_line2() +
                        addressBeenList.get(position).getCity() +
                        addressBeenList.get(position).getZip() +
                        addressBeenList.get(position).getCountry();
            }
        });*/
//		TextView txtDone = (TextView) addressDialogue.findViewById(R.id.txtDone);
//		TextView txtEdit = (TextView) addressDialogue.findViewById(R.id.txtEdit);
        RelativeLayout relativeMain = (RelativeLayout) addressDialogue.findViewById(R.id.relativeMain);

        relativeMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addressDialogue.dismiss();
            }
        });

		/*txtEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(checkOutActivity.this, EditAddress.class);
				startActivity(intent);
			}
		});*/

        txtDone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validate()) {
                    subscribeNotification();
                    if (isCOD) {
                        cashOnDelivery();
                    } else {
                        paymentActivity();
                    }
                } else {
                    Toast.makeText(mContext, "Please select Address to continue", Toast.LENGTH_SHORT).show();
                }
            }
        });

        relativeAddAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditAddress.class);
                intent.putExtra(AppConstants.POSITION, addAddressPosition);
                startActivity(intent);
                addressDialogue.dismiss();
            }
        });

        imgEditAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressSelectedposition == 100) {
                    Toast.makeText(mContext, "Please select address to contiinue", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, EditAddress.class);
                    intent.putExtra(AppConstants.ADDRESS_DETAILS, new Gson().toJson(addressBeenList.get(addressSelectedposition), AddressBean.class));
                    intent.putExtra(AppConstants.POSITION, addressSelectedposition);
                    startActivity(intent);
                }
            }
        });

    }

    private void subscribeNotification() {
        for (int i = 0; i < kartProductsList.size(); i++) {
            if (kartProductsList.get(i).getTitle().contains("kurnool")) {
                AppUtil.createMessaginGroup(AppConstants.KURNOOL_SONA);
            }
            if (kartProductsList.get(i).getTitle().contains("HMT")) {
                AppUtil.createMessaginGroup(AppConstants.HMT);
            }
            if (kartProductsList.get(i).getTitle().contains("basmati")) {
                AppUtil.createMessaginGroup(AppConstants.BASMATI);
            }
            if (kartProductsList.get(i).getTitle().contains("Idly")) {
                AppUtil.createMessaginGroup(AppConstants.IDLY);
            }
            if (kartProductsList.get(i).getTitle().contains("organic")) {
                AppUtil.createMessaginGroup(AppConstants.ORGANIC);
            }
            if (kartProductsList.get(i).getTitle().contains("bpt")) {
                AppUtil.createMessaginGroup(AppConstants.BPT);
            }
            if (kartProductsList.get(i).getTitle().contains("single")) {
                AppUtil.createMessaginGroup(AppConstants.SINGLE_POLISHED);
            }
            if (kartProductsList.get(i).getTitle().contains("unpolished")) {
                AppUtil.createMessaginGroup(AppConstants.UNPOLISHED);
            }

        }
    }

    private void cashOnDelivery() {

        progressDialog.setMessage("Placing Order");
        progressDialog.setCancelable(false);
        progressDialog.show();

        CheckOutBean checkOutObj = setDataToObj();     // Update checkOutObject to place order

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.POST,
                ApiConstants.ApiName.PLACE_ORDER, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                progressDialog.dismiss();
                if (response.isSuccess()) {
                    Intent intent = new Intent(mContext, ConfirmationToUserActivity.class);
                    intent.putExtra(AppConstants.ORDER_NUMBER, response.getResponseBody().toString());
                    startActivity(intent);
                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            cashOnDelivery();
                        }
                    });

                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Gson().toJson(checkOutObj, CheckOutBean.class).toString());//checkOutObj.toString());

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);

    }

    public void setDeliveryExpectedTime(String expectedTime, boolean isSameDay) {
        this.expectedTime = expectedTime;
        this.isSameDay = isSameDay;
        if (isSameDay) {
            Snackbar.make(relativeProducts, "Delivery Charges applicable for same day delivery", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        setShippingCharge();    //Set Shipping Charge
        updateTotalAmount();
    }

    private CheckOutBean setDataToObj() {

        CheckOutBean checkOutBeanObj = new CheckOutBean();

        Date date = new Date();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        checkOutBeanObj.setDt(format.format(date));

        checkOutBeanObj.setShippingaddress(addressSelected);

        checkOutBeanObj.setBase_price(Integer.toString(totalSellingPrice));

        checkOutBeanObj.setBillingadd(addressSelected);

        checkOutBeanObj.setPhone(cartBeanObj.getAddress().getPhone());

        checkOutBeanObj.setDisc_kg(disc_kg);

        checkOutBeanObj.setDisc_type(disc_type);

        checkOutBeanObj.setDisc_pr_id(disc_pr_id);

        checkOutBeanObj.setPurchase_price(Integer.toString(totalAmount));

//        checkOutBeanObj.setExpected("27-10-2015&nbsp;12:00PM to 4:00PM");

        checkOutBeanObj.setExpected(expectedTime);

        checkOutBeanObj.setEmail(email);

        checkOutBeanObj.setFirst_order("0");    // TODO update first order from server

        checkOutBeanObj.setDisc_price(disc_price);

        checkOutBeanObj.setDisc_fully_prd(disc_fully_product);

        checkOutBeanObj.setProducts(kartProductsList);

        checkOutBeanObj.setDel_charges(deliveryCharge);

        return checkOutBeanObj;

    }

    private void paymentActivity() {
        String uid = Utility.getId(mContext);

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.GETPAYMENTDETAILS + "uid=" + uid, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                if (response.isSuccess()) {
                    Gson gson = new Gson();
                    paymentBeanObj = gson.fromJson(response.getResponseBody(), PaymentBean.class);
                    System.out.println(paymentBeanObj.getAccessCode());
                    updateAddress();    // Update Billing address and Shipping Address
                    proceedPayment(paymentBeanObj.getAccessCode(), paymentBeanObj.getMerchantId(), paymentBeanObj.getCurrency(),
                            paymentBeanObj.getRedirectURL(), paymentBeanObj.getCancelURL(), paymentBeanObj.getRsaKeyUrl(),
                            paymentBeanObj.getOrder_number());
                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            paymentActivity();
                        }
                    });

                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);

    }

    private void updateAddress() {

        paymentBeanObj.setBilling_address(addressSelected);//addressBeenList.get(addressSelectedposition).getAddress_line1());
        paymentBeanObj.setBilling_city(addressBeenList.get(addressSelectedposition).getCity());
        paymentBeanObj.setBilling_country("India");
        paymentBeanObj.setBilling_email(email);
        paymentBeanObj.setBilling_name(addressBeenList.get(addressSelectedposition).getName());
        paymentBeanObj.setBilling_state("Telangana");
        paymentBeanObj.setBilling_tel(addressBeenList.get(addressSelectedposition).getPhoneNumber());
        paymentBeanObj.setBilling_zip(addressBeenList.get(addressSelectedposition).getZip());

        paymentBeanObj.setDelivery_address(addressSelected); //paymentBeanObj.getBilling_address());
        paymentBeanObj.setDelivery_city(paymentBeanObj.getBilling_city());
        paymentBeanObj.setDelivery_country(paymentBeanObj.getBilling_country());
        paymentBeanObj.setDelivery_email(paymentBeanObj.getBilling_email());
        paymentBeanObj.setDelivery_name(paymentBeanObj.getBilling_name());
        paymentBeanObj.setDelivery_state(paymentBeanObj.getBilling_state());
        paymentBeanObj.setDelivery_tel(paymentBeanObj.getBilling_tel());
        paymentBeanObj.setDelivery_zip(paymentBeanObj.getBilling_zip());

    }

    private void proceedPayment(String accessCode, String merchantId, String currency, String redirectURL,
                                String cancelURL, String rsaKeyUrl, String orderId) {

        CheckOutBean checkOutBeanObj = setDataToObj();

        checkOutBeanObj.setOrderNumber(orderId);

//        totalAmount = 1;
        //Mandatory parameters. Other parameters can be added if required.
        String vAccessCode = ServiceUtility.chkNull(accessCode).toString().trim();
        String vMerchantId = ServiceUtility.chkNull(merchantId).toString().trim();
        String vCurrency = ServiceUtility.chkNull(currency).toString().trim();
        String vAmount = ServiceUtility.chkNull(checkOutBeanObj.getPurchase_price()).toString().trim();
        if (!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
            Intent intent = new Intent(this, CCAvenueWebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId).toString().trim());
            intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId).toString().trim());
            intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency).toString().trim());
            intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(checkOutBeanObj.getPurchase_price()).toString().trim());

            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectURL).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelURL).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl).toString().trim());

            intent.putExtra(AvenuesParams.BILLING_NAME, ServiceUtility.chkNull(paymentBeanObj.getBilling_name()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_ADDRESS, ServiceUtility.chkNull(paymentBeanObj.getBilling_address()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_CITY, ServiceUtility.chkNull(paymentBeanObj.getBilling_city()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_STATE, ServiceUtility.chkNull(paymentBeanObj.getBilling_state()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_ZIP, ServiceUtility.chkNull(paymentBeanObj.getBilling_zip()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_COUNTRY, ServiceUtility.chkNull(paymentBeanObj.getBilling_country()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_TEL, ServiceUtility.chkNull(paymentBeanObj.getBilling_tel()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_EMAIL, ServiceUtility.chkNull(paymentBeanObj.getBilling_email()).toString().trim());

            intent.putExtra(AvenuesParams.DELIVERY_NAME, ServiceUtility.chkNull(paymentBeanObj.getDelivery_name()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, ServiceUtility.chkNull(paymentBeanObj.getDelivery_address()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_CITY, ServiceUtility.chkNull(paymentBeanObj.getDelivery_city()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_STATE, ServiceUtility.chkNull(paymentBeanObj.getDelivery_state()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_ZIP, ServiceUtility.chkNull(paymentBeanObj.getDelivery_zip()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, ServiceUtility.chkNull(paymentBeanObj.getDelivery_country()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_TEL, ServiceUtility.chkNull(paymentBeanObj.getDelivery_tel()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_EMAIL, ServiceUtility.chkNull(paymentBeanObj.getDelivery_email()).toString().trim());


            intent.putExtra(AvenuesParams.ORDER_DETAILS, new Gson().toJson(checkOutBeanObj, CheckOutBean.class));

          /*  intent.putExtra(AvenuesParams.DT, ServiceUtility.chkNull(checkOutBeanObj.getDt()).toString().trim());
            intent.putExtra(AvenuesParams.BASE_PRICE, ServiceUtility.chkNull(checkOutBeanObj.getBase_price()).toString().trim());
            intent.putExtra(AvenuesParams.DISC_KG, ServiceUtility.chkNull(checkOutBeanObj.getDisc_kg()).toString().trim());
            intent.putExtra(AvenuesParams.DISC_TYPE, ServiceUtility.chkNull(checkOutBeanObj.getDisc_type()).toString().trim());
            intent.putExtra(AvenuesParams.DISC_PR_ID, ServiceUtility.chkNull(checkOutBeanObj.getDisc_pr_id()).toString().trim());
            intent.putExtra(AvenuesParams.EXPECTED, ServiceUtility.chkNull(checkOutBeanObj.getExpected()).toString().trim());
            intent.putExtra(AvenuesParams.FIRST_ORDER, ServiceUtility.chkNull(checkOutBeanObj.getFirst_order()).toString().trim());
            intent.putExtra(AvenuesParams.DISC_PRICE, ServiceUtility.chkNull(checkOutBeanObj.getDisc_price()).toString().trim());
            intent.putExtra(AvenuesParams.DISC_FULLY_PRD, ServiceUtility.chkNull(checkOutBeanObj.getDisc_fully_prd()).toString().trim());*/


            startActivity(intent);
        } else {
            Toast.makeText(checkOutActivity.this, "All parameters are mandatory.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews() {
        txtContinue = (TextView) findViewById(R.id.txtContinue);
        listViewCheckOutProducts = (ListView) findViewById(R.id.listViewCheckOutProducts);
        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgCall.setClickable(true);
        backIcon = (ImageView) findViewById(R.id.backIcon);
        relativeNoProducts = (RelativeLayout) findViewById(R.id.relativeNoProducts);
        relativeProducts = (RelativeLayout) findViewById(R.id.relativeProducts);
        txtStartShopping = (TextView) findViewById(R.id.txtStartShopping);

        txtContinue.setText("Select Time Slot");
    }

    public void updateDeliveryCharge() {
        if (txtDeliveryChargesValue != null) {
            if (deliveryCharge > 0) {
                txtDeliveryChargesValue.setBackgroundColor(Color.parseColor("#FFFFFF"));
                txtDeliveryChargesValue.setTextSize(12);
                txtDeliveryChargesValue.setTextColor(Color.parseColor("#8b8a8a"));
                txtDeliveryChargesValue.setText("₹" + deliveryCharge);
            } else {
                txtDeliveryChargesValue.setBackgroundColor(Color.parseColor("#e67442"));
                txtDeliveryChargesValue.setTextColor(Color.parseColor("#FFFFFF"));
                txtDeliveryChargesValue.setText("FREE SHIPPING");
            }
        }
    }

    public void setShippingCharge() {
        deliveryCharge = 0;
        if (isLessQty) {
            deliveryCharge = Integer.valueOf(shippingCharges.getDeliver_charges());
        }
        if (isSameDay) {
            deliveryCharge = deliveryCharge + Integer.valueOf(shippingCharges.getExpress_del_chrgs());
        }
    }

    public void updateTotalAmount() {
        if (shippingCharges != null && shippingCharges.getCart_val() != null && totalSellingPrice < Integer.valueOf(shippingCharges.getCart_val())) {
            isLessQty = true;
        } else {
            isLessQty = false;
        }
        setShippingCharge();
        updateDeliveryCharge();
        txtDiscountChargesValue.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txtDiscountChargesValue.setText("₹ " + discountAvailed + "");
        txtSubTotalValue.setText("₹ " + Integer.toString(totalSellingPrice));
        totalAmount = totalSellingPrice - discountAvailed + deliveryCharge;
        txtTotalValue.setText("₹" + Integer.toString(totalAmount));

        txtContinue.setText("Continue to Pay Rs." + totalAmount);
    }

    public void updateOfferProduct(ProductBean productBean) {
        disc_price = 0;
        disc_type = 2;
        discountAvailed = 0;
        if (productBean != null) {
            disc_kg = productBean.getWeight();
            disc_pr_id = productBean.getId();
//            disc_fully_product = productBean.getTitle();
            kartProductsList.add(productBean);
            adapter.notifyDataSetChanged();
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        listViewCheckOutProducts.smoothScrollToPosition(0); // Scroll listview to top after adding product
    }

    public void removeOfferProduct(ProductBean productBean, String discount, int type) {
        disc_type = type;
        disc_fully_product = "";
        disc_pr_id = 0;
        disc_kg = 0;
        if (discount != null) {
            disc_price = Integer.parseInt(discount);
        }
        if (productBean != null) {
            kartProductsList.remove(productBean);
            adapter.notifyDataSetChanged();
            if (discount != null) {
                discountAvailed = Integer.parseInt(discount);
            } else {
                discountAvailed = 0;
            }
            updateTotalAmount();
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //-----------------------Run Time Permissions-------------------------------------------------//
    private void requestforCallPhonePermission() {
        final String permission = android.Manifest.permission.CALL_PHONE;
        if (ContextCompat.checkSelfPermission(checkOutActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(checkOutActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(checkOutActivity.this, new String[]{permission},
                AppConstants.MY_PERMISSIONS_CALL);
    }

    private void launch() {
        imgCall.setClickable(false);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + AppConstants.PHONE_NUMBER));//change the number
        startActivity(callIntent);
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(checkOutActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkOutActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_CALL:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launch();
                } else {
                    Toast.makeText(checkOutActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                Bundle offersBundle = new Bundle();
                OffersFragment offersFragment = new OffersFragment();
                offersBundle.putSerializable(AppConstants.OFFERS_DETAILS, offersObjList);
                offersFragment.setArguments(offersBundle);
                return offersFragment;
            } else {
                Bundle slotsBundle = new Bundle();
                SlotsFragment slotsFragment = new SlotsFragment();
                slotsFragment.setArguments(slotsBundle);
                return slotsFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Offers";
            } else {
                return "Delivery";
            }
        }
    }

    public class CustomProductsListAdapter extends ArrayAdapter<ProductBean> {

        private final Activity context;
        ArrayList<ProductBean> kartProductsList = new ArrayList<ProductBean>();
        private List<String> captionList = new ArrayList<String>();

        public CustomProductsListAdapter(Activity context, ArrayList<ProductBean> kartProductsList) {
            super(context, R.layout.listview_check_out, kartProductsList);
            this.context = context;
            this.kartProductsList = kartProductsList;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            final Holder holder = new Holder();
            View rowView = inflater.inflate(R.layout.checkoutlistview_layout, null, true);

            //-------------------------Product Card--------------------------------------------//
            holder.imgDown = (ImageView) rowView.findViewById(R.id.imgDown);
            holder.imgUp = (ImageView) rowView.findViewById(R.id.imgUp);
            holder.txtNumberOfItems = (TextView) rowView.findViewById(R.id.txtNumberOfItems);
            holder.imgClose = (ImageView) rowView.findViewById(R.id.imgClose);
            holder.txtProductTitle = (TextView) rowView.findViewById(R.id.txtProductTitle);
            holder.txtWeight = (TextView) rowView.findViewById(R.id.txtWeight);
            holder.txtOurPrice = (TextView) rowView.findViewById(R.id.txtOurPrice);
            holder.relativeProductCard = (RelativeLayout) rowView.findViewById(R.id.relativeProductCard);
            holder.imgProduct = (ImageView) rowView.findViewById(R.id.capturedImage);
            holder.card_view_product = (CardView) rowView.findViewById(R.id.card_view_product);

            //--------------------------Offer Product Card-------------------------------------//
            holder.card_view_offer = (CardView) rowView.findViewById(R.id.card_view_offer);
            holder.txtOfferProductTitle = (TextView) rowView.findViewById(R.id.txtOfferProductTitle);
            holder.txtOfferWeight = (TextView) rowView.findViewById(R.id.txtOfferWeight);
            holder.txtOfferPrice = (TextView) rowView.findViewById(R.id.txtOfferPrice);

            if (!kartProductsList.get(position).isOfferProduct()) {
                holder.card_view_product.setVisibility(View.VISIBLE);
                holder.card_view_offer.setVisibility(View.GONE);


                if (position == kartProductsList.size() - 1) {
                    totalSellingPrice = 0;
                    for (int i = 0; i < kartProductsList.size(); i++) {
                        totalSellingPrice = (kartProductsList.get(i).getNumberOfItems() * kartProductsList.get(i).getUnitPrice()) + totalSellingPrice;
                    }
                    updateTotalAmount();
                }

                int unitPrice = kartProductsList.get(position).getNumberOfItems() * kartProductsList.get(position).getUnitPrice();

                if (kartProductsList.get(position).getProduct_image() != null) {
                    List<String> productImage = Utility.seperateImages(kartProductsList.get(position).getProduct_image());
                    Picasso.with(mContext)
                            .load(productImage.get(0))
                            .into(holder.imgProduct);
                } else {
                    holder.imgProduct.setImageResource(R.drawable.ricebag_sample);
                }

                if (kartProductsList.size() > 0) {
                    if (kartProductsList.get(position).getNumberOfItems() == 0) {
                        kartProductsList.get(position).setNumberOfItems(1);
                    }
                    int productPrice = kartProductsList.get(position).getNumberOfItems() * kartProductsList.get(position).getOff_price();
                    holder.txtOurPrice.setText("Rs." + unitPrice);
                    if (kartProductsList.get(position).getWeight() == 0) {
                        holder.txtWeight.setText("Weight: 25kgs");
                    } else {
                        holder.txtWeight.setText("Weight: " + Integer.toString(kartProductsList.get(position).getWeight()) + "kgs");
                    }

                    holder.txtProductTitle.setText(kartProductsList.get(position).getTitle());

                    if (kartProductsList.get(position).getNumberOfItems() == 0) {
                        holder.txtNumberOfItems.setText("1");
                    } else {
                        holder.txtNumberOfItems.setText(Integer.toString(kartProductsList.get(position).getNumberOfItems()));
                    }

                    holder.imgClose.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Snackbar.make(v, AppConstants.REMOVEDFROMKART, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            Utility.removeKartProduct(kartProductsList.get(position), mContext, position);
                            kartProductsList.remove(position);
                            if (kartProductsList.size() == 0) {
                                relativeProducts.setVisibility(View.GONE);
                                relativeNoProducts.setVisibility(View.VISIBLE);
                            } else {
                                totalSellingPrice = 0;
                                for (int i = 0; i < kartProductsList.size(); i++) {
                                    totalSellingPrice = kartProductsList.get(i).getUnitPrice() + totalSellingPrice;
                                }
                                updateTotalAmount();
                                notifyDataSetChanged();
                            }
                        }
                    });

                    holder.imgUp.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            kartProductsList.get(position).setNumberOfItems(kartProductsList.get(position).getNumberOfItems() + 1);
                            saveInSharedPreference(kartProductsList);
                            totalSellingPrice = 0;
                            for (int i = 0; i < kartProductsList.size(); i++) {
                                totalSellingPrice = (kartProductsList.get(i).getNumberOfItems() * kartProductsList.get(i).getUnitPrice()) + totalSellingPrice;
                            }
//                            Utility.updateCartProduct(kartProductsList.get(position), mContext);
                            updateTotalAmount();
                            notifyDataSetChanged();
                        }
                    });

                    holder.imgDown.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (kartProductsList.get(position).getNumberOfItems() > 1) {
                                kartProductsList.get(position).setNumberOfItems(kartProductsList.get(position).getNumberOfItems() - 1);
                                totalSellingPrice = 0;
                                for (int i = 0; i < kartProductsList.size(); i++) {
                                    totalSellingPrice = (kartProductsList.get(i).getNumberOfItems() * kartProductsList.get(i).getUnitPrice()) + totalSellingPrice;
                                }
                                updateTotalAmount();
                                saveInSharedPreference(kartProductsList);
                                notifyDataSetChanged();
                            }
                        }
                    });

                    holder.txtProductTitle.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openProduct(position);
                        }
                    });

                    holder.imgProduct.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openProduct(position);
                        }
                    });

                    /*holder.relativeProductCard.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ProductDetailActivity.class);
                            intent.putExtra(AppConstants.ID, kartProductsList.get(position).getId());
                            intent.putExtra(AppConstants.TITLE, kartProductsList.get(position).getTitle());
                            startActivity(intent);
                        }
                    });*/
                }
            } else {
                holder.card_view_product.setVisibility(View.GONE);
                holder.card_view_offer.setVisibility(View.VISIBLE);

                holder.txtOfferProductTitle.setText(kartProductsList.get(position).getTitle());
                holder.txtOfferWeight.setText("Weight: " + kartProductsList.get(position).getWeight() + "kgs");
                holder.txtOfferPrice.setText("Rs. " + kartProductsList.get(position).getOff_price());

                totalSellingPrice = 0;
                for (int i = 0; i < kartProductsList.size(); i++) {
                    totalSellingPrice = (kartProductsList.get(i).getNumberOfItems() * kartProductsList.get(i).getUnitPrice()) + totalSellingPrice;
                }
                updateTotalAmount();
            }

            return rowView;
        }

        private void openProduct(int position) {
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra(AppConstants.ID, kartProductsList.get(position).getId());
            intent.putExtra(AppConstants.TITLE, kartProductsList.get(position).getTitle());
            startActivity(intent);
        }

        protected void saveInSharedPreference(ArrayList<ProductBean> kartProductsList) {
            // Get Previous List that are added to Kart
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();
            String addToKartString = gson.toJson(kartProductsList);
            editor.putString("addToKartString", addToKartString);
            editor.commit();
        }

        public class Holder {
            //-----------------------Normal Products-------------------------------//
            RelativeLayout relativeProductCard;
            TextView txtNumberOfItems, txtProductTitle, txtWeight, txtOurPrice;
            ImageView imgClose, imgDown, imgUp, imgProduct;
            CardView card_view_product;

            //------------------------Offer Products--------------------------------//
            CardView card_view_offer;
            TextView txtOfferProductTitle, txtOfferWeight, txtOfferPrice;
        }
    }

    public class CustomAdapter extends BaseAdapter {
        ArrayList<AddressBean> addressBeanArrayList = new ArrayList<AddressBean>();
        Context context;
        private LayoutInflater inflater = null;

        public CustomAdapter(checkOutActivity mainActivity, ArrayList<AddressBean> addressBeanArrayList) {
            this.addressBeanArrayList.clear();
            this.addressBeanArrayList.addAll(addressBeanArrayList);
            context = mainActivity;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return addressBeanArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listaddresses_layout, null);

            holder.tick = (ImageView) rowView.findViewById(R.id.imgTick);
            holder.relativeMainBorder = (RelativeLayout) rowView.findViewById(R.id.relativeMainBorder);
            holder.txtName = (TextView) rowView.findViewById(R.id.txtName);
            holder.text_address = (TextView) rowView.findViewById(R.id.text_address);


            final String address_appended = addressBeanArrayList.get(position).getName() + ", "
                    + addressBeanArrayList.get(position).getAddress_line1() + ", "
                    + addressBeanArrayList.get(position).getAddress_line2() + ", "
                    + addressBeanArrayList.get(position).getLandmark() + ", "
                    + addressBeanArrayList.get(position).getZip() + ", "
                    + addressBeanArrayList.get(position).getCity() + ", "
                    + addressBeanArrayList.get(position).getPhoneNumber();
            address_appended.replace("null, ", "");
            holder.txtName.setText(addressBeanArrayList.get(position).getName());
            holder.text_address.setText(address_appended);

            if (!addressBeanArrayList.get(position).isSelected()) {
                holder.relativeMainBorder.setBackgroundResource(R.drawable.layout_border_address_notselected);
            } else {
                holder.tick.setImageResource(R.drawable.img_address_elected_tick);
                holder.relativeMainBorder.setBackgroundResource(R.drawable.layout_border_address_notselected);
                addressSelectedposition = position;
            }

            rowView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    selectAddress(position, address_appended);
                }
            });

            return rowView;
        }

        protected void selectAddress(int position, String address_appended) {
            addressSelected = address_appended;
            isAddressSelected = true;
            if (!addressBeanArrayList.get(position).isSelected()) {
                for (int i = 0; i < addressBeanArrayList.size(); i++) {
                    addressBeanArrayList.get(i).setSelected(false);
                }
                addressBeanArrayList.get(position).setSelected(true);
            } else {
                addressBeanArrayList.get(position).setSelected(false);
            }
            notifyDataSetChanged();
        }

        public class Holder {
            TextView txtName, text_address;
            ImageView tick;
            RelativeLayout relativeMainBorder;
        }

    }
    //----------------------End Of Run Time Permissions------------------------------------------//
}
