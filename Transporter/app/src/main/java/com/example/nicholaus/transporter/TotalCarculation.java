package com.example.nicholaus.transporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Nicholaus on 10/21/2017.
 */

public class TotalCarculation  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_calc);
        initalizeVeriable();


        Toast.makeText(this,"NAME :" + nameintent +
                "SALARY : " +salaryintent
                + "INSURANCE : " + insuranceintent, Toast.LENGTH_SHORT).show();
    }

    // store information into the database
    public void onClick(View view) {

        // sends throught to server to get to the recycler view
        Intent intent = new Intent(getApplicationContext(), AddCardIntentService.class);
        intent.putExtra("NAME", namefield.getText());
        intent.putExtra("CAR",carCost.getText());
        intent.putExtra("BUS",busCost.getText());
        // get the current time
        intent.putExtra("TIME",Calendar.getInstance().toString() );
        startService(intent);
    }



    public void initalizeVeriable(){
        Intent intent = getIntent();
        nameintent = intent.getExtras().getString("NAME");
        salaryintent = intent.getExtras().getDouble("SALARY");
        insuranceintent = intent.getExtras().getDouble("INSURANE");
        serviceintent = intent.getExtras().getDouble("SERVICE");
        gasintent = intent.getExtras().getDouble("GAS");
        setCostCarAndBus();
    } // initialize everything


    public void setCostCarAndBus(){
        Double cartotal = insuranceintent + serviceintent + gasintent; // cost of maintaining your car
        Double bustotal = 360.00; // calculate the total
        busOrCar = (TextView) findViewById(R.id.decide_bus_orcar);
        carCost = (TextView) findViewById(R.id.car_cost);
        busCost = (TextView) findViewById(R.id.bus_cost);
        namefield = (TextView) findViewById(R.id.name_of_user);


        if(bustotal < cartotal){
            if(bustotal < salaryintent) { // use the bus
                busOrCar.setText("You should consider using the bus.\n\n Thank you for using this app");
            }else {// walk
                busOrCar.setText("You should walk to get where your going. \n\n Thank you for using this app");
            }
        }else {
            busOrCar.setText("Thank you for using this app");
        }

        carCost.setText("$ "+ cartotal);
        busCost.setText("$ "+ bustotal);
        namefield.setText(nameintent);
    }


    private TextView carCost;
    private TextView busCost;
    private TextView namefield;
    private TextView busOrCar;

    String nameintent;
    Double salaryintent;
    Double insuranceintent;
    Double serviceintent;
    Double gasintent;

}