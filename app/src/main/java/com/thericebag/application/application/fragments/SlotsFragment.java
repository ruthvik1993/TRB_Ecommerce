package com.thericebag.application.application.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.thericebag.application.R;
import com.thericebag.application.application.Activities.checkOutActivity;
import com.thericebag.application.application.utility.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SlotsFragment extends Fragment {

    RadioButton radioDay1, radioDay2, radioTime1, radioTime2;
    RadioGroup radioGroupDate, radioGroupTimeSlot;
    private View mView;
    private int hour;
    private boolean isSameDay = false;

    private String day1, day2;

    private String radioDay1Text = "", radioDay2Text, radioTime1Text, radioTime2Text;

    private String todayDate, tomorrowDate, dayAfterTomorrowDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_timeslot, container, false);

        initializeViews();

        buttonOnClicks();

        setTimeSlots();

        return mView;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            ((checkOutActivity) getActivity()).setSlotsVisited();
        }
    }

    private void setTimeSlots() {

        Calendar c = Calendar.getInstance();

        hour = c.get(Calendar.HOUR_OF_DAY);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        String formattedDate = df.format(c.getTime());

        GregorianCalendar gc = new GregorianCalendar();

        // Get Day after tomorrow date

        gc.add(Calendar.DATE, 2);

        dayAfterTomorrowDate = df.format(gc.getTime());

        // Get Tomorrow date

        gc = new GregorianCalendar();

        gc.add(Calendar.DATE, 1);

        tomorrowDate = df.format(gc.getTime());

        // Get Today date
        todayDate = df.format(new GregorianCalendar().getTime());

        radioGroupTimeSlot.clearCheck();
        radioGroupDate.clearCheck();

        if (hour >= 15) {
            radioDay1Text = tomorrowDate;
            radioDay2Text = dayAfterTomorrowDate;

            // TODO disable today radio button
            radioDay1.setText("Tomorrow");
            radioDay2.setText(dayAfterTomorrowDate);

            setTime(hour);

            radioDay1.setChecked(true);
            radioTime2.setChecked(true);

        } else {
            radioDay1Text = todayDate;
            radioDay2Text = tomorrowDate;

            radioDay1.setText("Today");
            radioDay2.setText("Tomorrow");

            radioTime1.setVisibility(View.INVISIBLE);

            setTime(hour);

            radioDay2.setChecked(true);
            radioTime1.setChecked(true);
        }


    }

    private void setTime(int hour) {
        if (hour >= 9) {

//            radioTime2.setChecked(true);

//            radioTime1.setClickable(false);

            radioTime2.setClickable(true);
        } else {
            radioTime1.setClickable(true);
            radioTime2.setClickable(true);

//            radioTime1.setChecked(true);
        }
    }

    private void buttonOnClicks() {
        radioGroupDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioGroupTimeSlot.clearCheck();
                if (checkedId == R.id.radioDay1) {
                    if (hour >= 15) {
                        radioTime1.setVisibility(View.VISIBLE);
                        radioTime1.setClickable(true);      // Tomorrow Header
                    } else if (hour >= 9) {
                        radioTime1.setClickable(false);     // Today header
                        radioTime1.setVisibility(View.INVISIBLE);
                    } else {
                        radioTime1.setClickable(true);      // Today header
                        radioTime1.setVisibility(View.VISIBLE);
                    }

                    radioDay1.setBackgroundResource(R.drawable.layout_notrounded_selected);
                    radioDay1.setTextColor(Color.parseColor("#FFFFFF"));

                    radioDay2.setBackgroundResource(R.drawable.layout_notrounded);
                    radioDay2.setTextColor(Color.parseColor("#a9a9a9"));
                } else if (checkedId == R.id.radioDay2) {
                    radioTime1.setVisibility(View.VISIBLE);
                    radioTime2.setVisibility(View.VISIBLE);
                    radioTime1.setClickable(true);

                    radioDay2.setBackgroundResource(R.drawable.layout_notrounded_selected);
                    radioDay2.setTextColor(Color.parseColor("#FFFFFF"));

                    radioDay1.setBackgroundResource(R.drawable.layout_notrounded);
                    radioDay1.setTextColor(Color.parseColor("#a9a9a9"));
                }
                setExpectedDeliverySlot();
            }
        });

        radioGroupTimeSlot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioTime1) {
                    radioTime1.setBackgroundResource(R.drawable.layout_round_corners_selected);
                    radioTime1.setTextColor(Color.parseColor("#FFFFFF"));
                    radioTime2.setBackgroundResource(R.drawable.layout_round_corners_border);
                    radioTime2.setTextColor(Color.parseColor("#a9a9a9"));
                } else if (checkedId == R.id.radioTime2) {
                    radioTime2.setBackgroundResource(R.drawable.layout_round_corners_selected);
                    radioTime2.setTextColor(Color.parseColor("#FFFFFF"));

                    radioTime1.setBackgroundResource(R.drawable.layout_round_corners_border);
                    radioTime1.setTextColor(Color.parseColor("#a9a9a9"));
                }
                setExpectedDeliverySlot();
            }
        });
    }

    private void initializeViews() {
        radioDay1 = (RadioButton) mView.findViewById(R.id.radioDay1);
        radioDay2 = (RadioButton) mView.findViewById(R.id.radioDay2);
        radioGroupDate = (RadioGroup) mView.findViewById(R.id.radioDate);

        radioGroupTimeSlot = (RadioGroup) mView.findViewById(R.id.radioGroupTimeSlot);
        radioTime1 = (RadioButton) mView.findViewById(R.id.radioTime1);
        radioTime2 = (RadioButton) mView.findViewById(R.id.radioTime2);
    }


    private void setExpectedDeliverySlot() {
        //  "27-10-2015&nbsp;12:00PM to 4:00PM"
        String date = "", time = "";
        if (radioDay1.isChecked()) {
            if (radioDay1Text.equalsIgnoreCase(todayDate)) {
                isSameDay = true;
            } else {
                isSameDay = false;
            }
            date = radioDay1Text;
        } else if (radioDay2.isChecked()) {
            date = radioDay2Text;
            isSameDay = false;
        }


        if (radioTime1.isChecked()) {
            time = "9:00AM to 02:00PM";
        }
        if (radioTime2.isChecked()) {
            if (radioDay1Text.equalsIgnoreCase(todayDate)) {
                time = "03:00PM to 08:00PM(SameDay)";
            } else {
                time = "03:00PM to 08:00PM";
            }
        }

        ((checkOutActivity) getActivity()).setDeliveryExpectedTime(date + "&nbsp;" + time, isSameDay);

    }

}