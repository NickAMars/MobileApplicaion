package com.example.nicholaus.fragmentss;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicholaus.fragmentss.DATABASE.CRUDMapProvider;
import com.example.nicholaus.fragmentss.DATABASE.DATABASEFieldNAmes.FieldNames;
import com.example.nicholaus.fragmentss.DATABASE.MapObjects;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Nicholaus on 10/30/2017.
 */

public class Fragment1 extends Fragment {
    private static final String TAG = "Fragment1";
    private ImageView imagebutton;
    TextView displaytext;
    private String address;
    Button btnMap;
    Button btnNext;
    int i;
    //  public static int hold;
    int imagecount;
    private ArrayList<String> newBitmaps;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_layout, container, false);
        initialize(view);
        Log.d(TAG, "onCreateView: started.");
        Bundle bundle = getArguments();
        if (bundle != null) {
            address = bundle.getString("ADDR");
            displaytext.setText(address);
            locationPicture();

        }
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).setViewPager(0);
                Toast.makeText(getActivity(), "you click next", Toast.LENGTH_SHORT);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Next button");
                if(newBitmaps.isEmpty()){
                    Toast.makeText(getActivity(),"No pictures to display",Toast.LENGTH_SHORT);
                    return;
                }
                if (newBitmaps.size() -1 > i) {
                    i = i + 1;
                    //-------------------------------------------------------------------------------------
                    BitmapFactory.Options options = new BitmapFactory.Options(); // create instance
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    // get the reference from the data base
                    Bitmap imageBitmap = BitmapFactory.decodeFile(newBitmaps.get(i),options);
                    // -----------------------------------------------------------------------------------------

                    imagebutton.setImageBitmap(imageBitmap); // skips throw the images
                } else {
                    i = 0;
                    //------------------------------------------------------------------
                    BitmapFactory.Options options = new BitmapFactory.Options(); // create instance
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    // get the reference from the data base
                    Bitmap imageBitmap = BitmapFactory.decodeFile(newBitmaps.get(i),options);
                    //---------------------------------------------------------------------------


                    imagebutton.setImageBitmap(imageBitmap);

                }
            }
        });
        return view;
    }

    public void initialize(View view) {
        imagebutton = (ImageView) view.findViewById(R.id.display_image_view_button);
        displaytext = (TextView) view.findViewById(R.id.display_address_of_location);
        btnMap = (Button) view.findViewById(R.id.map_button_go_back);
        btnNext=(Button) view.findViewById(R.id.display_next_image);
        address ="";
        i = 0;
        imagecount = 0;
    }

    public void locationPicture() {

        ImageSave2 savedimage = new ImageSave2(address);
        savedimage.execute();
    }




    public class ImageSave2 extends AsyncTask<Void, Void, ArrayList<String>> {
        private String address;
        public ImageSave2 (String address){
            this.address = address;
        }
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            MapObjects mapObjects = new MapObjects();
          ArrayList<MapObjects>objectss = new ArrayList<>();
            ArrayList<String> images = new ArrayList<>();
            try {
                ContentResolver contRes = getContext().getContentResolver();
                Cursor cursor = contRes.query(CRUDMapProvider.CONTENT_URI, null, null, null, null);

                while (cursor.moveToNext()) {

                    // if the addresses are equal then and only then i would get the image all the markers at the position
                    String addresses = cursor.getString(cursor.getColumnIndex(FieldNames.COL_ADDRESS));
                    if (address.equals(addresses)) {
                        mapObjects.setReferenceImage(cursor.getString(cursor.getColumnIndex(FieldNames.COL_REF_IMAGE)));
                        images.add(mapObjects.getReferenceImage());
                    }
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return images;
        }

        @Override
        protected void onPostExecute(ArrayList<String> image) {
            newBitmaps = new ArrayList<>();
            newBitmaps = image;

            // set the first one to the initial
            BitmapFactory.Options options = new BitmapFactory.Options(); // create instance
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            // get the reference from the data base
            Bitmap imageBitmap = BitmapFactory.decodeFile(newBitmaps.get(0),options);

            imagebutton.setImageBitmap(imageBitmap); // initialize
        }
    }
}
