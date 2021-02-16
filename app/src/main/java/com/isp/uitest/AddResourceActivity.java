package com.isp.uitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isp.uitest.Class.ResourceSet;
import com.isp.uitest.ui.login.LoginInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddResourceActivity extends Fragment {

    public static HashMap<String,HashMap> resourceMap = new HashMap<String,HashMap>();
    public FragmentManager manager;
    final DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference rsc_root = mRootDatabaseRef.child("Resource");
    String itemId;
    EditText itemAmt;
    Spinner itemLocation;
    CheckBox item_bookable_staff;
    CheckBox item_bookable_stu;
    //CheckBox item_categories;
    //TODO: add item_location(Select) corresponding to recourse code 001 002 003 004
    //if (locationCode.equals("001")) this.location = "DSL";
    //            if (locationCode.equals("002")) this.location = "Physics Lab";
    //            if (locationCode.equals("003")) this.location = "Fab Lab";
    //            if (locationCode.equals("004")) this.location = "Electronic lab";
    EditText item_name;
    CheckBox item_no_return;
    Button update_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_resource_activity, container, false);
        item_name = rootView.findViewById(R.id.add_name);
        itemAmt = rootView.findViewById(R.id.add_amt);
        itemLocation = rootView.findViewById(R.id.add_location);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemLocation.setAdapter(adapter);
        item_no_return = rootView.findViewById(R.id.add_return);
        item_bookable_stu = rootView.findViewById(R.id.bookable_by_stu);
        item_bookable_staff = rootView.findViewById(R.id.bookable_by_staff);
        update_button = rootView.findViewById(R.id.add_submit);
        rsc_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resourceMap = (HashMap<String, HashMap>) snapshot.getValue();
                System.out.println(resourceMap);
                //Toast.makeText(getActivity(),"Sync Successfully", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String locationCode = locToId(itemLocation.getSelectedItem().toString());
                itemId = locationCode + item_name.getText().toString();
                try {
                    if (resourceMap == null) resourceMap = new HashMap<String,HashMap>();
                    if (resourceMap.get(itemId) == null) {
                        rsc_root.child(itemId).child("amt").setValue(Long.valueOf(itemAmt.getText().toString()));
                        rsc_root.child(itemId).child("bookable_by_staff").setValue(item_bookable_staff.isChecked());
                        rsc_root.child(itemId).child("bookable_by_stu").setValue(item_bookable_stu.isChecked());
                        rsc_root.child(itemId).child("no_need_return").setValue(item_no_return.isChecked());
                        //Category should be a long string, splited by space
                        rsc_root.child(itemId).child("categories").setValue("Default");
                        rsc_root.child(itemId).child("location").setValue(locationCode);
                        rsc_root.child(itemId).child("name").setValue(item_name.getText().toString());
                        System.out.println("added");
                        Toast.makeText(getActivity().getApplication(), "Item added successfully!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity().getApplication(), "This resource is already in our library!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    item_name.setText("");
                    itemAmt.setText("");
                    if(item_bookable_staff.isChecked()){
                        item_bookable_staff.toggle();
                    }
                    if(item_bookable_stu.isChecked()){
                        item_bookable_stu.toggle();
                    }
                    if(item_no_return.isChecked()){
                        item_no_return.toggle();
                    }
                }
            }
        });


        return rootView;
    }

    public String locToId(String location){
        switch(location){
            case "DSL":
                return "001";
            case "Physics Lab":
                return "002";
            case "Fab Lab":
                return "003";
            case"ARMS Lab":
                return "004";
            default:
                return "000";
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
