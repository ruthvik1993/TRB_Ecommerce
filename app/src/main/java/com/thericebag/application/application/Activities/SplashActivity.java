package com.thericebag.application.application.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.SmsMessage;
import android.view.Window;
import android.widget.Toast;

import com.thericebag.application.R;
import com.thericebag.application.application.fragments.WelcomeScreenFragment;
import com.thericebag.application.application.utility.Utility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class SplashActivity extends FragmentActivity {

    static final int TOTAL_PAGES = 3;
    static final String INTENT_FILTER_FOR_SMS = "android.provider.Telephony.SMS_RECEIVED";
    // Logcat tag
    private static final String TAG = "MainActivity";
    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    static String randomCode;
    ViewPager pager;
    final CountDownTimer splashtimer = new CountDownTimer(3000, 3000) {

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
        }
    };
    PagerAdapter pagerAdapter;
    SharedPreferences prefs;
    long startTime;
    String registeredNumber, codeFromMsg;
    int currentPage = 0;
    Context mcontext;
    Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if ((System.currentTimeMillis() - startTime) / 1000 > 45) {
                /*try {
                    unregisterReceiver(mReceiver);

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}*/
                mHandler.removeCallbacks(runnable);
                //				showTimeOutDialog();
                //				if (SplashActivity.isActivityVisible()) {
                //					showTimeOutDialog();
                //				} else {
                //					dialogue.dismiss();
                //					toast("We will SMS you the verification code soon.", WelcomeScreen.this);
                //
                //				}
            } else {
                mHandler.postDelayed(runnable, 1000);
            }
        }
    };
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mHandler.removeCallbacks(runnable);
            Bundle bundle = intent.getExtras();
            Object[] messages = (Object[]) bundle.get("pdus");
            SmsMessage[] sms = new SmsMessage[messages.length];
            // Create messages for each incoming PDU
            for (int n = 0; n < messages.length; n++) {
                sms[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            }
            for (SmsMessage msg : sms) {
                codeFromMsg = msg.getMessageBody();
            }
            if (verifyCode()) {
                //				registerUser(mContext);
                SharedPreferences settings = getSharedPreferences("prefs", 0); // 0 - for private mode
                Editor editor = settings.edit();
                editor.putBoolean("ISREGISTERED", true);
                editor.commit();
                Toast.makeText(context, "Correct ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, Home.class);
                startActivity(i);
            } else {
                Toast.makeText(mcontext, "Please check the verification code and re-enter", Toast.LENGTH_SHORT).show();
            }
        }

    };
    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    public static String getRandomalphanumericNumber(int numOfCharacters) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        char[] chars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        for (int i = 0; i < numOfCharacters; i++) {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_layout);
        initializeViews();

        Utility.setFirstTime(SplashActivity.this);
        pagerAdapter = new ScreenSlideAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 != 2) {
                    splashtimer.cancel();
                    splashtimer.start();
                } else {
                    splashtimer.cancel();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initializeViews() {
        // btnDone = (TextView) findViewById(R.id.done);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    public void onPause() {
        super.onPause(); // Always call the superclass method first
        splashtimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        splashtimer.start();

        IntentFilter intentFilter = new IntentFilter(INTENT_FILTER_FOR_SMS);
        this.registerReceiver(mReceiver, intentFilter);
        /*sentRandomCode = prefs.getString(AppConstants.RANDOM_CODE, "");
        if (!isCreateCalled && sentRandomCode != null && !sentRandomCode.isEmpty()) {
			dialogue.dismiss();

			isCreateCalled = false;
		}*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
    }

    private void endTutorial() {
        finish();
        // overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

    }

	/*protected boolean verifyCode() {
		if (codeFromMsg != null && codeFromMsg.contains(randomCode)) {
			return true;
		}
		return false;
	}*/

    public void SendSms(String registeredNumber, Context mcontext) {
        this.registeredNumber = registeredNumber;
        this.mcontext = mcontext;
        startTime = System.currentTimeMillis();
        mHandler.postDelayed(runnable, 1000);
        new SendActivationCode().execute();
    }

    protected boolean verifyCode() {
        //		randomCode = prefs.getString(AppConstants.RANDOM_CODE, "");

        if (codeFromMsg != null && codeFromMsg.contains(randomCode)) {
            return true;
        }
        return false;
    }

    protected void showTimeOutDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        // Setting Dialog Title
        alertDialog.setTitle("Sorry");
        // Setting Dialog Message
        alertDialog
                .setMessage("There is some problem with the system. Please wait for some time and enter the verification code in the text box shown here");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                // finish();
                dialog.dismiss();
                //				toast("Please enter the code sent to you\n and click OK to register.", SplashActivity.this);

            }
        });
        alertDialog.show();

    }

    private class ScreenSlideAdapter extends FragmentStatePagerAdapter {

        public ScreenSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new WelcomeScreenFragment(position);
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }
    }

    private class SendActivationCode extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog dialogue = new ProgressDialog(mcontext);
        SharedPreferences prefs;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //			dialogue = new ProgressDialog(SplashActivity.this);
            //			dialogue.setCancelable(false);
            dialogue.setMessage("Sending OTP");
            dialogue.show();
            Toast.makeText(mcontext, "Sending activation code..." + registeredNumber, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isMsgSent = false;
            randomCode = getRandomalphanumericNumber(4);
			/*prefs = getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putString(AppConstants.RANDOM_CODE, randomCode);
//			Log.e(FullertonApp.TAG, "RandomCode ### : " + randomCode);
			editor.commit();
			 */
            //			String URI = "";
            isMsgSent = true;
            String URI = "http://login.bulksmsgateway.in/sendmessage.php?user=atchyutmaddukuri&password=jamesbond_007&mobile=" + registeredNumber + "&message=Thank%20you%20for%20registering%20with%20us.%20Your%20RiceBag%20verification%20code%20is%20" + randomCode + "&sender=RiceBG&type=3";
            try {
                URL mURL = new URL(URI);
                HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
                    isMsgSent = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return isMsgSent;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                Toast.makeText(mcontext, "Please try again later...", Toast.LENGTH_SHORT).show();
                dialogue.dismiss();
                //				pager.setCurrentItem(0);
            } else {
                dialogue.dismiss();
            }
			/*Intent intent = new Intent(mcontext,Home.class);
			startActivity(intent);*/

        }
    }

}