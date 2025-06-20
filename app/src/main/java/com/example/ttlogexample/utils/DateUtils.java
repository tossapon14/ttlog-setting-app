package com.example.ttlogexample.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {
    public static String getMillsTimeFormat(long date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}
