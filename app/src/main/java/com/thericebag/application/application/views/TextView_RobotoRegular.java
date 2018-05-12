package com.thericebag.application.application.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextView_RobotoRegular extends TextView {
    public TextView_RobotoRegular(Context context) {
        super(context);
        init();
    }

    public TextView_RobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextView_RobotoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
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