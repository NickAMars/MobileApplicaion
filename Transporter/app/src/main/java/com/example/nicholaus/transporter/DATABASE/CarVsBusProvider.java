package com.example.nicholaus.transporter.DATABASE;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.nicholaus.transporter.DATABASE.FieldNames.CarsVsBusFieldName;

/**
 * Created by Nicholaus on 10/23/2017.
 */
// content provider for cars and bus
public class CarVsBusProvider extends ContentProvider {


    // finish with the general setup of my content provider for the  car vs bus  version


    // -------------------------------------------general methods---------------------------------------------------------------
    @Override
    public boolean onCreate() {
        Log.d(TAG,"OnCreate For CarVsBusProvider");
        dbHelper = DBHelper.getInstance(getContext());
        return true;
    }//successfully loaded
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG,"QUERY in CarVsBusProvider");
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables(DBHelper.TABLE_NAME); // target the database name
        // make the database readable
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // call the query method
        Cursor cursor = sqb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG,"getType in CarVsBusProvider");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Log.d(TAG,"insert in CarVsBusProvider");
        int matchCode = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase(); // make database writable
        long id = 0;
        switch (matchCode) {
            case CODE_1:
                id = sqlDB.insert(DBHelper.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG,"delete For CarVsBusProvider");
        int matchCode = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // make the database writable
        int rowsAffected = 0;
        switch (matchCode) {
            case CODE_1:
                // delete table name information
                rowsAffected = db.delete(DBHelper.TABLE_NAME, selection, selectionArgs); // delete specific
                break;
            case CODE_2:
                String id = uri.getLastPathSegment();
                   if(TextUtils.isEmpty(selection)) {
                rowsAffected = db.delete(DBHelper.TABLE_NAME, CarsVsBusFieldName.COL_ID + "=" + id, null); // delete a event
                } else{
                       // delete selected element at the id
                  rowsAffected = db.delete(DBHelper.TABLE_NAME, CarsVsBusFieldName.COL_ID+ "=" + id + "and"+ selection,selectionArgs);
                 }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG,"update For CarVsBusProvider");
        int matchCode = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // make database writable
        int Rowupdated ;
        switch (matchCode) {
            case CODE_1:
                //update everything
                Rowupdated = db.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_2:
                //Update single user
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    Rowupdated  = db.update(DBHelper.TABLE_NAME, values, CarsVsBusFieldName.COL_ID+ "=" + id, null);
                else
                    Rowupdated  = db.update(DBHelper.TABLE_NAME, values, CarsVsBusFieldName.COL_ID + "=" + id + " AND " + selection, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Rowupdated ;
    }
    // -----------------------------------------------general methods----------------------------------------------------------------------------------------
    // specificst

    private static final String TAG = "CarVsBusProvider";
    public static final String AUTHORITY = "com.example.nicholaus.transporter.DATABASE.CarVsBusProvider";
    private static final String BASE_PATH = "space";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // code for different type of data access functionality



    private DBHelper dbHelper;
    private static final int CODE_1 = 1;
    private static final int CODE_2 = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, CODE_1); // without id added
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CODE_2); // with id added
    }

}
