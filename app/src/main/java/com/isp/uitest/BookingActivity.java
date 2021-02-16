package com.isp.uitest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isp.uitest.Class.BookingEvent;
import com.isp.uitest.Class.ResourceSet;
import com.isp.uitest.Class.Staff;
import com.isp.uitest.Class.Student;
import com.isp.uitest.Class.User;
import com.isp.uitest.data.LoginDataSource;
import com.isp.uitest.ui.login.LoginActivity;
import com.isp.uitest.ui.login.LoginInfo;

import java.util.ArrayList;
import java.util.HashMap;

import static com.isp.uitest.ui.login.ButtonEffect.buttonEffect;

public class BookingActivity extends Activity {

    LoginInfo info;
    DatePicker time_collect;
    DatePicker time_return;
    EditText amt;
    Button order_button;
    User user;
    String eventsId = "";
    ResourceSet.Resource resource;
    String EventId;
    long nextID;
    final DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference evt_root = mRootDatabaseRef.child("Event");
    final DatabaseReference rsc_root = mRootDatabaseRef.child("Resource");
    final DatabaseReference usr_root = mRootDatabaseRef.child("User");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_item);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*0.9), (int) (height*0.8));
        info = (LoginInfo) getApplication();
        user = info.getLoginUser();
        resource = info.getSelectedResource();
        time_collect = findViewById(R.id.time_collect);
        time_return = findViewById(R.id.time_return);
        amt = findViewById(R.id.amt);
        order_button = findViewById(R.id.order_button);
        amt.setHint(info.getSelectedResource().getStock());
        evt_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nextID = snapshot.child("next").getValue(Long.class); }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});

        order_button.setOnClickListener(new View.OnClickListener(){
            //Create a new event
            @Override
            public void onClick(View view) {
                try {
                    int requiredAmt = Integer.parseInt(amt.getText().toString());
                    if (requiredAmt > 0 && resource.getStockInInt() - requiredAmt >= 0 &&
                            ((user instanceof Student & resource.isBookable_by_stu()) || user instanceof Staff & resource.isBookable_by_staff())) {
                        EventId = BookingEvent.createBookingId(nextID);
                        DatabaseReference newEventRoot = evt_root.child(EventId);
                        newEventRoot.child("amt").setValue(amt.getText().toString());
                        newEventRoot.child("item_id").setValue(resource.getId());
                        newEventRoot.child("item_name").setValue(resource.getName());
                        newEventRoot.child("location").setValue(resource.getLocation());
                        newEventRoot.child("ppl_id").setValue(user.getId());
                        newEventRoot.child("ppl_name").setValue(user.getName());
                        newEventRoot.child("status").setValue("to be collected");
                        int collectMonth = time_collect.getMonth() + 1;
                        int returnMonth = time_return.getMonth() + 1;
                        newEventRoot.child("time_collect").setValue(time_collect.getDayOfMonth() + "/" + collectMonth + "/" + time_collect.getYear());
                        newEventRoot.child("time_return").setValue(resource.is_no_need_return()? "NA" : returnMonth + "/" +time_return.getMonth() + "/" + time_return.getYear());
                        //Change next and resource amt + add Booking event
                        evt_root.child("next").setValue(BookingEvent.createNextId(nextID));
                        rsc_root.child(resource.getId()).child("amt").setValue(resource.getStockInInt() - requiredAmt);
                        usr_root.child(user.getId()).child("event").setValue(BookingEvent.createBookingId(nextID) + user.getEventIDInString());
                        user.addOneId(EventId);
                        Toast.makeText(getApplication(),"Create event Successfully!", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(getApplication(),(requiredAmt > 0 && resource.getStockInInt() - requiredAmt >= 0)?"Not Bookable":"Stock not enough", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(),"Please fill all the fields!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}

