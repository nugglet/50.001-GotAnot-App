package com.isp.uitest.ui.login;

import android.app.Application;

import com.isp.uitest.Class.BookingEvent;
import com.isp.uitest.Class.ResourceSet;
import com.isp.uitest.Class.User;

import java.util.ArrayList;


public class LoginInfo extends Application {
    private static User loginUser;
    private static ArrayList<BookingEvent> bookingEvents = new ArrayList<>();
    private static ResourceSet.Resource selectedResource;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public  User getLoginUser(){
        return loginUser;
    }

    public void setUser(User loginUser){
        LoginInfo.loginUser = loginUser;
    }

    public boolean isLogin(){
        return loginUser != null;
    }

    public  ResourceSet.Resource getSelectedResource() { return selectedResource; }

    public void setSelectedResource(ResourceSet.Resource selectedResource) { this.selectedResource = selectedResource; }

    public ArrayList<BookingEvent> getBookingEvents() {
        return bookingEvents;
    }
}
