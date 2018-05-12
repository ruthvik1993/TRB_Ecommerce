package com.thericebag.application.application.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.thericebag.application.application.beans.AddressBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

//import com.eagleeye.library.bean.AddressBean;
//import com.eagleeye.library.receiver.BroadCastReceiverForGps.MyLocationListener;

public class EditAddress extends Activity {

    private LocationManager mlocManager;

    private MyLocationListener mlocListener;

    private AddressBean addressBeanObj;

    private TextInputLayout editInputLayoutName;

    private TextInputLayout editInputLayoutAddress_line1;

    private TextInputLayout editInputLayoutStreetAddress;

    private TextInputLayout editInputLayoutCity;

    private TextInputLayout editInputLayoutLandmark;

    private TextInputLayout editInputLayoutMobileNumber;

    private TextInputLayout editInputLayoutPinCode;

    private EditText editName;

    private EditText edit_address_line_one;

    private EditText edit_street_address;

    private EditText edit_city;

    private EditText edit_Landmark;

    private EditText edit_mobile_number;

    private EditText edit_pin_code;

    private Button txtDone;

    private int addressPosition;

    private Context mContext;

    private ProgressDialog progressDialog;

    private ImageView backIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_address);

        mContext = EditAddress.this;

        progressDialog = new ProgressDialog(mContext);

        initializeViews();

        buttonClicks();

        getData();

        startLocationListening();

    }

    private void getData() {
        if (getIntent().getExtras() != null) {

            String addressDetailsString = getIntent().getExtras().getString(AppConstants.ADDRESS_DETAILS);

            addressPosition = getIntent().getExtras().getInt(AppConstants.POSITION);

            Gson gson = new Gson();

            addressBeanObj = new AddressBean();

            if (addressDetailsString != null && addressDetailsString.length() > 0) {

                addressBeanObj = gson.fromJson(addressDetailsString, AddressBean.class);

            }

            setUIFields();
        }
    }

    private void setUIFields() {

        if (addressBeanObj.getName() != null && addressBeanObj.getName().length() > 0) {

            editName.setText(addressBeanObj.getName());

        }

        if (addressBeanObj.getLandmark() != null && addressBeanObj.getLandmark().length() > 0) {

            edit_Landmark.setText(addressBeanObj.getLandmark());

        }

        if (addressBeanObj.getPhoneNumber() != null && addressBeanObj.getPhoneNumber().length() > 0) {

            edit_mobile_number.setText(addressBeanObj.getPhoneNumber());

        }

        if (addressBeanObj.getAddress_line1() != null) {

            edit_address_line_one.setText(addressBeanObj.getAddress_line1());

        }
        if (addressBeanObj.getAddress_line2() != null) {


            edit_street_address.setText(addressBeanObj.getAddress_line2());

        }

        if (addressBeanObj.getCity() != null) {

            edit_city.setText(addressBeanObj.getCity());

        }

        if (addressBeanObj.getZip() != null) {

            edit_pin_code.setText(addressBeanObj.getZip());

        }

    }

    private void autoDetectAddressPopup(final String city, final String postalCode, final String address, String flat, final String area) {

        final Dialog autoDetectAddressDialogue = new Dialog(EditAddress.this, android.R.style.Theme_Black_NoTitleBar);

        autoDetectAddressDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));

        autoDetectAddressDialogue.setContentView(R.layout.address_detected_dialogue);

        TextView txtDetectedAddress = (TextView) autoDetectAddressDialogue.findViewById(R.id.txtDetectedAddress);

        RelativeLayout relativeSelect = (RelativeLayout) autoDetectAddressDialogue.findViewById(R.id.relativeSelect);

        txtDetectedAddress.setText(address);

        RelativeLayout relativeHeader = (RelativeLayout) autoDetectAddressDialogue.findViewById(R.id.relativeHeader);

        RelativeLayout relativeManually = (RelativeLayout) autoDetectAddressDialogue.findViewById(R.id.relativeManually);

        relativeManually.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                autoDetectAddressDialogue.dismiss();
            }
        });

        /*relativeHeader.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                autoDetectAddressDialogue.dismiss();

            }
        });*/

        relativeSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_street_address.setText(area);
                edit_city.setText(city);
                edit_pin_code.setText(postalCode);

                autoDetectAddressDialogue.dismiss();
            }
        });

        // addressDialogue.setCancelable(false);

        autoDetectAddressDialogue.show();
    }

    private void buttonClicks() {

        backIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    updateAddress();
                }
            }
        });
    }

    private boolean validate() {
        boolean flag = true;
        if (edit_address_line_one.getText().toString().trim().length() == 0) {
            editInputLayoutAddress_line1.setError(AppConstants.ERROR_ADDRESS_LINE1);
            flag = false;
        }
        if (edit_street_address.getText().toString().trim().length() == 0) {
            editInputLayoutStreetAddress.setError(AppConstants.ERROR_STREET_ADDRESS);
            flag = false;
        }
        if (editName.getText().toString().trim().length() == 0) {
            editInputLayoutName.setError(AppConstants.ERROR_NAME);
            flag = false;
        }
        if (edit_mobile_number.getText().toString().trim().length() == 0) {
            editInputLayoutMobileNumber.setError(AppConstants.ERROR_MOBILE_NUMBER);
            flag = false;
        }
        if (edit_city.getText().toString().trim().length() == 0) {
            editInputLayoutCity.setError(AppConstants.ERROR_CITY);
            flag = false;
        }
        if (edit_Landmark.getText().toString().trim().length() == 0) {
            editInputLayoutLandmark.setError(AppConstants.ERROR_LANDMARK);
            flag = false;
        }
        if (edit_pin_code.getText().toString().trim().length() == 0) {
            editInputLayoutPinCode.setError(AppConstants.ERROR_PINCODE);
            flag = false;
        }
        if (edit_mobile_number.getText().toString().trim().length() != 10) {
            flag = false;
            editInputLayoutMobileNumber.setError("Enter valid mobile number");
        }
        if (edit_pin_code.getText().toString().trim().length() < 6) {
            editInputLayoutPinCode.setError("Enter valid pin code");
            flag = false;
        }
        return flag;
    }

    private void updateAddress() {

        int position = 0;

        if (addressPosition == 100) {
            position = 1;
        } else {
            position = addressPosition + 1;
        }

        String address_line1 = URLEncoder.encode(edit_address_line_one.getText().toString().trim());

        String address_line2 = URLEncoder.encode(edit_street_address.getText().toString().trim());

        String address_name = URLEncoder.encode(editName.getText().toString().trim());

        String address_phone = URLEncoder.encode(edit_mobile_number.getText().toString().trim());

        String address_city = URLEncoder.encode(edit_city.getText().toString().trim());

        String address_landmark = URLEncoder.encode(edit_Landmark.getText().toString().trim());

        String address_zip = URLEncoder.encode(edit_pin_code.getText().toString().trim());

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,

                ApiConstants.ApiName.UPDATE_ADDRESS + "email=" + Utility.getEmail(mContext) + "&position=" + position

                        + "&address_line1=" + address_line1 + "&address_line2=" + address_line2

                        + "&address_name=" + address_name + "&address_phone=" + address_phone

                        + "&address_city=" + address_city + "&address_landmark=" + address_landmark

                        + "&address_zip=" + address_zip, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {

                progressDialog.dismiss();

                if (response.isSuccess()) {

                    updateUI();

                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);

                    networkError_dialog.showDialog(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            networkError_dialog.dismissDialog();

                            getData();

                        }

                    });

                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }

            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);

        progressDialog.setMessage(AppConstants.UPDATING_ADDRESS);

        progressDialog.setCancelable(false);

        progressDialog.show();
    }

    private void updateUI() {

        Intent intent = new Intent(mContext, EditProfileActivity.class);

        startActivity(intent);

        finish();

    }

    private void initializeViews() {

        backIcon = (ImageView) findViewById(R.id.backIcon);

        editName = (EditText) findViewById(R.id.editName);

        edit_address_line_one = (EditText) findViewById(R.id.edit_address_line_one);

        edit_street_address = (EditText) findViewById(R.id.edit_street_address);

        edit_city = (EditText) findViewById(R.id.edit_city);

        edit_Landmark = (EditText) findViewById(R.id.edit_Landmark);

        edit_mobile_number = (EditText) findViewById(R.id.edit_mobile_number);

        edit_pin_code = (EditText) findViewById(R.id.edit_pin_code);

        txtDone = (Button) findViewById(R.id.txtDone);

        editInputLayoutName = (TextInputLayout) findViewById(R.id.editInputLayoutName);

        editInputLayoutAddress_line1 = (TextInputLayout) findViewById(R.id.editInputLayoutAddress_line1);

        editInputLayoutStreetAddress = (TextInputLayout) findViewById(R.id.editInputLayoutStreetAddress);

        editInputLayoutCity = (TextInputLayout) findViewById(R.id.editInputLayoutCity);

        editInputLayoutLandmark = (TextInputLayout) findViewById(R.id.editInputLayoutLandmark);

        editInputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.editInputLayoutMobileNumber);

        editInputLayoutPinCode = (TextInputLayout) findViewById(R.id.editInputLayoutPinCode);

    }

    private void startLocationListening() {

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            showDialogGPS();
        } else {
            requestForLocationPermission();
        }
    }

    private void showDialogGPS() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public class MyLocationListener implements LocationListener {
        String city, postalCode, address, latitude, longitude, flat, area;

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(EditAddress.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                city = addresses.get(0).getLocality();
                postalCode = addresses.get(0).getPostalCode();
                flat = addresses.get(0).getAddressLine(0);
                area = addresses.get(0).getAddressLine(1);
                address = addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2);
                latitude = location.getLatitude() + "";
                longitude = location.getLongitude() + "";
                autoDetectAddressPopup(city, postalCode, address, flat, area);
                mlocManager.removeUpdates(mlocListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
//			dialogue.dismiss();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }
    }


    //-----------------------Run Time Permissions-------------------------------------------------//
    private void requestForLocationPermission() {
        final String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ContextCompat.checkSelfPermission(mContext, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditAddress.this, permission)) {
                showPermissionRationaleDialog("This app requires your location to auto-detect address", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(EditAddress.this, new String[]{permission},
                AppConstants.MY_PERMISSIONS_CALL);
    }

    private void launch() {
        mlocListener = new MyLocationListener();
        if (mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            mlocManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, mlocListener, null);
        } else if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mlocManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mlocListener, null);
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new android.app.AlertDialog.Builder(EditAddress.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditAddress.this.requestForPermission(permission);
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
//                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //----------------------End Of Run Time Permissions------------------------------------------//


    /*@Override
    protected void onResume() {
        super.onResume();
        startLocationListening();
    }*/
}
