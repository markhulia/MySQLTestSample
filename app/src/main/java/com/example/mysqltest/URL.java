package com.example.mysqltest;

/**
 * Created by markhulia on 17/05/15.
 */
public class URL {

    //emulator
    //public static final String URL = "http://10.0.2.2:2604/webservice/";

    //home
    public static final String URL = "http://10.0.1.5:2604/webservice/";
    static int rowNumber = 0;

    public static void setRowNumber(int i) {
        rowNumber++;
        return;
    }

    //Office ANDROID network
    //public static final String URL = "http://192.168.1.153:2604/webservice/";
}
