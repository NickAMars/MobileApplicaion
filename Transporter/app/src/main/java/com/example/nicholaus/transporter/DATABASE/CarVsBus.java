package com.example.nicholaus.transporter.DATABASE;

/**
 * Created by Nicholaus on 10/23/2017.
 */

public class CarVsBus {
    private int id;
    private String name;
    private String time;
    private int carCost;
    private int busCost;


    public int getId() {return id;}
    public String getName() {return name;}
    public String getTime() {return time;}
    public int getCarCost() {return carCost;}
    public int getBusCost() {return busCost;}

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setTime(String time) {this.time = time;}
    public void setCarCost(int carCost) {this.carCost = carCost;}
    public void setBusCost(int busCost) {this.busCost = busCost;}
}
