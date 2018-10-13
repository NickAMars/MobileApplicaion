package com.example.nicholaus.transporter.DATABASE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nicholaus.transporter.DATABASE.FieldNames.CarsVsBusFieldName;

/**
 * Created by Nicholaus on 10/23/2017.
 */


// simple task of database
public class DBHelper extends SQLiteOpenHelper {
    // Creating the table for data base
    private static final String TAG = "DBHelper";
    static final String TABLE_NAME = "test1_table";
    static final String DATABASE_NAME = "test1_database";

    // create the table with car and bus
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + CarsVsBusFieldName.COL_ID+ " INTEGER PRIMARY KEY,"
                + CarsVsBusFieldName.COL_NAME + " TEXT,"
                + CarsVsBusFieldName.COL_CAR_COST + " INTEGER,"
                + CarsVsBusFieldName.COL_BUS_COST + " INTEGER,"
                + CarsVsBusFieldName.COL_TIME + " TEXT)");
    }// +COL_FLAG+ " INTEGER"


    // the version of the database
    private static final int DB_VERSION = 1;


    private static DBHelper mInstance;  //
    private final Context mContext;

    private DBHelper(Context mContext) {
        super(mContext, DATABASE_NAME, null, DB_VERSION);
        this.mContext = mContext;
    }


    @Override

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    // to prevent leak
    public static DBHelper getInstance(Context applicationContext) { // gets the context of the content provider
        if (mInstance == null)
            mInstance = new DBHelper(applicationContext);  //android.app.Application@beb897c
        return mInstance;
    }
}
