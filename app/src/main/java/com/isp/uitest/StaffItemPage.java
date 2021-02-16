package com.isp.uitest;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isp.uitest.Class.ResourceSet;
import com.isp.uitest.Class.Staff;
import com.isp.uitest.Class.Student;
import com.isp.uitest.ui.login.LoginInfo;

public class StaffItemPage extends Fragment {
    TextView itemName;
    TextView location;
    EditText amt;
    LoginInfo info;
    ImageButton book;
    FragmentManager manager;
    final DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference rsc_root = mRootDatabaseRef.child("Resource");

    public StaffItemPage() {
        super(R.layout.activity_staff_item_page);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // initialise view
        final View rootView = inflater.inflate(R.layout.activity_staff_item_page, null);
        itemName = rootView.findViewById(R.id.item_name);
        location = rootView.findViewById(R.id.item_loc1);
        amt = rootView.findViewById(R.id.item_editstock);
        book = rootView.findViewById(R.id.book);
        info = (LoginInfo)getActivity().getApplication();


        final ResourceSet.Resource selectedResource = info.getSelectedResource();
        //get the selected item if logged in
        if (info.isLogin()){
            if (info.getSelectedResource() != null){
                itemName.setText(selectedResource.getName());
                location.setText(selectedResource.getLocation());
                amt.setText(selectedResource.getStock());
            }
        }

        //Update stock
        amt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (info.isLogin() && info.getLoginUser() instanceof Staff && !amt.getText().toString().equals("")) {
                        //Perform your Actions here.
                        rsc_root.child(selectedResource.getId()).child("amt").setValue(Long.valueOf(amt.getText().toString()));
                        selectedResource.setStock(Integer.parseInt(amt.getText().toString()));
                        Toast.makeText(getActivity(),"Updated!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getActivity(),amt.getText().toString().equals("")?"Invalid input":"Permission Denied", Toast.LENGTH_LONG).show();
                        amt.setText(selectedResource.getStock());
                        //System.out.println(info.getLoginUser().getRole());
                    }
                    handled = true;
                }
                return handled;
            }
        });

        book.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(info.isLogin() && ((info.getLoginUser() instanceof Staff && info.getSelectedResource().isBookable_by_staff()) || (info.getLoginUser() instanceof Student && info.getSelectedResource().isBookable_by_stu()))) {
                   /*manager = getActivity().getSupportFragmentManager();
                    NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.nav_host_fragment);
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.booking_item);*/
                    startActivity(new Intent(getActivity(), BookingActivity.class));
                }else{
                    Toast.makeText(getActivity(),info.getLoginUser() instanceof Staff?"Item not bookable by staff":"Item not bookable by Student", Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // data initialization
        super.onCreate(savedInstanceState);
    }
}