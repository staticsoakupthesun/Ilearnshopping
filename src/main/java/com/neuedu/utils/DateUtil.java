package com.neuedu.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtil {

    private static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:ss";
    /**
     * 将date转成String
     * **/
    public static String dateToStr(Date date,String formate){
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(formate);
    }

    public static String dateToStr(Date date){
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }

    //String-->Date

    public static Date strToDate(String str){
            DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(STANDARD_FORMAT);
            DateTime dateTime= dateTimeFormatter.parseDateTime(str);
            return dateTime.toDate();

    }

    public static Date strToDate(String str,String format){
        DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(format);
        DateTime dateTime= dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();

    }
}
