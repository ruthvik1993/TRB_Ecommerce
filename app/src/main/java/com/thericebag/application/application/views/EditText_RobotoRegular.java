package com.thericebag.application.application.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by atchy on 04-10-2016.
 */

public class EditText_RobotoRegular extends EditText {
    public EditText_RobotoRegular(Context context) {
        super(context);
        init();
    }

    public EditText_RobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditText_RobotoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
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
