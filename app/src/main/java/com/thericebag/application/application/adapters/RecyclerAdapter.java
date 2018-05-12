package com.thericebag.application.application.adapters;

/**
 * Created by abhil on 18-08-2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.hfrecyclerview.HFRecyclerView;
import com.thericebag.application.R;
import com.thericebag.application.application.beans.OrderConfirmBean;
import com.thericebag.application.application.beans.OrderConfirmedProducts;

import java.util.ArrayList;

public class RecyclerAdapter extends HFRecyclerView<OrderConfirmedProducts> {

    private OrderConfirmBean orderConfirmObj = new OrderConfirmBean();

    public RecyclerAdapter(ArrayList<OrderConfirmedProducts> data, OrderConfirmBean orderConfirmObj) {
        // With Header & With Footer
        super(data, true, true);
        this.orderConfirmObj = orderConfirmObj;
//        super()
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            String title = getItem(position).getTitle();
            itemViewHolder.txtProductTitle.setText(title);
            itemViewHolder.txtOurPrice.setText("₹ " + getItem(position).getPurchase_price());
            itemViewHolder.txtWeight.setText("Weight: " + getItem(position).getSizes_quantity());
            itemViewHolder.txtQty.setText("Qty: " + getItem(position).getPurchase_quantity());
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.txtOrderID.setText("#" + orderConfirmObj.getReference_no() + "");
            headerViewHolder.txtAmount.setText(orderConfirmObj.getPurchase_price());
            headerViewHolder.txtOrderDate.setText(orderConfirmObj.getDt());
            headerViewHolder.txtPaymentType.setText(orderConfirmObj.getCard_type());

        } else if (holder instanceof FooterViewHolder) {

        }
    }

    //region Override Get ViewHolder
    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_example, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.item_header, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.item_footer, parent, false));
    }
    //endregion

    //region ViewHolder Header and Footer
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductTitle;
        TextView txtWeight;
        TextView txtOurPrice;
        TextView txtQty;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtProductTitle = (TextView) itemView.findViewById(R.id.txtProductTitle);
            txtWeight = (TextView) itemView.findViewById(R.id.txtWeight);
            txtOurPrice = (TextView) itemView.findViewById(R.id.txtOurPrice);
            txtQty = (TextView) itemView.findViewById(R.id.txtQty);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderID;
        TextView txtOrderDate;
        TextView txtPaymentType;
        TextView txtAmount;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            txtOrderID = (TextView) itemView.findViewById(R.id.txtOrderID);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtPaymentType = (TextView) itemView.findViewById(R.id.txtPaymentType);
            txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}