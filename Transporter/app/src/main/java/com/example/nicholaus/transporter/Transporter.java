package com.example.nicholaus.transporter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Transporter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter);
        initial();
    }

    public void onClick(View view) {

        // temporary string
        String namestring = "";
        Double gasDouble = 0.00;
        Double salaryDouble = 0.00;
        Double serviceDouble = 0.00;
        Double insuranceDouble = 0.00;
        // calling intent
Intent intent = new Intent(Transporter.this, TotalCarculation.class);
        // begin careful with conversion
        try {
         namestring = name.getText().toString();
            if(TextUtils.isEmpty(namestring)){return;} // they must name the file
         gasDouble = Double.parseDouble(gas.getText().toString());
         salaryDouble = Double.parseDouble(salary.getText().toString());
         serviceDouble = Double.parseDouble(service.getText().toString());
         insuranceDouble = Double.parseDouble(insurance.getText().toString());
    }catch(Exception e){e.printStackTrace();}
            intent.putExtra("NAME", name.getText().toString());
            intent.putExtra("GAS", gasDouble);
            intent.putExtra("SALARY",salaryDouble);
            intent.putExtra("SERVICE", serviceDouble);
            intent.putExtra("INSURANE", insuranceDouble);

        // startIntent
        startActivity(intent);
    }


    // these are the initialize fields
    public void initial(){
        name = (EditText) findViewById(R.id.name_transporter);
        gas = (EditText) findViewById(R.id.gas_transporter);
        salary = (EditText) findViewById(R.id.salary_transporter);
        service = (EditText) findViewById(R.id.service_transporter);
        insurance = (EditText) findViewById(R.id.insurance_transporter);
    }

    EditText name;
    EditText gas;
    EditText salary;
    EditText service;
    EditText insurance;
}
