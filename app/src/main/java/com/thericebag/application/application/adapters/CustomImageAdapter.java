package com.thericebag.application.application.adapters;


import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thericebag.application.R;

import java.util.ArrayList;
import java.util.List;

public class CustomImageAdapter extends PagerAdapter {
    Context context;
    List<String> imagesList = new ArrayList<String>();
    LayoutInflater layoutInflater;

    public CustomImageAdapter(Context context, List<String> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (imagesList != null) {
            if (imagesList.size() > 2) {
                return 2;
            } else {
                return imagesList.size();
            }
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.image_item, container, false);
        final ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);

        //imageView.setScaleType(ScaleType.CENTER_CROP);

        //imageView.setImageResource(imageId[position]);

        imageView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    // Wait until layout to call Picasso
                    @Override
                    public void onGlobalLayout() {
                        // Ensure we call this only once
                        imageView.getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);


                        Picasso.with(context)
                                .load(imagesList.get(position))
                                .into(imageView);
                    }
                });

        Picasso.with(context)
                .load(imagesList.get(position))
                //.placeholder(R.drawable.progress_animation)
                .into(imageView);

        container.addView(viewItem);

        return viewItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // container.removeView((RelativeLayout) object);
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == ((RelativeLayout) arg1);
        return arg0 == ((View) arg1);
    }
}
