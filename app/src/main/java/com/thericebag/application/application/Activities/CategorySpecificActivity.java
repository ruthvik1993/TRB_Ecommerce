package com.thericebag.application.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.beans.ProductBean;
import com.thericebag.application.application.beans.SubCategoryBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CategorySpecificActivity extends Activity {

    ListView listViewCategories;
    String[] test = {"one", "Two"};
    ImageView backIcon, imgKart;
    TextView txtCategory;
    ImageView imgCall;
    int id;
    String cat_name;
    ProgressDialog progressDialog;
    ArrayList<ProductBean> subCategoryList = new ArrayList<ProductBean>();

    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);
        Utility.changeStatusbarColor(this, "#54ad54");
        mContext = CategorySpecificActivity.this;
        progressDialog = new ProgressDialog(mContext);
        Utility.StartTrackingScreen(mContext, "2", "CategorySpecific Screen");
        //analyticsFunction();
        initializeViews();

        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getInt(AppConstants.ID);
            cat_name = getIntent().getExtras().getString(AppConstants.CATEGORYNAME);
            txtCategory.setText(cat_name);
            getSubCategoryDetails();
        }
        buttonClicks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        imgCall.setClickable(true);
    }

    private void getSubCategoryDetails() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.GETSUBCATEGORIES + "cat_id=" + id, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                progressDialog.dismiss();
                if (response.isSuccess()) {
                    Gson gson = new Gson();
                    SubCategoryBean subCategoryBeanObj = gson.fromJson(response.getResponseBody(), SubCategoryBean.class);
                    subCategoryList = subCategoryBeanObj.getSubCategories_list();
                    CustomListAdapter adapter = new CustomListAdapter(CategorySpecificActivity.this, subCategoryList);
                    listViewCategories.setAdapter(adapter);
                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            getSubCategoryDetails();
                        }
                    });

                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
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

    private void addDummyData() {
        ProductBean obj = new ProductBean();
        obj.setId(1);
        obj.setAct_price(1500);
        obj.setBest_seller(0);
        obj.setDescription("testing purpose Descriptin");
        obj.setNumberOfItems(1);
        obj.setOff_price(1400);
        obj.setQuantity(1);
        obj.setShort_desc("short description");
        obj.setTitle("Kurnool Sona Masoori");
        obj.setUnitPrice(50);
        obj.setWeight(25);

        subCategoryList.add(obj);

        obj = new ProductBean();
        obj.setId(1);
        obj.setAct_price(1500);
        obj.setBest_seller(0);
        obj.setDescription("testing purpose Descriptin");
        obj.setNumberOfItems(1);
        obj.setOff_price(1400);
        obj.setQuantity(1);
        obj.setShort_desc("short description");
        obj.setTitle("Kurnool Sona Masoori");
        obj.setUnitPrice(50);
        obj.setWeight(25);

        subCategoryList.add(obj);


        CustomListAdapter adapter = new CustomListAdapter(CategorySpecificActivity.this, subCategoryList);
        listViewCategories.setAdapter(adapter);
    }

    private void buttonClicks() {

        //-----------ListView Onlcick-------------//
        listViewCategories.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(CategorySpecificActivity.this, ProductDetailActivity.class);
                intent.putExtra(AppConstants.ID, subCategoryList.get(position).getId());
                intent.putExtra(AppConstants.TITLE, subCategoryList.get(position).getTitle());
                startActivity(intent);
            }
        });

        //----------Back Icon Onclick--------//
        backIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategorySpecificActivity.this, Home.class);
                startActivity(intent);
            }
        });

        //-------------------------Phone Call-------------//
        imgCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                launch();
//                requestforCallPhonePermission();
            }
        });

        imgKart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategorySpecificActivity.this, checkOutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        listViewCategories = (ListView) findViewById(R.id.listViewCategories);
        backIcon = (ImageView) findViewById(R.id.backIcon);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtCategory.setText("Kurnool Sona Masoori");
        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgCall.setClickable(true);
        imgKart = (ImageView) findViewById(R.id.imgKart);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //-----------------------Run Time Permissions-------------------------------------------------//
    private void requestforCallPhonePermission() {
        final String permission = android.Manifest.permission.CALL_PHONE;
        if (ContextCompat.checkSelfPermission(CategorySpecificActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CategorySpecificActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(CategorySpecificActivity.this, new String[]{permission},
                AppConstants.MY_PERMISSIONS_CALL);
    }

    private void launch() {
        imgCall.setClickable(false);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + AppConstants.PHONE_NUMBER));//change the number
        startActivity(callIntent);
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(CategorySpecificActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CategorySpecificActivity.this.requestForPermission(permission);
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
                    Toast.makeText(CategorySpecificActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class CustomListAdapter extends ArrayAdapter<SubCategoryBean> {

        private final Activity context;
        ArrayList<ProductBean> subCategoryList = new ArrayList<ProductBean>();

        public CustomListAdapter(Activity context, ArrayList<ProductBean> subCategoryList) {
            super(context, R.layout.listview_layout);
            this.context = context;
            this.subCategoryList = subCategoryList;
        }

        @Override
        public int getCount() {
            return subCategoryList.size();
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listview_layout, null, true);
            TextView txtProduct = (TextView) rowView.findViewById(R.id.txtProduct);
            TextView txtOurPrice = (TextView) rowView.findViewById(R.id.txtOurPrice);
            ImageView imgProduct = (ImageView) rowView.findViewById(R.id.productImage);
//			TextView txtKart = (TextView) rowView.findViewById(R.id.txtKart);
            RelativeLayout relativeBuyNow = (RelativeLayout) rowView.findViewById(R.id.relativeBuyNow);

            subCategoryList.get(position).setNumberOfItems(1);
            int unitPrice = subCategoryList.get(position).getNumberOfItems() * subCategoryList.get(position).getOff_price();
            subCategoryList.get(position).setUnitPrice(unitPrice);
            txtProduct.setText(subCategoryList.get(position).getTitle());
            txtOurPrice.setText("Offer price: ₹" + subCategoryList.get(position).getOff_price());
            List<String> productImages = Utility.seperateImages(subCategoryList.get(position).getProduct_image());
            if (subCategoryList.get(position).getProduct_image() != null) {
                Picasso.with(mContext)
                        .load(productImages.get(0))
                        .into(imgProduct);
            }
/*
            relativeBuyNow.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
//					saveInSharedPreference(position);
                    Intent intent = new Intent(CategorySpecificActivity.this, checkOutActivity.class);
                    startActivity(intent);
                }
            });*/

            return rowView;
        }

        protected void saveInSharedPreference(int position) {
            // Get Previous List that are added to Kart
            ArrayList<ProductBean> addToKartList = new ArrayList<ProductBean>();
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("addToKartString", null);
            Type type = new TypeToken<ArrayList<ProductBean>>() {
            }.getType();
            addToKartList = gson.fromJson(json, type);

            // Save the current Item to Kart
            addToKartList.add(subCategoryList.get(position));
            Editor editor = sharedPrefs.edit();
            gson = new Gson();
            String addToKartString = gson.toJson(addToKartList);
            editor.putString("addToKartString", addToKartString);
            editor.commit();
        }
    }

    //----------------------End Of Run Time Permissions------------------------------------------//
}
