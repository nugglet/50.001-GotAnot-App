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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class SearchActivity extends Fragment {


    private RecyclerView recyclerView;
    public static SearchItemAdapter recyclerviewItemAdapter;
    public static HashMap<String,HashMap> resourceMap = new HashMap<String,HashMap>();
    public static ArrayList<ResourceSet.Resource> searchResult = new ArrayList<>();
    public FragmentManager manager;
    final DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference rsc_root = mRootDatabaseRef.child("Resource");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_search, container, false);
        recyclerView = rootView.findViewById(R.id.recycleViewSearch);

        recyclerviewItemAdapter = new SearchItemAdapter(searchResult);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerviewItemAdapter);

        recyclerviewItemAdapter.setOnItemClickListener(new ClickListener<ResourceSet.Resource>() {
            @Override
            public void onClick(View view,  ResourceSet.Resource data, int position) {
                LoginInfo info = (LoginInfo)getActivity().getApplication();
                info.setSelectedResource(data);
                manager = getActivity().getSupportFragmentManager();
                NavHostFragment navHostFragment = (NavHostFragment) manager.findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.staffItem_button);
            }
        });
        rsc_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resourceMap = (HashMap<String, HashMap>) snapshot.getValue();
                System.out.println(resourceMap);
                //Toast.makeText(getActivity(),"Sync Successfully", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});
        return rootView;
    }

    public static void updateSearch(String searchQuery) {
        if (resourceMap != null) searchResult = ResourceSet.accurateSearch(searchQuery,resourceMap);
        if (resourceMap != null && searchResult.size() == 0) searchResult = ResourceSet.wideSearch(searchQuery,resourceMap);
        recyclerviewItemAdapter.updateList(searchResult);
    }

    public static void updateSearch(ArrayList<ResourceSet.Resource> resultsArr){
        searchResult = resultsArr;
        recyclerviewItemAdapter.updateList(searchResult);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}