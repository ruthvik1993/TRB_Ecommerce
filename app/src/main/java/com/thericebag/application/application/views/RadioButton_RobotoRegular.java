package com.thericebag.application.application.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by atchy on 17-09-2016.
 */
public class RadioButton_RobotoRegular extends RadioButton {
    public RadioButton_RobotoRegular(Context context) {
        super(context);
        init();
    }

    public RadioButton_RobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioButton_RobotoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_regular.ttf");
            setTypeface(tf, Typeface.NORMAL);
        }
    }
}
