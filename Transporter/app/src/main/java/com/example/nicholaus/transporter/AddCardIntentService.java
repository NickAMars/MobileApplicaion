package com.example.nicholaus.transporter;

import android.app.IntentService;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.nicholaus.transporter.DATABASE.CarVsBusProvider;
import com.example.nicholaus.transporter.DATABASE.FieldNames.CarsVsBusFieldName;
import com.example.nicholaus.transporter.View.ViewInformation;

/**
 * Created by Nicholaus on 10/23/2017.
 */

public class AddCardIntentService extends IntentService{

    private static final String TAG = "AddCardIntentService" ;

    public AddCardIntentService() {
        super("ADD_card_to_recycler_view");
        Log.d(TAG,"Constructor of  AddCardIntentService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // pass intent
      Log.d(TAG,"onHandlerIntent of  AddCardIntentService");
        String name = intent.getStringExtra("NAME");
        Double carCost =  intent.getExtras().getDouble("CAR");
        Double busCost = intent.getExtras().getDouble("BUS");
        String time = intent.getStringExtra("TIME");


        // add item to recycler view
        try {

            if (!TextUtils.isEmpty(name)) {
                ContentValues cv = new ContentValues();
                cv.put(CarsVsBusFieldName.COL_NAME, name); // puts username with column name
                cv.put(CarsVsBusFieldName.COL_CAR_COST, carCost);
                cv.put(CarsVsBusFieldName.COL_BUS_COST, carCost);
                cv.put(CarsVsBusFieldName.COL_TIME, time);
                getContentResolver().insert(CarVsBusProvider.CONTENT_URI, cv); // insert method
                Thread.sleep(500); // wait time
            }
        }catch (Exception e){e.printStackTrace();}

        // go to recycler view
        Intent  recyclerViewIntent = new Intent ( AddCardIntentService.this, ViewInformation.class);
        recyclerViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(recyclerViewIntent); // go to recycler view

    }
}
