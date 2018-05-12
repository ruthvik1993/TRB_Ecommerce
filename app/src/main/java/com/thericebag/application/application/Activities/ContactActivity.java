package com.thericebag.application.application.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.thericebag.application.R;
import com.thericebag.application.application.utility.Utility;

public class ContactActivity extends AppCompatActivity {

    private ImageView backIcon;

    private WebView webContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().hide();   // Hide Tool bar from layout
        Utility.changeStatusbarColor(this, "#54ad54");
        initializeViews();

        onClicks();

        webContactUs.loadData("<html> <head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\"> <title>Contact Address:</title> </head> <body> <div style=\"margin-left:25px\"> <table> <tr><td style=\"vertical-align:top\"><img src=\"https://www.thericebag.com/mobile-images/contact/address_img_contact_us.png\" width=\"20px\" height=\"20px\"></td><td> <p>TheRiceBag,<br>Near GemMotors Workshop,<br>Kondapur,<br>Hyderabad-500084</p></td> </tr> <tr><td><img src=\"https://www.thericebag.com/mobile-images/contact/phone_icon_contact_us.png\" width=\"20px\" height=\"20px\"></td> <td><p>8985093100</p></td></tr><br> <tr><td><img src=\"https://www.thericebag.com/mobile-images/contact/mail_icon_contact_us.png\" width=\"20px\" height=\"20px\"></td> <td><p>support@thericebag.com</p></td></tr> <tr><td><img src=\"https://www.thericebag.com/mobile-images/contact/url_image_contact_us.png\" width=\"20px\" height=\"20px\"></td> <td><p>www.thericebag.com</p></td></tr></table> </div> </body> </html>", "text/html", null);
    }

    private void onClicks() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeViews() {
        webContactUs = (WebView) findViewById(R.id.webContactUs);
        backIcon = (ImageView) findViewById(R.id.backIcon);
    }
}
