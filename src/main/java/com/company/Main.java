package com.company;

public class Main {

    public static void main(String[] args) {
        ItemsDB db = new ItemsDB();
        AppGUI gui = new AppGUI(db);

        //This was the decimal class I attempted to work on to fix the Jtable currency issue.
        //DecimalPlacesInTable decimal= new DecimalPlacesInTable();
    }
}
