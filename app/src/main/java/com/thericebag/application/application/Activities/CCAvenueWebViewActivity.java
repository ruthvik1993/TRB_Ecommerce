package com.thericebag.application.application.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Response;
import com.avenues.lib.utility.AvenuesParams;
import com.avenues.lib.utility.Constants;
import com.avenues.lib.utility.RSAUtility;
import com.avenues.lib.utility.ServiceHandler;
import com.avenues.lib.utility.ServiceUtility;
import com.thericebag.application.R;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.utility.AppConstants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.TextUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EncodingUtils;

public class CCAvenueWebViewActivity extends Activity {
    Intent mainIntent;
    String html, encVal;
    private ProgressDialog dialog;
    private Context mContext;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mContext = CCAvenueWebViewActivity.this;
        setContentView(R.layout.activity_ccavenue);
        mainIntent = getIntent();

        // Calling async task to get display content
        new RenderView().execute();
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }

    private void updateOrderAPI() {
        String checkOutBean = mainIntent.getStringExtra(AvenuesParams.ORDER_DETAILS);
//        new Gson().fromJson(checkOutBean, CheckOutBean.class);
        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.POST,
                ApiConstants.ApiName.UPDATE_ORDER, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                if (response.isSuccess()) {
                    Intent intent = new Intent(CCAvenueWebViewActivity.this, ConfirmationToUserActivity.class);
                    intent.putExtra(AppConstants.ORDER_NUMBER, mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }, checkOutBean);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(CCAvenueWebViewActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

            // Send Extra Parameters - Billing Address and Shipping Address
            params.add(new BasicNameValuePair(AvenuesParams.BILLING_NAME, mainIntent.getStringExtra(AvenuesParams.BILLING_NAME)));
            params.add(new BasicNameValuePair(AvenuesParams.BILLING_ADDRESS, mainIntent.getStringExtra(AvenuesParams.BILLING_ADDRESS)));
            params.add(new BasicNameValuePair(AvenuesParams.BILLING_CITY, mainIntent.getStringExtra(AvenuesParams.BILLING_CITY)));
            params.add(new BasicNameValuePair(AvenuesParams.BILLING_STATE, mainIntent.getStringExtra(AvenuesParams.BILLING_STATE)));
            params.add(new BasicNameValuePair(AvenuesParams.BILLING_ZIP, mainIntent.getStringExtra(AvenuesParams.BILLING_ZIP)));
            params.add(new BasicNameValuePair(AvenuesParams.BILLING_COUNTRY, mainIntent.getStringExtra(AvenuesParams.BILLING_COUNTRY)));
            params.add(new BasicNameValuePair(AvenuesParams.BILLING_TEL, mainIntent.getStringExtra(AvenuesParams.BILLING_TEL)));
            params.add(new BasicNameValuePair(AvenuesParams.BILLING_EMAIL, mainIntent.getStringExtra(AvenuesParams.BILLING_EMAIL)));

            params.add(new BasicNameValuePair(AvenuesParams.DELIVERY_NAME, mainIntent.getStringExtra(AvenuesParams.DELIVERY_NAME)));
            params.add(new BasicNameValuePair(AvenuesParams.DELIVERY_ADDRESS, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ADDRESS)));
            params.add(new BasicNameValuePair(AvenuesParams.DELIVERY_CITY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_CITY)));
            params.add(new BasicNameValuePair(AvenuesParams.DELIVERY_STATE, mainIntent.getStringExtra(AvenuesParams.DELIVERY_STATE)));
            params.add(new BasicNameValuePair(AvenuesParams.DELIVERY_ZIP, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ZIP)));
            params.add(new BasicNameValuePair(AvenuesParams.DELIVERY_COUNTRY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_COUNTRY)));
            params.add(new BasicNameValuePair(AvenuesParams.DELIVERY_TEL, mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL)));
            params.add(new BasicNameValuePair(AvenuesParams.DELIVERY_EMAIL, mainIntent.getStringExtra(AvenuesParams.DELIVERY_EMAIL)));

            // Extra parameters to CCAVENUE
            /*params.add(new BasicNameValuePair(AvenuesParams.DT, mainIntent.getStringExtra(AvenuesParams.DT)));
            params.add(new BasicNameValuePair(AvenuesParams.BASE_PRICE, mainIntent.getStringExtra(AvenuesParams.BASE_PRICE)));
            params.add(new BasicNameValuePair(AvenuesParams.DISC_KG, mainIntent.getStringExtra(AvenuesParams.DISC_KG)));
            params.add(new BasicNameValuePair(AvenuesParams.DISC_TYPE, mainIntent.getStringExtra(AvenuesParams.DISC_TYPE)));
            params.add(new BasicNameValuePair(AvenuesParams.DISC_PR_ID, mainIntent.getStringExtra(AvenuesParams.DISC_PR_ID)));
            params.add(new BasicNameValuePair(AvenuesParams.EXPECTED, mainIntent.getStringExtra(AvenuesParams.EXPECTED)));
            params.add(new BasicNameValuePair(AvenuesParams.FIRST_ORDER, mainIntent.getStringExtra(AvenuesParams.FIRST_ORDER)));
            params.add(new BasicNameValuePair(AvenuesParams.DISC_PRICE, mainIntent.getStringExtra(AvenuesParams.DISC_PRICE)));
            params.add(new BasicNameValuePair(AvenuesParams.DISC_FULLY_PRD, mainIntent.getStringExtra(AvenuesParams.DISC_FULLY_PRD)));*/


            String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));

                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (dialog.isShowing())
                dialog.dismiss();

            if (!(encVal != null && !encVal.isEmpty())) {
                Toast.makeText(mContext, "Something went wrong. Please contact TheRiceBag or select the mode 'cash on delivery' to proceed", Toast.LENGTH_LONG).show();
                onBackPressed();
            } else {

                @SuppressWarnings("unused")
                class MyJavaScriptInterface {
                    @JavascriptInterface
                    public void processHTML(String html) {
                        // process the html as needed by the app
                        String status = null;
                        if (html.indexOf("Failure") != -1) {
                            status = "Transaction Declined!";
                        } else if (html.indexOf("Success") != -1) {
                            status = "Transaction Successful!";
                        } else if (html.indexOf("Aborted") != -1) {
                            status = "Transaction Cancelled!";
                        } else {
                            status = "Status Not Known!";
                        }

                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ConfirmationToUserActivity.class);
                        intent.putExtra(AppConstants.STATUS, status);
                        startActivity(intent);
                    }
                }

                final WebView webview = (WebView) findViewById(R.id.webview);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
                webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(webview, url);

                        if (url.equalsIgnoreCase("https://www.thericebag.com/services/ccavenue/ccavResponseHandler.php")) {
                            updateOrderAPI();
                        }

                    /*if (url.indexOf("/ccavResponseHandler.jsp") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }*/
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    Intent intent = new Intent(CCAvenueWebViewActivity.this, ConfirmationToUserActivity.class);
//                    startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                    }
                });

			/* An instance of this class will be registered as a JavaScript interface    */
                StringBuffer params = new StringBuffer();
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));


                // Send Extra Parameters - Billing Address and Shipping Address
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME, mainIntent.getStringExtra(AvenuesParams.BILLING_NAME)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ADDRESS, mainIntent.getStringExtra(AvenuesParams.BILLING_ADDRESS)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY, mainIntent.getStringExtra(AvenuesParams.BILLING_CITY)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE, mainIntent.getStringExtra(AvenuesParams.BILLING_STATE)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP, mainIntent.getStringExtra(AvenuesParams.BILLING_ZIP)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY, mainIntent.getStringExtra(AvenuesParams.BILLING_COUNTRY)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL, mainIntent.getStringExtra(AvenuesParams.BILLING_TEL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL, mainIntent.getStringExtra(AvenuesParams.BILLING_EMAIL)));

                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_NAME, mainIntent.getStringExtra(AvenuesParams.DELIVERY_NAME)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ADDRESS, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ADDRESS)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_CITY)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_STATE, mainIntent.getStringExtra(AvenuesParams.DELIVERY_STATE)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ZIP, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ZIP)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_COUNTRY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_COUNTRY)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_TEL, mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_EMAIL, mainIntent.getStringExtra(AvenuesParams.DELIVERY_EMAIL)));

            /*params.append(ServiceUtility.addToPostParams(AvenuesParams.DT, mainIntent.getStringExtra(AvenuesParams.DT)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BASE_PRICE, mainIntent.getStringExtra(AvenuesParams.BASE_PRICE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DISC_KG, mainIntent.getStringExtra(AvenuesParams.DISC_KG)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DISC_TYPE, mainIntent.getStringExtra(AvenuesParams.DISC_TYPE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DISC_PR_ID, mainIntent.getStringExtra(AvenuesParams.DISC_PR_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.EXPECTED, mainIntent.getStringExtra(AvenuesParams.EXPECTED)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.FIRST_ORDER, mainIntent.getStringExtra(AvenuesParams.FIRST_ORDER)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DISC_PRICE, mainIntent.getStringExtra(AvenuesParams.DISC_PRICE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DISC_FULLY_PRD, mainIntent.getStringExtra(AvenuesParams.DISC_FULLY_PRD)));*/

                params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));

                String vPostParams = "";
                if (params != null && !TextUtils.isEmpty(params)) {
                    vPostParams = params.substring(0, params.length() - 1);
                }
                try {
                    webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
                } catch (Exception e) {
                    showToast("Exception occured while processing your payment request. Please check and try again");
                }
            }
        }
    }
}