package com.example.mysqltest;


/**
 * Created by markhulia on 25/05/15.
 */
public class Globals {
    public static final String URL = "http://10.0.1.5:2604/webservice/";
    public static String itemName;
    public static String itemLocation;
    public static String itemIfno;
    public static String itemComment;
    public static String user;
    public static int itemQuantity;
    public static int itemRowNumber;

    public static int getItemQuantity() {
        return itemQuantity;
    }

    public static void setItemQuantity(int itemQuantity) {
        Globals.itemQuantity = itemQuantity;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        Globals.user = user;
    }

    public static int getItemRowNumber() {
        return itemRowNumber;
    }

    public static void setItemRowNumber(int itemRowNumber) {
        Globals.itemRowNumber = itemRowNumber;
    }
}
