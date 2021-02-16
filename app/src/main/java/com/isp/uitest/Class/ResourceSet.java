package com.isp.uitest.Class;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class ResourceSet {
    //To add a resource, simply call recourseSet.all_can_access_no_need_return_item(args) and it will be added into the Recourse Set
    public static class Resource {
        //location should be a 3 digit index
        private static String makeId(String location, String name_displayed) {
            return location + name_displayed;
        }

        String[] categories;
        String name;
        String location;
        int amt;
        String id;
        boolean bookable_by_stu;
        boolean bookable_by_staff;
        boolean no_need_return;

        //For creating a recourse
        public Resource(String name, String locationCode, long amt, String[] categories,
                        boolean bookable_by_stu, boolean bookable_by_staff, boolean no_need_return) {
            this.name = name;

            this.location = "Unknown";
            if (locationCode.equals("001")) this.location = "DSL";
            if (locationCode.equals("002")) this.location = "Physics Lab";
            if (locationCode.equals("003")) this.location = "Fab Lab";
            if (locationCode.equals("004")) this.location = "ARMS lab";

            this.amt = (int) amt;
            this.id = makeId(locationCode, this.name);
            this.bookable_by_staff = bookable_by_staff;
            this.bookable_by_stu = bookable_by_stu;
            this.no_need_return = no_need_return;
            this.categories = categories;
        }

        @Override
        public String toString(){ return "id: " + name + " with qtn: " + amt + "@" + location; }

        void updateStock(Resource resource, int add_amt, User user){
            //Only allow this when user is instance of staff
            //TODO: DB implementation needed here to update the stock
        }

        public String getName() { return name; }

        public String getLocation() { return location; }

        public String getStock() { return Integer.toString(amt); }

        public int getStockInInt() { return amt; }

        public String getId() { return id; }

        public void setStock(int stock) { this.amt = stock; }

        public boolean isBookable_by_stu(){ return bookable_by_stu; };

        public boolean isBookable_by_staff(){ return bookable_by_staff; };

        public boolean is_no_need_return() { return no_need_return; }

    }


    //Searching Methods

    //1
    public static ArrayList<Resource> accurateSearch(String query, HashMap<String, HashMap> resourceMap) {
        ArrayList<String> resourceListFromDB = new ArrayList<>(resourceMap.keySet());
        String[] queryArray = query.split(" ");
        ArrayList<Resource> result = new ArrayList<>();

        String[] categories;
        String name;
        String location;
        long amt;
        boolean bookable_by_stu;
        boolean bookable_by_staff;
        boolean no_need_return;

        for (int i = 0; i <= queryArray.length - 1; i++) queryArray[i] = queryArray[i].toLowerCase();
        //Accurate Search
        for (String id : resourceListFromDB) {
            HashSet<String> itemFeature = new HashSet<>(Arrays.asList(id.substring(3).toLowerCase().split(" ")));
            System.out.println(itemFeature);
            for (String keyword : queryArray) {
                if (itemFeature.contains(keyword.toLowerCase())) {
                    HashMap rawResource = resourceMap.get(id);
                    categories = ((String) rawResource.get("categories")).split(" ");
                    name = ((String) rawResource.get("name"));
                    location = ((String) rawResource.get("location"));
                    amt = ((long)rawResource.get("amt"));
                    bookable_by_stu = ((boolean) rawResource.get("bookable_by_stu"));
                    bookable_by_staff = ((boolean) rawResource.get("bookable_by_staff"));
                    no_need_return = ((boolean) rawResource.get("no_need_return"));
                    result.add(new Resource(name,location,amt,categories,bookable_by_stu,bookable_by_staff,no_need_return));
                    break;
                }
            }
        }
        System.out.println(result);
        System.out.println("Accurate Result: " + result);
        return result;
    }

    //2
    public static ArrayList<Resource> wideSearch(String query, HashMap<String, HashMap> resourceMap) {
        String[] queryArray = query.split(" ");
        ArrayList<Resource> result = new ArrayList<>();
        ArrayList<String> resourceListFromDB = new ArrayList<>(resourceMap.keySet());

        String[] categories;
        String name;
        String location;
        long amt;
        boolean bookable_by_stu;
        boolean bookable_by_staff;
        boolean no_need_return;

        //Only have the first letter of q
        for (int i = 0; i <= queryArray.length - 1; i++) {
            queryArray[i] = queryArray[i].substring(0, 1).toLowerCase();
        }

        //Accurate Search
        for (String id : resourceListFromDB) {
            ArrayList<String> feature = new ArrayList<>(Arrays.asList(id.substring(3).toLowerCase().split(" ")));
            for (int i = 0; i <= feature.size() - 1; i++) feature.set(i,feature.get(i).substring(0,1));
            HashSet<String> itemFeature = new HashSet<>(feature);
            System.out.println("widLib " + itemFeature);
            for (String keyword : queryArray) {
                if (itemFeature.contains(keyword.toLowerCase())) {
                    HashMap rawResource = resourceMap.get(id);
                    categories = ((String) rawResource.get("categories")).split(" ");
                    name = ((String) rawResource.get("name"));
                    location = ((String) rawResource.get("location"));
                    amt = ((long)rawResource.get("amt"));
                    bookable_by_stu = ((boolean) rawResource.get("bookable_by_stu"));
                    bookable_by_staff = ((boolean) rawResource.get("bookable_by_staff"));
                    no_need_return = ((boolean) rawResource.get("no_need_return"));
                    result.add(new Resource(name,location,amt,categories,bookable_by_stu,bookable_by_staff,no_need_return));
                    break;
                }
            }
        }
        System.out.println("Wide Result: " + result);
        return result;
    }
}
