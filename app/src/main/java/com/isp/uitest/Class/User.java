package com.isp.uitest.Class;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class User {
    String name,ID, password, role;
    ArrayList<String> eventIDs = new ArrayList<> ();
    public User(String name, String ID, String password, String role, String eventIDs) {
        this.name = name;
        this.ID = ID;
        this.password = password;
        this.role = role;
        if(!eventIDs.equals("")) this.eventIDs = new ArrayList<> (Arrays.asList(eventIDs.split(" ")));
    }

    public String getName() {
        return this.name;
    }
    public String getId() {
        return this.ID;
    }
    //public String getPassword() { return this.password; }
    public String getRole() {
        return this.role;
    }
    public ArrayList<String> getEventIDs() {
        return eventIDs;
    }
    public String getEventIDInString() {
        if (eventIDs.size() == 0) return " ";
        else {
            String idString = " ";
            for (String id: eventIDs){
                idString = idString + id + " ";
            }
            return idString;
        }
    }
    public void addOneId(String id){
        eventIDs.add(id);
    }
}
