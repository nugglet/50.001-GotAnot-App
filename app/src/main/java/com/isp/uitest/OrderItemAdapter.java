package com.isp.uitest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.isp.uitest.Class.BookingEvent;
import com.isp.uitest.Class.ResourceSet;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {
    private ArrayList<BookingEvent> bookingEvents;
    private ClickListener clickListener;

    OrderItemAdapter(ArrayList<BookingEvent> mItemList) {
        this.bookingEvents = mItemList;
    }

    @Override
    public OrderItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        return new OrderItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderItemAdapter.MyViewHolder holder, final int position) {
        final BookingEvent item = bookingEvents.get(position);
        String textDesc = "Name: " + item.getPpl_name() + "\nItem: " + item.getItem_name() +"\nQuantity: " + item.getRequired_amt() + "\nCollection: " + item.getTime_collect() + "\nReturn:" + item.getTime_return() + "\nLocation: " + item.getLocation() + "\nStatus: " + item.getStatus() ;
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
        return bookingEvents.size();
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
            //img = itemView.findViewById(R.id.itemImageOrder);
            desc = itemView.findViewById(R.id.itemDescOrder);
            itemLayout = itemView.findViewById(R.id.itemLayoutOrder);
        }
    }

    public void updateList(ArrayList newdata){
        bookingEvents = newdata;
        notifyDataSetChanged();
    }
}
