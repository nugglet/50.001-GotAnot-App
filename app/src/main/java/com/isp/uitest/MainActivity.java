package com.isp.uitest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isp.uitest.Class.ResourceSet;
import com.isp.uitest.ui.login.LoginInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static com.isp.uitest.ui.login.ButtonEffect.buttonEffect;

public class MainActivity extends Fragment {
    HashMap<String, HashMap> resourceMap;
    ArrayList<ResourceSet.Resource> loc_result;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // initialise view
        View rootView = inflater.inflate(R.layout.activity_main, null);

        TextView welcome;
        welcome = rootView.findViewById(R.id.welcome);
        LoginInfo info = (LoginInfo)getActivity().getApplication();
        welcome.setText("Hi, " + info.getLoginUser().getName() + "!");
        final DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference rsc_root = mRootDatabaseRef.child("Resource");
        ImageButton dsl = rootView.findViewById(R.id.DSLbutton);
        ImageButton fab = rootView.findViewById(R.id.FabLabButton);
        ImageButton arms = rootView.findViewById(R.id.arms_button);
        ImageButton phys = rootView.findViewById(R.id.phys_button);

        buttonEffect(dsl);
        buttonEffect(fab);
        buttonEffect(arms);
        buttonEffect(phys);

        rsc_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resourceMap = (HashMap<String, HashMap>) snapshot.getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});
        dsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked");
                try {
                    loc_result = searchByLocation(resourceMap,"001");
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.nav_host_fragment);
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.search_test);
                    SearchActivity.updateSearch(loc_result);
                }catch (Exception e){
                    Toast.makeText(getActivity().getApplication(), "Sync Error", Toast.LENGTH_LONG);
                }
            }
        });
        phys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked");
                try {
                    loc_result = searchByLocation(resourceMap,"002");
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.nav_host_fragment);
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.search_test);
                    SearchActivity.updateSearch(loc_result);
                }catch (Exception e){
                    Toast.makeText(getActivity().getApplication(), "Sync Error", Toast.LENGTH_LONG);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked");
                try {
                    loc_result = searchByLocation(resourceMap,"003");
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.nav_host_fragment);
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.search_test);
                    SearchActivity.updateSearch(loc_result);
                }catch (Exception e){
                    Toast.makeText(getActivity().getApplication(), "Sync Error", Toast.LENGTH_LONG);
                }
            }
        });
        arms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked");
                try {
                    loc_result = searchByLocation(resourceMap,"004");
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.nav_host_fragment);
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.search_test);
                    SearchActivity.updateSearch(loc_result);
                }catch (Exception e){
                    Toast.makeText(getActivity().getApplication(), "Sync Error", Toast.LENGTH_LONG);
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

    public ArrayList<ResourceSet.Resource> searchByLocation (HashMap<String, HashMap> resourceMap, String locationCode){
        ArrayList<String> resourceListFromDB = new ArrayList<>(resourceMap.keySet());
        ArrayList<ResourceSet.Resource> result = new ArrayList<>();

        String[] categories;
        String name;
        String location;
        long amt;
        boolean bookable_by_stu;
        boolean bookable_by_staff;
        boolean no_need_return;

        for (String id : resourceListFromDB) {
            if (id.substring(0,3).equals(locationCode)) {
                    HashMap rawResource = resourceMap.get(id);
                    categories = ((String) rawResource.get("categories")).split(" ");
                    name = ((String) rawResource.get("name"));
                    location = ((String) rawResource.get("location"));
                    amt = ((long)rawResource.get("amt"));
                    bookable_by_stu = ((boolean) rawResource.get("bookable_by_stu"));
                    bookable_by_staff = ((boolean) rawResource.get("bookable_by_staff"));
                    no_need_return = ((boolean) rawResource.get("no_need_return"));
                    result.add(new ResourceSet.Resource(name,location,amt,categories,bookable_by_stu,bookable_by_staff,no_need_return));
                }
            }
        System.out.println(result);
        return result;
    }
}

    /*//Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    //Managers


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout =findViewById(R.id.drawer_layout_main);
        navigationView = findViewById(R.id.navBar);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        *//*------Navigation Drawer Menu---------*//*

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navOpen, R.string.navClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Log.i("tag", "home pressed");
                //Intent intent = new Intent(navigationView.getContext(), MainActivity.class);
                //startActivity(intent);
                break;

            case R.id.nav_profile:
                Log.i("tag", "profile pressed");
                Intent intent = new Intent(navigationView.getContext(), ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.itemTest_button:
                Intent itemIntent = new Intent(navigationView.getContext(), ItemPage.class);
                startActivity(itemIntent);
                break;

            case R.id.nav_history:
                Intent histIntent = new Intent(navigationView.getContext(), OrderHistory.class);
                startActivity(histIntent);
                break;

            case R.id.staffItem_button:
                Intent staffIntent = new Intent(navigationView.getContext(), StaffItemPage.class);
                startActivity(staffIntent);
                break;

            case R.id.search_test:
                Intent searchIntent = new Intent(navigationView.getContext(), SearchActivity.class);
                startActivity(searchIntent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }*/

