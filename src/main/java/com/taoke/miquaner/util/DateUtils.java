package com.taoke.miquaner.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final ThreadLocal<Calendar> CALENDAR = new ThreadLocal<>();

    public static Date add(Date now, int field, int amount) {
        Calendar calendar = CALENDAR.get();
        if (null == calendar) {
            calendar = Calendar.getInstance();
            CALENDAR.set(calendar);
        }

        calendar.setTime(now);
        calendar.add(field, amount);
        return calendar.getTime();
    }

}
