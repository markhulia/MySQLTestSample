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
    public static String itemId;
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

    public static String getItemComment() {
        return itemComment;
    }

    public static void setItemComment(String itemComment) {
        Globals.itemComment = itemComment;
    }

    public static String getItemId() {
        return itemId;
    }

    public static void setItemId(String itemId) {
        Globals.itemId = itemId;
    }

    public static String getItemIfno() {
        return itemIfno;
    }

    public static void setItemIfno(String itemIfno) {
        Globals.itemIfno = itemIfno;
    }

    public static String getItemLocation() {
        return itemLocation;
    }

    public static void setItemLocation(String itemLocation) {
        Globals.itemLocation = itemLocation;
    }

    public static String getItemName() {
        return itemName;
    }

    public static void setItemName(String itemName) {
        Globals.itemName = itemName;
    }
}