package com.example.nicholaus.transporter.View.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nicholaus.transporter.R;

/**
 * Created by Nicholaus on 10/22/2017.
 */

public class RVHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    private static final String TAG = "RVHolder";
    public final TextView txtUserName;
    public final TextView txtCarTotalCost;
    public final TextView txtBusTotalCost;
    public final TextView txtTimeMade;
    ItemClickListener itemClickListener;

    public RVHolder(View cardview) {
        super(cardview);
        Log.d(TAG,"Constructor of RVHolder");
        // bind card view fields
        txtUserName     = (TextView) cardview.findViewById(R.id.name_card_view_item);
        txtCarTotalCost = (TextView) cardview.findViewById(R.id.car_card_view_item);
        txtBusTotalCost = (TextView) cardview.findViewById(R.id.bus_card_view_items);
        txtTimeMade     = (TextView) cardview.findViewById(R.id.date_card_view_item);
        cardview.setOnClickListener(this); // click on card
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick();
    }

    public void setItemOnClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
