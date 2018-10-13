package com.example.nicholaus.fragmentss;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nicholaus.fragmentss.DATABASE.CRUDMapProvider;
import com.example.nicholaus.fragmentss.DATABASE.DATABASEFieldNAmes.FieldNames;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nicholaus on 10/30/2017.
 */

public class Fragment2 extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private static final String TAG = "Fragment2";
    private Button btnNavFrag1;
    View mView;
    private String yourAddress;
    private FusedLocationProviderClient client;
    boolean PermissionGranted;
    private GoogleMap mMap;
    final static int REQUEST_LOCATION_CODE = 1; // code
    private static final float DEFAULT_ZOOM = 10f;
    private Location currentLocation;

    static final int REQUEST_TAKE_PHOTO = 10;
    static final int REQUEST_IMAGE_CAPTURE = 10;
    private Double latitude;
    private Double longitude;
    private byte[] image;
    private static final String CAMERA_FP_AUTHORITY = "com.example.nicholaus.fragmentss.fileprovider";
    private String mCurrentPhotoPath;
    ImageView imageView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment2_layout, container, false);
        btnNavFrag1 = (Button) mView.findViewById(R.id.camera_picture_taken);
        btnNavFrag1.setOnClickListener(this);
        imageView =(ImageView) mView.findViewById(R.id.add_image_viii);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // check version
            checkLocationPermision();
        }
        return mView;
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    private void dispatchTakePictureIntent() {
        //Create an Intent to use the default Camera Application
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG,ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Use the FileProvider defined in the Manifest as the authority for sharing across the Intent
                //Provides a content:// URI instead of a file:// URI which throws an error post API 24
                Uri photoURI = FileProvider.getUriForFile(getActivity(),CAMERA_FP_AUTHORITY,photoFile);
                //Put the content:// URI as the output location for the photo
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //Start the Camera Application for a result
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    //---------------------------------------------------
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //Use ExternalStoragePublicDirectory so that it is accessible for the MediaScanner
        //Associate the directory with your application by adding an additional subdirectory
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCamera");
        if(!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d(TAG,"Storage Directory: " + storageDir.getAbsolutePath());
        return image;
    }
    //----------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //  Bundle extras = data.getExtras();
            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,options);
            //-----------=================Start Aynch method==========----------------------------------
            ImageSave imagesave = new ImageSave(yourAddress,mCurrentPhotoPath,latitude,longitude);
            imagesave.execute();
            // ----------------------------------------------------------------------
            galleryAddPic();

           updateMarker();

        }
    }
public void updateMarker(){
    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.d(TAG,"onMarkerClick");
            Fragment1 tofrag1= new Fragment1();
            Bundle bundle = new Bundle();
            bundle.putString("ADDR", marker.getTitle());
            bundle.putDouble("LAT",marker.getPosition().latitude);
            bundle.putDouble("LNG",marker.getPosition().longitude);
            tofrag1.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.fragment1, tofrag1).commit();

            // Bundle args = new Bundle();
            // args.putString("ADDR", yourAddress);
            // tofrag1.setArguments(args);
            //  getFragmentManager().beginTransaction().add(R.id.containter, tofrag1).commit();
            ((MainActivity)getActivity()).setViewPager(1); // goes to the next fragment
            return false;
        }
    });
    QueryThrough guery = new QueryThrough(mMap);
    guery.execute();// query through database
}

    // saves image in a thread
    private class ImageSave extends AsyncTask<Object, Void, String> {
        private String address;
        private String mCurrentPhotoPath;
        private Double lat;
        private Double lng;
        public ImageSave(String address, String mCurrentPhotoPath, Double lat, Double lng){
            this.address = address;
            this.mCurrentPhotoPath = mCurrentPhotoPath;
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        protected String doInBackground(Object... objects) {
            try {
                // insert in the database
                CRUDMapProvider crud = new CRUDMapProvider();
                ContentValues cv = new ContentValues();
                cv.put(FieldNames.COL_ADDRESS, address);
                cv.put(FieldNames.COL_REF_IMAGE, mCurrentPhotoPath);
                cv.put(FieldNames.COL_LAT, lat);
                cv.put(FieldNames.COL_LNG, lng);
                getActivity().getContentResolver().insert(CRUDMapProvider.CONTENT_URI, cv);
                Thread.sleep(500);
            } catch (InterruptedException e) {e.printStackTrace();}
            return null;
        }
        // no need for post message

        @Override
        protected void onPostExecute(String aVoid) {
            Log.d(TAG, "You have successfully save the image");
        }
    }
    private void galleryAddPic() {
        File f = new File(mCurrentPhotoPath);
        Intent myMediaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        myMediaIntent.setData(Uri.fromFile(f));
        getContext().sendBroadcast(myMediaIntent);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onClick(View v) {
        dispatchTakePictureIntent();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getDeviceLocation();
        mMap.setMyLocationEnabled(true);

        QueryThrough guery = new QueryThrough(mMap);
        guery.execute();// query through database


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG,"onMarkerClick");
                Fragment1 tofrag1= new Fragment1();
                Bundle bundle = new Bundle();
                bundle.putString("ADDR", marker.getTitle());
                bundle.putDouble("LAT",marker.getPosition().latitude);
                bundle.putDouble("LNG",marker.getPosition().longitude);
                tofrag1.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
              manager.beginTransaction().replace(R.id.fragment1, tofrag1).commit();

                // Bundle args = new Bundle();
               // args.putString("ADDR", yourAddress);
               // tofrag1.setArguments(args);
              //  getFragmentManager().beginTransaction().add(R.id.containter, tofrag1).commit();
              ((MainActivity)getActivity()).setViewPager(1); // goes to the next fragment
                return false;
            }
        });

    }
    /*
* //Put the value
fragment1 name = new fragment1 ();
Bundle args = new Bundle();
args.putString("YourKey", "YourValue");
ldf.setArguments(args);

//Inflate the fragment
getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();*/


    private class QueryThrough extends AsyncTask<Void, Void, Cursor>{
        private GoogleMap mMap;
public QueryThrough(GoogleMap mMap){
    this.mMap = mMap;

}

        @Override
        protected Cursor doInBackground(Void... params) {
            ContentResolver contRes = getContext().getContentResolver();
            Cursor cursor = null;
            try {
                 cursor = contRes.query(CRUDMapProvider.CONTENT_URI, null, null, null, null);
                Thread.sleep(1000);
            } catch (InterruptedException e) {e.printStackTrace();}

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor c) {


            if(c.getCount()> 0){
                while (c.moveToNext()) {
                    Double longitude = c.getDouble(c.getColumnIndex(FieldNames.COL_LNG));
                    String Place = c.getString(c.getColumnIndex(FieldNames.COL_ADDRESS));
                    Double latitude = c.getDouble(c.getColumnIndex(FieldNames.COL_LAT));
                    Log.d(TAG, "Address : "+Place+" Latitude : " + latitude + " Longitude : " + longitude);
                    // some reason they dont want you to put in different class
                    mMap.addMarker(new MarkerOptions().title(Place)
                            .position(new LatLng(latitude,longitude))
                    );

                   /* Log.d(TAG,"\nLongititude : \n" + c.getDouble(c.getColumnIndex(FieldNames.COL_LNG))
                    +"\n Latitiude :\n" + c.getDouble(c.getColumnIndex(FieldNames.COL_LAT))
                    +"\n Address  : \n" +c.getString(c.getColumnIndex(FieldNames.COL_ADDRESS))
                                    +"\n Image  : \n" +c.getBlob(c.getColumnIndex(FieldNames.COL_IMAGE))
                    );*/
                }
            }

        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }



    // ---------------------------gives me the beginning  location of the user------------------------------
    // get the location of the user
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the device current location");

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        try{
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                Task location = client.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Found location");
                            currentLocation = (Location) task.getResult(); // update currentLocation
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), DEFAULT_ZOOM ); // move the map to
                            Address address =getGeocordinates(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())); // your address converted to a string
                            yourAddress = address.getFeatureName()+ " " +address.getThoroughfare();
                            // could create a marker here with picture
                            Log.d(TAG, "Your location is : " + yourAddress);
                            Log.d(TAG, "latitude : " + latitude + "longitude : " + longitude);
                        }else{
                            Log.d(TAG, "Cant find location");
                        }
                    }
                });
            }
        }catch(SecurityException e){
            Log.d(TAG,"getDirectionLocation: SecurityException:" + e.getMessage());
        }
    }
    // zoom
    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    // ------------------------------------------------------------------------------------------------------------
// use geocode to get the address of the coordinates  ----- return address;
    public Address getGeocordinates(LatLng latLng){
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            latitude = latLng.latitude;
            longitude = latLng.longitude;
            // gets the address of the location
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        } catch (IOException e) {e.printStackTrace();}
        catch (IllegalArgumentException i){i.printStackTrace();}
        if(addresses != null || addresses.size() == 0){
            Address address = addresses.get(0);
            return address;
        }
        return null;
    }
// -----------------------------------------------------------------------------------


    //---------------------------------Asking permission-------------------------  //https://developer.android.com/training/permissions/requesting.html
    // Should we show an explanation?
    private void checkLocationPermision() { // when they decline from the first time gives them an option
        if(Build.VERSION.SDK_INT >=23) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //shouldShowRequestPermissionRationale() returns true if the app has requested this permission previously and the user denied the request.
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // // need to send alert dialog here before the user has been as for another permission.
                    // when the app calls request permission the system throws a dialog box which cannot be altered
                    Log.d(TAG,"Here15");
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE); // present the user with another try to allow
                } else {
                    // the shouldShowRequestPermissionRationale()  method return false the first time
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
                }
                PermissionGranted = false;
                //     requestPermissions(null,REQUEST_LOCATION_CODE);
            }
            //  requestPermissions(null,REQUEST_LOCATION_CODE);
            PermissionGranted = true;
        }
        else{
            PermissionGranted=true;
        }
    }
    // when user respond this method is called
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionGranted = false;
        Log.d(TAG,"Here2");
        switch (requestCode){ // beginning of switch case statement

            case REQUEST_LOCATION_CODE:
                // check if permission was granted or not
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // if granted
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        PermissionGranted = true;

                        mMap.setMyLocationEnabled(true);
                        isStoragePermissionGranted();
                        getDeviceLocation();

                    }
                }
                else {
                    displayAlertDialog(); // this sends we need your corperation on the enabling of your locations
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                }
                else{
                    displayAlertDialog();
                }


                    break;
            default:  displayAlertDialog();
                break;
        }// end of switch case statement
    }
    // create alert dialog when user has decline the permission
    private void displayAlertDialog(){
        new AlertDialog.Builder(getActivity()).
                setMessage(R.string.alert_dialog_message).
                setPositiveButton(R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        }

                )
                .create()
                .show();
    }








}
