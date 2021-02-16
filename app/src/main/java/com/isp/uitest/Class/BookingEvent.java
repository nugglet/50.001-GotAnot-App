package com.isp.uitest.Class;
import java.text.DecimalFormat;

public class BookingEvent {
    private String event_id;
    private int required_amt;
    private String item_id;
    private String item_name;
    private String location;
    private String ppl_id;
    private String ppl_name;
    private String status;
    private String time_collect;
    private String time_return;

    public BookingEvent(ResourceSet.Resource resource, String time_collect, String time_return, User ppl, int required_amt, String event_id) {
        this.event_id = event_id;
        this.required_amt = required_amt;
        this.item_id = resource.getId();
        this.item_name = resource.getName();
        this.location = resource.getLocation();
        this.ppl_id = ppl.getId();
        this.ppl_name = ppl.getName();
        this.status = "To be collected";
        this.time_collect = time_collect;
        this.time_return = time_return;
    }

    public BookingEvent(String event_id, int required_amt, String item_id, String item_name, String location, String ppl_id, String ppl_name, String status, String time_collect, String time_return) {
        this.event_id = event_id;
        this.required_amt = required_amt;
        this.item_id = item_id;
        this.item_name = item_name;
        this.location = location;
        this.ppl_id = ppl_id;
        this.ppl_name = ppl_name;
        this.status = status;
        this.time_collect = time_collect;
        this.time_return = time_return;
    }

    public String getEvent_id() {
        return event_id;
    }

    public int getRequired_amt() {
        return required_amt;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getLocation() {
        return location;
    }

    public String getPpl_id() {
        return ppl_id;
    }

    public String getPpl_name() {
        return ppl_name;
    }

    public String getStatus() {
        return status;
    }

    public String getTime_collect() {
        return time_collect;
    }

    public String getTime_return() {
        return time_return;
    }

    public static long createNextId(long nextID) {
        nextID = nextID > 9999999 ? 1 : nextID + 1;
        return nextID;
    }

    public static String createBookingId(long ID) {
        DecimalFormat df = new DecimalFormat("0000000");
        return df.format(ID);
    }
}
