package com.example.nicholaus.fragmentss.DATABASE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nicholaus.fragmentss.DATABASE.DATABASEFieldNAmes.FieldNames;

/**
 * Created by Nicholaus on 10/24/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    @Override
    public void onCreate(SQLiteDatabase db) { // create the table needed
        Log.d(TAG,"onCreate");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + FieldNames.COL_ID + " INTEGER PRIMARY KEY,"
                + FieldNames.COL_ADDRESS + " TEXT,"
                + FieldNames.COL_REF_IMAGE + " BLOB,"  // hold image
                + FieldNames.COL_LAT + " DOUBLE,"
                + FieldNames.COL_LNG + " DOUBLE)");
    }
    public DBHelper(Context mContext) {
        super(mContext, DATABASE_NAME, null, DB_VERSION);
        Log.d(TAG,"Constructor");
        this.mContext = mContext;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG,"onUpgrade");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public static DBHelper getInstance(Context applicationContext) { // gets the context of the content provider
        Log.d(TAG, "getInstance");
        if (mInstance == null)
            mInstance = new DBHelper(applicationContext);  //android.app.Application@beb897c
        return mInstance;
    }


    // Creating the table for data base
    private static final String TAG = "DBHelper";
    static final String TABLE_NAME = "map_table";
    static final String DATABASE_NAME = "map_database";
    private static final int DB_VERSION = 1; // version
    private static DBHelper mInstance;
    private final Context mContext;
}
