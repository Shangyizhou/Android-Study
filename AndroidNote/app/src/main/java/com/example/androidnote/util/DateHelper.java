package com.example.androidnote.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

//日期格式化类
public class DateHelper {

    protected static final String DISPLAY_WEEK_FORMAT = "EEEE";
    protected static final String DISPLAY_DATE_FORMAT = "yyyy年MM月dd日";

    public static class DatePeriod { //时间段
        public long begin;
        public long end;

        public DatePeriod(long begin, long end) {
            this.begin = begin;
            this.end = end;
        }
    }

    //当前时间
    public static Calendar getToday() {
        return Calendar.getInstance();
    }

    //返回前期日期的前一天
    public static Calendar getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(GregorianCalendar.DAY_OF_MONTH, -1);
        return calendar;
    }

    //返回一年的时间段
    public static DatePeriod getYearPeriod(int year) {
        Calendar calendar = new GregorianCalendar(year, 0, 0);
        long begin = calendar.getTimeInMillis();
        calendar.add(GregorianCalendar.YEAR, 1);
        long end = calendar.getTimeInMillis();
        return new DatePeriod(begin, end);
    }

    //返回一月的时间段
    public static DatePeriod getMonthPeriod(int year, int month) {
        Calendar calendar = new GregorianCalendar(year, month, 1);
        long begin = calendar.getTimeInMillis();
        calendar.add(GregorianCalendar.MONTH, 1);
        long end = calendar.getTimeInMillis();
        return new DatePeriod(begin, end);
    }

    //返回一天的时间段
    public static DatePeriod getDatePeriod(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);
        long begin = calendar.getTimeInMillis();
        calendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
        long end = calendar.getTimeInMillis();
        return new DatePeriod(begin, end);
    }

    //格式化日期数据
    public static DatePeriod getDatePeriod(Calendar calendar) {
        return getDatePeriod(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    //格式化周数据
    public static DatePeriod getWeekPeriod(Calendar calendar) {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long begin = calendar.getTimeInMillis();
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        long end = calendar.getTimeInMillis();
        return new DatePeriod(begin, end);
    }

    //格式化月份数据
    public static DatePeriod getMonthPeriod(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long begin = calendar.getTimeInMillis();
        calendar.add(GregorianCalendar.MONTH, 1);
        long end = calendar.getTimeInMillis();
        return new DatePeriod(begin, end);
    }

    //格式化周数据
    public static String getWeekStr(long milliseconds) {
        return new SimpleDateFormat(DISPLAY_WEEK_FORMAT, Locale.CHINA).format(milliseconds);
    }

    //格式化日期数据
    public static String getDateStr(long milliseconds) {
        return new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.CHINA).format(milliseconds);
    }
}
