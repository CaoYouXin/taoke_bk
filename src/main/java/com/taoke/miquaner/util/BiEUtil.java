package com.taoke.miquaner.util;

import com.taoke.miquaner.data.EBiItemDetailClicked;

import java.util.Calendar;
import java.util.Date;

public class BiEUtil {

    private static final ThreadLocal<Calendar> CALENDAR = new ThreadLocal<>();

    public static void set(EBiItemDetailClicked entity, Date date) {
        entity.setTimePoint(date);

        Calendar calendar = CALENDAR.get();
        if (null == calendar) {
            calendar = Calendar.getInstance();
            CALENDAR.set(calendar);
        }

        calendar.setTime(date);
        entity.setYear((short) calendar.get(Calendar.YEAR));
        entity.setMonth((byte) calendar.get(Calendar.MONTH));
        entity.setDay((byte) calendar.get(Calendar.DAY_OF_MONTH));
        entity.setHour((byte) calendar.get(Calendar.HOUR_OF_DAY));
        entity.setMinute((byte) calendar.get(Calendar.MINUTE));
        entity.setSecond((byte) calendar.get(Calendar.SECOND));
    }

}
