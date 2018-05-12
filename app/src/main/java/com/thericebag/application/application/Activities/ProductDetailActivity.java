package com.thericebag.application.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.thericebag.application.R;
import com.thericebag.application.application.ChipCloudTags.ChipCloud;
import com.thericebag.application.application.ChipCloudTags.ChipListener;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.TRBApplication;
import com.thericebag.application.application.adapters.CustomImageAdapter;
import com.thericebag.application.application.beans.ProductBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductDetailActivity extends Activity {

    ViewPager viewPager;
    ImageView imgDot1, imgDot2, imgDot3, imgCall, backIcon, imgKart;
    TextView txtTrySample, txtDescription, txtOurPrice, txtRefund, txtProductName,
            txtOurPriceValue;
    TextView txtProductNameHeader;
    RelativeLayout relativeBuyNow, relativeAddToCart;
    String description, short_desc, title;
    int off_price, act_price, weight = 25, numberOfItems = 1, unitPrice;
    ProductBean productBeanObj = new ProductBean();
    RadioGroup radioGroup;
    Boolean isSampleSelected = false;
    Boolean canSelectSample;
    int perKgPrice = 0;
    String[] weightArray = null;
    String[] kgs = null;
    boolean fromSample = false;
    private TextView txtBuyNowTotalPrice;
    private Context mContext;
    private TextView txtAddToKart;
    private String email;
    private boolean isFirstTime = true;
    private ChipCloud select_weight;
    private int indexSelected = 0;
    private int id;
    private boolean isAddedToCart = false;
    private ProgressDialog progressdialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetail_layout);
        mContext = ProductDetailActivity.this;
        Utility.changeStatusbarColor(this, "#54ad54");
        progressdialog = new ProgressDialog(mContext);
        email = Utility.getEmail(mContext);
        Utility.StartTrackingScreen(mContext, "3", "ProductDetail Screen");


//        analyticsFunction();
        initializeViews();
        buttonOnClicks();

        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getInt(AppConstants.ID);
            String title = getIntent().getExtras().getString(AppConstants.TITLE);

//            txtProductNameHeader.setText(title);
            txtProductNameHeader.setText("Product Detail");
        }

    }

    @Override
    public void onBackPressed() {
      /*  TRBApplication application = (TRBApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();

        mTracker.setScreenName(null);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());*/
        super.onBackPressed();
    }

    /*private void analyticsFunction() {
        // Obtain the shared Tracker instance.
        TRBApplication application = (TRBApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();

        mTracker.setScreenName("Image~" + "PrductDetailActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());
    }*/

    private void setImageAdapter() {

        List<String> sliderImagesList = Arrays.asList(productBeanObj.getProduct_image().split(","));

        if (sliderImagesList.size() > 0) {
            PagerAdapter adapter = new CustomImageAdapter(mContext, sliderImagesList);
            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {
                    if (arg0 == 0) {
                        imgDot1.setImageResource(R.drawable.dot_filled);
                        imgDot2.setImageResource(R.drawable.dot_notfilled);
                        imgDot3.setImageResource(R.drawable.dot_notfilled);

                    } else if (arg0 == 1) {
                        imgDot1.setImageResource(R.drawable.dot_notfilled);
                        imgDot2.setImageResource(R.drawable.dot_filled);
                        imgDot3.setImageResource(R.drawable.dot_notfilled);
                    } else if (arg0 == 2) {
                        imgDot1.setImageResource(R.drawable.dot_notfilled);
                        imgDot2.setImageResource(R.drawable.dot_notfilled);
                        imgDot3.setImageResource(R.drawable.dot_filled);
                    } else if (arg0 == 3) {
                        imgDot1.setImageResource(R.drawable.dot_notfilled);
                        imgDot2.setImageResource(R.drawable.dot_notfilled);
                        imgDot3.setImageResource(R.drawable.dot_notfilled);
                    } else if (arg0 == 4) {
                        imgDot1.setImageResource(R.drawable.dot_notfilled);
                        imgDot2.setImageResource(R.drawable.dot_notfilled);
                        imgDot3.setImageResource(R.drawable.dot_notfilled);
                    }
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    System.out.print("1. " + arg0 + "\n arg1: " + arg1 + "\n arg2: " + arg2);

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    System.out.print("1. " + arg0);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        email = Utility.getEmail(mContext);
        getProductDetails(id);

        imgCall.setClickable(true);

    }

    private void getProductDetails(final int id) {
        productBeanObj = new ProductBean();
        progressdialog.setMessage("Getting product details...");
        progressdialog.setCancelable(false);
        progressdialog.show();
        if (email != null) {
            TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                    ApiConstants.ApiName.GETPRODUCTDETAILS + "id=" + id + "&email=" + email, new Response.Listener<TheRiceBagResponse>() {
                @Override
                public void onResponse(TheRiceBagResponse response) {
                    progressdialog.dismiss();
                    if (response.isSuccess()) {
                        Gson gson = new Gson();
                        productBeanObj = gson.fromJson(response.getResponseBody(), ProductBean.class);
                        productBeanObj.getDescription();
                        productBeanObj.setNumberOfItems(1); // Save One Item to cart.
                        canSelectSample = productBeanObj.getFirstSample();
                        updateDataInUI();
                        setImageAdapter();  // Set Product Images
                    } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                        final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                        networkError_dialog.showDialog(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                networkError_dialog.dismissDialog();
                                getProductDetails(id);
                            }
                        });

                    } else {
                        Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                    }
                }
            }, null);
            NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
        } else {
            TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                    ApiConstants.ApiName.GETPRODUCTDETAILS + "id=" + id, new Response.Listener<TheRiceBagResponse>() {
                @Override
                public void onResponse(TheRiceBagResponse response) {
                    progressdialog.dismiss();
                    if (response.isSuccess()) {
                        Gson gson = new Gson();
                        productBeanObj = gson.fromJson(response.getResponseBody(), ProductBean.class);
                        productBeanObj.getDescription();
                        productBeanObj.setNumberOfItems(1); // Save One Item to cart.
                        canSelectSample = false;
                        updateDataInUI();
                        setImageAdapter();  // Set Product Images
                    } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                        final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                        networkError_dialog.showDialog(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                networkError_dialog.dismissDialog();
                                getProductDetails(id);
                            }
                        });

                    } else {
                        Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                    }
                }
            }, null);
            NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
        }
    }


    private void updateDataInUI() {

        ArrayList<ProductBean> kartProductsList = new ArrayList<ProductBean>();
        kartProductsList = Utility.getKartProducts(mContext);

        if (isProductAdded(kartProductsList)) {
            isAddedToCart = true;
            txtAddToKart.setText("Go to Cart");
        } else {
            txtAddToKart.setText("Add To Basket");
        }

        txtProductName.setText(productBeanObj.getTitle());
        perKgPrice = Utility.getperKGPrice(productBeanObj.getOff_price(), productBeanObj.getWeight());
//        txtMrktPriceValue.setText("₹" + productBeanObj.getAct_price());
        txtBuyNowTotalPrice.setText("(₹" + productBeanObj.getOff_price() + ")");
        txtDescription.setText(Html.fromHtml("" + productBeanObj.getDescription()));
        txtRefund.setText(Html.fromHtml(productBeanObj.getRefund_policy()));

        if (email == null) {
            txtTrySample.setText(AppConstants.LOGIN_TO_SAMPLE);
        } else {
            txtTrySample.setText(AppConstants.TRY_SAMPLE);
        }

//        setRadioButtons();
        if (isFirstTime) {
            isFirstTime = false;
            txtOurPriceValue.setText("₹" + productBeanObj.getOff_price());
            setWeights();
        } else {
            select_weight.setSelectedChip(weightArray.length - 1);
        }

    }

    private boolean isProductAdded(ArrayList<ProductBean> kartProductsList) {
        if (kartProductsList != null) {
            for (int i = 0; i < kartProductsList.size(); i++) {
                if (kartProductsList.get(i).getId() == productBeanObj.getId()) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private void setWeights() {

        String packings = productBeanObj.getPackings();

        kgs = packings.split(",");

        weightArray = new String[kgs.length];
        ArrayList<String> weightsList = new ArrayList<String>();

//        final String[] kgs = {"1kg", "1kg", "1kg", "1kg", "1kg", "1kg", "1kg", "1kg"};

        for (int i = 0; i <= kgs.length - 1; i++) {
            weightArray[i] = kgs[i] + "kg";
//            weightsList.add(kgs[i] + "kg");
        }

        new ChipCloud.Configure()
                .chipCloud(select_weight)
                .deselectedColor(Color.parseColor("#FFFFFF"))
                .deselectedFontColor(Color.parseColor("#333333"))
//                .selectedColor(Color.parseColor("#e67442"))
//                .selectedFontColor(Color.parseColor("#ffffffff"))
                .labels(weightArray)
                .mode(ChipCloud.Mode.REQUIRED)
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {
                        isSampleSelected = false;
                        indexSelected = index;
                        int weight = Integer.parseInt(kgs[index]);
                        txtOurPriceValue.setText("₹" + (perKgPrice * weight));
                        txtBuyNowTotalPrice.setText("(₹" + (perKgPrice * weight) + ")");
                        txtTrySample.setBackgroundResource(R.drawable.layout_green_round_border);
                        txtTrySample.setTextColor(Color.parseColor("#5cba5c"));
                        productBeanObj.setWeight(weight);
                    }

                    @Override
                    public void chipDeselected(int index) {
                        /*if (!isSampleSelected) {
                            chipSelected(index);
                        }*/
                        // Toast.makeText(mContext, "Deselected: " + kgs[index], Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        select_weight.setSelectedChip(weightArray.length - 1);
    }

    /*private void setRadioButtons() {

        int count = 4;

        String packings = productBeanObj.getPackings();

        final String[] kgs = packings.split(",");
//        String part1 = parts[0]; // 004
//        String part2 = parts[1]; // 034556

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 0, 0, 0);

        radioGroup = new RadioGroup(this);

        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 1; i <= kgs.length; i++) {

            rdbtn = new RadioButton(this);

            rdbtn.setId(i + 1);

            rdbtn.setText(kgs[i - 1] + "kg");

            rdbtn.setLayoutParams(params);

            rdbtn.setTextColor(ContextCompat.getColorStateList(mContext, R.drawable.rbtn_textcolor_selector));

            rdbtn.setBackgroundResource(R.drawable.radiobutton_weight_selector);

            rdbtn.setButtonDrawable(null);

            rdbtn.setChecked(true);

            final int weight = Integer.parseInt(kgs[i - 1]);


            txtOurPriceValue.setText("₹" + productBeanObj.getOff_price());
            rdbtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtOurPriceValue.setText("₹" + (perKgPrice * weight));
                    txtBuyNowTotalPrice.setText("(₹" + (perKgPrice * weight) + ")");
                    isSampleSelected = false;
                    txtTrySample.setBackgroundResource(R.drawable.layout_green_round_border);
                    txtTrySample.setTextColor(Color.parseColor("#5cba5c"));
                }
            });

            radioGroup.addView(rdbtn);
        }

//        ((ViewGroup) findViewById(R.id.radioGroupWeight)).addView(radioGroup);

    }*/

    private void buttonOnClicks() {

        txtTrySample.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email != null) {
                    if (!isSampleSelected) {
                        if (canSelectSample) {
                            isSampleSelected = true;
                            fromSample = true;
                            select_weight.setDeSelectChip(indexSelected);
//                          setChipSelection(100);
//                          select_weight.chipDeselected(indexSelected);
                            txtTrySample.setBackgroundResource(R.drawable.layout_green_round);
                            txtTrySample.setTextColor(Color.parseColor("#FFFFFF"));
                            txtOurPriceValue.setText("₹" + perKgPrice);
                            txtBuyNowTotalPrice.setText("(₹" + perKgPrice + ")");
                            productBeanObj.setWeight(1);
                        } else {
                            final Snackbar snackBar = Snackbar.make(relativeBuyNow, AppConstants.SAMPLE_ALREADY_SELECTED, Snackbar.LENGTH_INDEFINITE);

                            snackBar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackBar.dismiss();
                                }
                            });
                            View yourSnackBarView = snackBar.getView();
                            TextView textView = (TextView) yourSnackBarView.findViewById(android.support.design.R.id.snackbar_text); //Get reference of snackbar textview
                            textView.setMaxLines(3);
                            snackBar.show();
                        }
                    } else {
                        isSampleSelected = false;
                        txtTrySample.setBackgroundResource(R.drawable.layout_green_round_border);
                        txtTrySample.setTextColor(Color.parseColor("#5cba5c"));
                    }
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra(AppConstants.FROM_ACTIVITY, AppConstants.FROM_PRODUCTDETAIL_ACTIVITY);
                    startActivity(intent);
                }
            }
        });

        imgKart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, checkOutActivity.class);
                startActivity(intent);
            }
        });

        backIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                launch();
                //requestforCallPhonePermission();
            }
        });

        relativeBuyNow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                productBeanObj.setUnitPrice(perKgPrice * productBeanObj.getWeight());
                productBeanObj.setNumberOfItems(1);
                Utility.addCartProduct(productBeanObj, mContext);

                Intent intent = new Intent(ProductDetailActivity.this, checkOutActivity.class);
                startActivity(intent);
            }
        });

        relativeAddToCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddedToCart) {
                    Intent intent = new Intent(ProductDetailActivity.this, checkOutActivity.class);
                    startActivity(intent);
                } else {
                    productBeanObj.setUnitPrice(perKgPrice * productBeanObj.getWeight());
                    productBeanObj.setNumberOfItems(1);
                    Utility.addCartProduct(productBeanObj, mContext);

                    Snackbar.make(v, AppConstants.ADDEDTOKART, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    // ------------ Change to Go to Cart-------------- //
                    isAddedToCart = true;
                    txtAddToKart.setText("Go to Cart");
                }
            }
        });
    }

    private void initializeViews() {
        imgDot1 = (ImageView) findViewById(R.id.imgDot1);
        imgDot2 = (ImageView) findViewById(R.id.imgDot2);
        imgDot3 = (ImageView) findViewById(R.id.imgDot3);

        imgKart = (ImageView) findViewById(R.id.imgKart);
        backIcon = (ImageView) findViewById(R.id.backIcon);
        txtOurPriceValue = (TextView) findViewById(R.id.txtOurPriceValue);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgCall.setClickable(true);
        viewPager = (ViewPager) findViewById(R.id.pager);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtRefund = (TextView) findViewById(R.id.txtRefund);
        txtOurPrice = (TextView) findViewById(R.id.txtOurPrice);
        txtBuyNowTotalPrice = (TextView) findViewById(R.id.txtBuyNowTotalPrice);
        txtAddToKart = (TextView) findViewById(R.id.txtAddToKart);
        txtTrySample = (TextView) findViewById(R.id.txtTrySample);
        txtProductNameHeader = (TextView) findViewById(R.id.txtProductNameHeader);
        relativeBuyNow = (RelativeLayout) findViewById(R.id.relativeBuyNow);
        relativeAddToCart = (RelativeLayout) findViewById(R.id.relativeAddToCart);
        select_weight = (ChipCloud) findViewById(R.id.select_weight);

    }


    //-----------------------Run Time Permissions-------------------------------------------------//
    private void requestforCallPhonePermission() {
        final String permission = android.Manifest.permission.CALL_PHONE;
        if (ContextCompat.checkSelfPermission(ProductDetailActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProductDetailActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(ProductDetailActivity.this, new String[]{permission},
                AppConstants.MY_PERMISSIONS_CALL);
    }

    private void launch() {
        imgCall.setClickable(false);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + AppConstants.PHONE_NUMBER));//change the number
        startActivity(callIntent);
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(ProductDetailActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProductDetailActivity.this.requestForPermission(permission);
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
                    Toast.makeText(ProductDetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    //----------------------End Of Run Time Permissions------------------------------------------//
}
