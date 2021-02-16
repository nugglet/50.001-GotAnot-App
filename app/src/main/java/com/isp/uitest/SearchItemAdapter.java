package com.isp.uitest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.isp.uitest.Class.ResourceSet;

import java.util.ArrayList;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.MyViewHolder> {

    private ArrayList<ResourceSet.Resource> itemsList;
    private ClickListener clickListener;

    SearchItemAdapter(ArrayList<ResourceSet.Resource> mItemList) {
        this.itemsList = mItemList;
    }

    @Override
    public SearchItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchItemAdapter.MyViewHolder holder, final int position) {
        final ResourceSet.Resource item = itemsList.get(position);
        String textDesc = "Name: " + item.getName() + "\nLocation: " + item.getLocation() + "\nStock: " + item.getStock();
        holder.desc.setText(textDesc);

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView desc;
        private LinearLayout itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            //img = itemView.findViewById(R.id.itemImageSearch);
            desc = itemView.findViewById(R.id.itemDescSearch);
            itemLayout = itemView.findViewById(R.id.itemLayoutSearch);
        }
    }

    public void updateList(ArrayList newdata){
        itemsList = newdata;
        notifyDataSetChanged();
    }
}
