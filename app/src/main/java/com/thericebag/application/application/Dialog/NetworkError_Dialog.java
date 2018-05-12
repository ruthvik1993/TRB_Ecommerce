package com.thericebag.application.application.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.thericebag.application.R;


/**
 * Created by abhil on 08-11-2016.
 */

public class NetworkError_Dialog {

    Context mcontext;
    Dialog dialog;

    public NetworkError_Dialog(Context context) {
        mcontext = context;
        dialog = new Dialog(context);
    }

    public void dismissDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showDialog(View.OnClickListener onClickListener) {

        // custom dialog
        dialog.setContentView(R.layout.dialog_internet_connection);

        dialog.setCancelable(false);

        TextView exitApp = (TextView) dialog.findViewById(R.id.exitApp);

        TextView tryAgain = (TextView) dialog.findViewById(R.id.tryAgain);

        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mcontext.startActivity(homeIntent);
            }
        });

        tryAgain.setOnClickListener(onClickListener);

        dialog.show();
    }
}