package com.company;

public class Main {

    public static void main(String[] args) {
        ItemsDB db = new ItemsDB();
        AppGUI gui = new AppGUI(db);
        DecimalPlacesInTable decimal= new DecimalPlacesInTable();
    }
}
