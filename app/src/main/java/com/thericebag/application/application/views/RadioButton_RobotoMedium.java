package com.thericebag.application.application.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by atchy on 17-09-2016.
 */
public class RadioButton_RobotoMedium extends RadioButton {
    public RadioButton_RobotoMedium(Context context) {
        super(context);
        init();
    }

    public RadioButton_RobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioButton_RobotoMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_medium.ttf");
            setTypeface(tf, Typeface.NORMAL);
        }
    }
}
