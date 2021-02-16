package com.isp.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isp.uitest.Class.BookingEvent;
import com.isp.uitest.Class.Student;
import com.isp.uitest.Class.User;
import com.isp.uitest.ui.login.LoginActivity;
import com.isp.uitest.ui.login.LoginInfo;

import java.io.DataOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistory extends Fragment {
    LoginInfo info;
    User user;
    private TabLayout tabLayout;
    private int tabPosition = 0;
    private RecyclerView recyclerView;
    private static ArrayList<BookingEvent> bookingEvents = new ArrayList<>();
    private ArrayList<BookingEvent> allEvents = new ArrayList<>();
    private static OrderItemAdapter recyclerviewItemAdapter;
    final DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference evt_root = mRootDatabaseRef.child("Event");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_order, container, false);
        info = (LoginInfo)getActivity().getApplication();
        user = info.getLoginUser();
        bookingEvents = info.getBookingEvents();
        recyclerView = rootView.findViewById(R.id.recycleViewOrder);
        recyclerviewItemAdapter = new OrderItemAdapter(bookingEvents);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerviewItemAdapter);

        tabLayout = rootView.findViewById(R.id.history_tabs);
        //TODO Add function to switch data for recyclerview, case 0 is bookingevent for user, case 1 is all booking events
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println(tab.getPosition());
                if(tab.getPosition() == 0) {
                    tabPosition = 0;
                    Toast.makeText(getActivity(), "Order Tab", Toast.LENGTH_SHORT).show();
                    System.out.println("booking events: " + bookingEvents);
                    recyclerviewItemAdapter.updateList(bookingEvents);
                    recyclerviewItemAdapter.setOnItemClickListener(new ClickListener() {
                        @Override
                        public void onClick(View view, Object data, int position) {
                        }
                    });
                }
                else{
                    tabPosition = 1;
                        Toast.makeText(getActivity(), "Logs Tab", Toast.LENGTH_SHORT).show();
                        System.out.println("all events: " + allEvents);
                        recyclerviewItemAdapter.updateList(allEvents);
                        recyclerviewItemAdapter.setOnItemClickListener(new ClickListener() {
                            @Override
                            public void onClick(View view, Object data, int position) {
                                if(((BookingEvent) data).getTime_return().equals("NA")){
                                    if(((BookingEvent) data).getStatus().equals("to be collected")){
                                        evt_root.child(((BookingEvent) data).getEvent_id()).child("status").setValue("collected");
                                    }
                                    else{
                                        evt_root.child(((BookingEvent) data).getEvent_id()).child("status").setValue("to be collected");
                                    }
                                }
                                else{
                                    if(((BookingEvent) data).getStatus().equals("to be collected")){
                                        evt_root.child(((BookingEvent) data).getEvent_id()).child("status").setValue("to be returned");
                                    }
                                    else if (((BookingEvent) data).getStatus().equals("to be returned")){
                                        evt_root.child(((BookingEvent) data).getEvent_id()).child("status").setValue("returned");
                                    }
                                    else{
                                        evt_root.child(((BookingEvent) data).getEvent_id()).child("status").setValue("to be collected");
                                    }
                                }
                            }
                        });

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if(info.getLoginUser() instanceof Student) {
            View tablay = rootView.findViewById(R.id.history_tabs);
            ((ViewGroup) tablay.getParent()).removeView(tablay);
        }else{

        }
        evt_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingEvents = new ArrayList<>();
                System.out.println("------>" + user.getEventIDs());
                try {
                    //Personal events
                    bookingEvents.clear();
                    for (String id:user.getEventIDs()) {
                        HashMap<String, Object> evt = (HashMap<String, Object>) snapshot.child(id).getValue();
                        String event_id = id;
                        int required_amt = Integer.parseInt(evt.get("amt").toString());
                        String item_id = (String) evt.get("item_id");
                        String item_name = (String) evt.get("item_name");
                        String location = (String) evt.get("location");
                        String ppl_id = evt.get("ppl_id").toString();
                        String ppl_name = (String) evt.get("ppl_name");
                        String status = (String) evt.get("status");
                        String time_collect = (String) evt.get("time_collect");
                        String time_return = (String) evt.get("time_return");
                        bookingEvents.add(new BookingEvent(event_id, required_amt, item_id, item_name,
                                location, ppl_id, ppl_name, status, time_collect, time_return));

                    }

                    Toast.makeText(getActivity().getApplication(),user.getEventIDs().size() == 0?"No events to view!":"Events ready to view!", Toast.LENGTH_LONG).show();
                    //recyclerviewItemAdapter.updateList(bookingEvents);
                    //All Events (Staff only)
                    if (user.getRole().equals("Staff")){
                        HashMap<String, Object> staff_evt = (HashMap<String, Object>) snapshot.getValue();
                        System.out.println(staff_evt);
                        allEvents.clear();
                        for (String id: staff_evt.keySet()) {
                            if (!(id.equals("next") || id.equals("z"))) {
                                System.out.println(id);
                                HashMap<String, Object> singleEvt = (HashMap<String, Object>) staff_evt.get(id);
                                String event_id = id;
                                int required_amt = Integer.parseInt(singleEvt.get("amt").toString());
                                String item_id = (String) singleEvt.get("item_id");
                                String item_name = (String) singleEvt.get("item_name");
                                String location = (String) singleEvt.get("location");
                                String ppl_id = singleEvt.get("ppl_id").toString();
                                String ppl_name = (String) singleEvt.get("ppl_name");
                                String status = (String) singleEvt.get("status");
                                String time_collect = (String) singleEvt.get("time_collect");
                                String time_return = (String) singleEvt.get("time_return");
                                allEvents.add(new BookingEvent(event_id, required_amt, item_id, item_name,
                                        location, ppl_id, ppl_name, status, time_collect, time_return));
                            }
                        }
                        //System.out.println(allEvents);
                        Toast.makeText(getActivity().getApplication(),"Logs ready to view!", Toast.LENGTH_LONG).show();
                    }

                } catch (NullPointerException e) {
                    try {
                        Toast.makeText(getActivity().getApplication(), "No events to view!", Toast.LENGTH_LONG).show();
                    }
                    catch (Exception ee){ }
                }
                if(tabPosition == 0) {
                    recyclerviewItemAdapter.updateList(bookingEvents);
                }
                else{
                    recyclerviewItemAdapter.updateList(allEvents);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // data initialization
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        evt_root.child("z").setValue(0);
        System.out.println("back");
    }
}