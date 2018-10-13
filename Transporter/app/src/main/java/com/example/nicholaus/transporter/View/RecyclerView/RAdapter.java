package com.example.nicholaus.transporter.View.RecyclerView;

import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.nicholaus.transporter.DATABASE.CarVsBus;
import com.example.nicholaus.transporter.DATABASE.CarVsBusProvider;
import com.example.nicholaus.transporter.DATABASE.FieldNames.CarsVsBusFieldName;
import com.example.nicholaus.transporter.R;

/**
 * Created by Nicholaus on 10/22/2017.
 */

public class RAdapter extends RecyclerView.Adapter<RVHolder>{
    private static final String TAG = "RAdapater";
    private Cursor cursor;
    private Context context;

    public RAdapter(Cursor cursor,Context context) {
        Log.d(TAG, "Constructor in RAdapter");
        this.cursor = cursor;
        this.context = context;
    }
    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) { // finish inflating me view
        Log.d(TAG, "onCreateViewHolder in RAdapter");
        return new RVHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_items, parent, false));
    }

    @Override
    public void onBindViewHolder(RVHolder holder, final int position) {
        // bind to recHolder
        Log.d(TAG, "onBindViewHolder in RAdapter");
        final CarVsBus result = getUserFromCursor(position); // on bind position
        holder.txtUserName.setText(result.getName());
        holder.txtCarTotalCost.setText(result.getCarCost());
        holder.txtBusTotalCost.setText(result.getBusCost());
        holder.txtTimeMade.setText(result.getTime());
        holder.setItemOnClickListener(new ItemClickListener() {
            @Override
            public void onItemClick() {
                Uri uri = Uri.parse(CarVsBusProvider.CONTENT_URI + "/" + result.getId());
                context.getContentResolver().delete(uri, null, null); //  is method of class android.content.Context
                // need to refresh the screen so Load is needed here
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount in RAdapter");
        if(cursor != null){
            return cursor.getCount(); // get the number of cursor item that where provided by the database
        }
        return 0;
    }

    private CarVsBus getUserFromCursor(int position){
        Log.d(TAG,"getUserFromCursor in RAdapter");
        cursor.moveToPosition(position); // move to next
        CarVsBus carVsBus = new CarVsBus(); // initialize object
        // put information in object base on the fields position
        carVsBus.setId(cursor.getInt(cursor.getColumnIndex(CarsVsBusFieldName.COL_ID)));
        carVsBus.setName(cursor.getString(cursor.getColumnIndex(CarsVsBusFieldName.COL_NAME)));
        carVsBus.setCarCost(cursor.getInt(cursor.getColumnIndex(CarsVsBusFieldName.COL_CAR_COST)));
        carVsBus.setBusCost(cursor.getInt(cursor.getColumnIndex(CarsVsBusFieldName.COL_BUS_COST)));
        carVsBus.setTime(cursor.getString(cursor.getColumnIndex(CarsVsBusFieldName.COL_TIME)));
        return carVsBus;
    }

}
