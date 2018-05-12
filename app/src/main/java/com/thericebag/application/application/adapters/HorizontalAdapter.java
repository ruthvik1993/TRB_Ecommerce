package com.thericebag.application.application.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thericebag.application.R;
import com.thericebag.application.application.Activities.ProductDetailActivity;
import com.thericebag.application.application.beans.ProductBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhil on 21-08-2016.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    ArrayList<ProductBean> products = new ArrayList<>();
    Context mContext;

    public HorizontalAdapter(Context context, ArrayList<ProductBean> products) {
        this.products = products;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ProductBean productObj = products.get(position);

        if (productObj != null && productObj.getAct_price() != 0 && productObj.getOff_price() != 0 & productObj.getTitle() != null) {

            holder.txtProduct.setText(productObj.getTitle());

            holder.txtMrkPrice.setText("₹ " + productObj.getAct_price());
            holder.txtOurPrice.setText("₹ " + productObj.getOff_price());

            List<String> productImages = Utility.seperateImages(productObj.getProduct_image());
            if (productObj.getProduct_image() != null) {
                Picasso.with(mContext)
                        .load(productImages.get(0)).placeholder(R.drawable.ricebag_sample)
                        .into(holder.imgPic);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra(AppConstants.ID, productObj.getId());
                    intent.putExtra(AppConstants.TITLE, productObj.getTitle());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProduct;
        public TextView txtOurPrice;
        public TextView txtMrkPrice;
        public ImageView imgPic;

        public MyViewHolder(View view) {
            super(view);
            txtProduct = (TextView) view.findViewById(R.id.txtProduct);
            txtOurPrice = (TextView) view.findViewById(R.id.txtOurPrice);
            txtMrkPrice = (TextView) view.findViewById(R.id.txtMrkPrice);
            imgPic = (ImageView) view.findViewById(R.id.imgPic);
        }
    }
}
