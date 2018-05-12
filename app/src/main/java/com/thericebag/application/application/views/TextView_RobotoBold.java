package com.thericebag.application.application.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextView_RobotoBold extends TextView {
    public TextView_RobotoBold(Context context) {
        super(context);
        init();
    }

    public TextView_RobotoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextView_RobotoBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_bold.ttf");
            setTypeface(tf, Typeface.NORMAL);
        }

    }
}