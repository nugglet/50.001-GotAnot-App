package com.isp.uitest.Class;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Location {

    static int total_locations = 0;

    private String name, address, openingHours;
    private int location_id;
    private User[] staff;
    private ArrayList<ResourceSet> resourceArrayList;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mChildReference = mRootReference.child("Location");

    // unsure as of how to reference each location under the number to link to variables
    // TODO: link and create object for each Location


    public Location(String name, int location_id, String address, User[] staff, ArrayList<ResourceSet> resourceArrayList, String openingHours) {
        this.name = name;
        this.location_id = location_id;
        this.address = address;
        this.staff = staff;
        //this.resourceArrayList = resourceArrayList;
        this.openingHours = openingHours;

        total_locations++;
    }

    public String getName() {
        return this.name;
    }

    public int getLocation_id() {
        return this.location_id;
    }

    public String getAddress() {
        return this.address;
    }

    public User[] getStaff() {
        return this.staff;
    }

    //public ArrayList<ResourceSet> getResourceArrayList() {
       // return this.resourceArrayList;
    //}

    public String getOpeningHours() {
        return this.openingHours;
    }

    public int getTotal_locations() {
        return Location.total_locations;
    }
}