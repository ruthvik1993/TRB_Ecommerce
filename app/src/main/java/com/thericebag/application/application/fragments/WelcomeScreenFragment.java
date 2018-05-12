package com.thericebag.application.application.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thericebag.application.R;
import com.thericebag.application.application.Activities.SplashActivity;

public class WelcomeScreenFragment extends Fragment {

    int position;
    SharedPreferences prefs;

    /*
     * public static WelcomeScreenFragment newInstance(int i) {
     * WelcomeScreenFragment pane = new WelcomeScreenFragment(); Bundle bundle =
     * new Bundle(); bundle.putInt(LAYOUT_ID, layoutId);
     * pane.setArguments(bundle); return pane; }
     */
    public WelcomeScreenFragment() {
        // TODO Auto-generated constructor stub
    }

    @SuppressLint("ValidFragment")
    public WelcomeScreenFragment(int i) {
        // TODO Auto-generated constructor stub
        position = i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View android = inflater.inflate(R.layout.fragment_screen1, container, false);
        /*txtView1 = (TextView) android.findViewById(R.id.txtView1);
        imgFb = (ImageView) android.findViewById(R.id.imgFb);
		txtSecondLine = (TextView) android.findViewById(R.id.txtSecondLine);
		imgTwitter = (ImageView) android.findViewById(R.id.imgTwitter);
		gPlusImg = (ImageView) android.findViewById(R.id.imgGoogle);*/
//		prefs = this.getActivity().getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        final EditText mobileNumber = (EditText) android.findViewById(R.id.mobileNumber);
        TextView done = (TextView) android.findViewById(R.id.done);
        done.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String registeredNumber = mobileNumber.getText().toString();
                SplashActivity obj = new SplashActivity();
                obj.SendSms(registeredNumber, getActivity());
                Toast.makeText(getActivity(), "Done Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        switch (position) {
            case 0:
//			txtSecondLine.setText("IN BANGALORE");
                break;
            case 1:
//			txtSecondLine.setText("AND INJURED 5000");
                break;
            case 2:
//			txtSecondLine.setText("IN BANGALORE");
                break;
            default:
                break;
        }
        return android;
    }

    public void onActivityResult(int requestCode, int responseCode, Intent intent) {

    }

}