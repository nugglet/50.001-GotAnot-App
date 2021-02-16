package com.isp.uitest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isp.uitest.Class.BookingEvent;
import com.isp.uitest.Class.Student;
import com.isp.uitest.Class.User;
import com.isp.uitest.ui.login.LoginInfo;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public Toolbar toolbar;
    public ActionBarDrawerToggle toggle;
    public FragmentManager manager;
    public Fragment fragment = null;
    public SearchView searchBar;
    public View toolbarLayout;
    final DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference evt_root = mRootDatabaseRef.child("Event");
    LoginInfo info;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        info = (LoginInfo) getApplication();
        user = info.getLoginUser();

        drawerLayout = findViewById(R.id.nav_drawer_layout);
        navigationView = findViewById(R.id.fragment_navBar);
        toolbar = findViewById(R.id.nav_toolbar);

        toolbarLayout = findViewById(R.id.nav_toolbar);
        searchBar = (SearchView) toolbarLayout.findViewById(R.id.searchBar);
        /*searchBar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(DrawerActivity.this, R.id.nav_host_fragment);
                navController.navigate(R.id.search_test);
            }
        });*/


        setSupportActionBar(toolbar);
        toggle = setupDrawerToggle();

        /*------Navigation Drawer Menu---------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navOpen, R.string.navClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (info.getLoginUser() instanceof Student){
            navigationView.getMenu().removeItem(R.id.add_resource);
        }


        navigationView.setNavigationItemSelectedListener(this);

        /*----------Fragments------------*/
        manager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                NavController navController = Navigation.findNavController(DrawerActivity.this, R.id.nav_host_fragment);
                navController.navigate(R.id.search_test);
                SearchActivity.updateSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                NavController navController = Navigation.findNavController(DrawerActivity.this, R.id.nav_host_fragment);
                navController.navigate(R.id.search_test);
                return false;
            }
        });


        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();


    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navOpen, R.string.navClose);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }*/


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Boolean res =  NavigationUI.onNavDestinationSelected(item, navController);
        Log.i("profile", res.toString());
        return res;
    }


        /*manager = getSupportFragmentManager();
        Class fragmentClass = MainActivity.class;

        switch (item.getItemId()){
            case R.id.nav_home:
                fragmentClass = MainActivity.class;
                break;

            case R.id.nav_profile:
                Log.i("tag", "profile clicked");
                fragmentClass = ProfileActivity.class;
                break;

            case R.id.itemTest_button:
                fragmentClass = ItemPage.class;
                break;

            case R.id.nav_history:
                fragmentClass = OrderHistory.class;
                break;

            case R.id.staffItem_button:
                fragmentClass = StaffItemPage.class;
                break;

            case R.id.search_test:
                fragmentClass = SearchActivity.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        manager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();

        item.setChecked(true);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;*/

}
