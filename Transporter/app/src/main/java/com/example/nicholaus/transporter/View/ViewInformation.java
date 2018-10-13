package com.example.nicholaus.transporter.View;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.nicholaus.transporter.DATABASE.CarVsBus;
import com.example.nicholaus.transporter.DATABASE.CarVsBusProvider;
import com.example.nicholaus.transporter.DATABASE.FieldNames.CarsVsBusFieldName;
import com.example.nicholaus.transporter.R;
import com.example.nicholaus.transporter.View.RecyclerView.RAdapter;

/**
 * Created by Nicholaus on 10/22/2017.
 */

public class ViewInformation extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView recyclerView;
    private RAdapter rAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        recyclerView =(RecyclerView) findViewById(R.id.recycle_view);
         loadData();
    }

    public void loadData(){
        getSupportLoaderManager().initLoader(0,null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, CarVsBusProvider.CONTENT_URI,
                new String[]{CarsVsBusFieldName.COL_ID,
                                CarsVsBusFieldName.COL_NAME,
                                CarsVsBusFieldName.COL_CAR_COST,
                                CarsVsBusFieldName.COL_BUS_COST,
                                CarsVsBusFieldName.COL_TIME}, null, null, null);
        return cursorLoader; // set only project
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        rAdapter = new RAdapter(data, this); // cursor and the context
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(rAdapter); // place adapter
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerView.setAdapter(null);
    } // dont recreate whats alread there


}
