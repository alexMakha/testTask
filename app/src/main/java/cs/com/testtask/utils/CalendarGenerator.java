package cs.com.testtask.utils;


import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs.com.testtask.R;
import cs.com.testtask.models.Day;
import cs.com.testtask.models.Week;

/**
 * Created by AlexCs on 6/19/2014.
 */
public class CalendarGenerator {
    private static List<Day> currentCalendarList = new ArrayList<Day>();
    private static List<Day> previousCalendarList = new ArrayList<Day>();
    private static List<Day> nextCalendarList = new ArrayList<Day>();
    private static List<Week> currentCalendar = new ArrayList<Week>();
    private static Time currentMonth = new Time();

    private static String TAG = "ALEX_MOB";

    private static String[] imageURLs;

    public static List<Day> getCurrentMonthList() {
        Time today = new Time();
        currentCalendarList.clear();
        today = currentMonth;
        int randomID = imageURLs.length;
        Random random = new Random();
        int monthDay = today.getActualMaximum(Time.MONTH_DAY);
        int month = today.month;
        int year = today.year;

        for (int i = 1; i <= monthDay; i++) {

            today.set(i, month, year);
            Day day = new Day(today.toMillis(false), imageURLs[random.nextInt(randomID)], true);
            day.getWeekDay();
            StringBuilder sb = new StringBuilder();
            sb.append(day.day).append("-").append(day.month).append("-").append(day.year).append("-").append(day.getWeekDay());
            Log.v(TAG, sb.toString());

            currentCalendarList.add(day);
        }
        return currentCalendarList;
    }

    public static List<Day> getPreviousMonthList() {
        return fillCalendarList(previousCalendarList, 0, 11, -1);
    }

    public static List<Day> getNextMonthList() {
        return fillCalendarList(nextCalendarList, 11, 0, 1);
    }

    private static List<Day> fillCalendarList(List<Day> calendarList, int startMonth, int endMonth, int delta) {
        Time today = new Time();
        calendarList.clear();
        today.set(currentMonth.monthDay, currentMonth.month, currentMonth.year);
        int randomID = imageURLs.length;
        Random random = new Random();
        int month;
        int year;
        if (today.month == startMonth) {
            month = endMonth;
            year = today.year + delta;
        } else {
            month = today.month + delta;
            year = today.year;
        }
        today.set(1, month, year);

        int monthDay = today.getActualMaximum(Time.MONTH_DAY);
        for (int i = 1; i <= monthDay; i++) {
            today.set(i, month, year);
            Day day = new Day(today.toMillis(false), imageURLs[random.nextInt(randomID)], false);
            StringBuilder sb = new StringBuilder();
            sb.append(day.day).append("-").append(day.month).append("-").append(day.year).append(day.imgURL);
            Log.v(TAG, sb.toString());
            calendarList.add(day);
        }
        return calendarList;
    }

    public static void toCurrentMonth(Context context) {
        imageURLs = context.getResources().getStringArray(R.array.image_url);
        currentMonth.setToNow();
    }

    public static List<Week> getInitData() {
        List<Day> tmpList = new ArrayList<Day>();

        currentCalendar.clear();

        tmpList.addAll(getPreviousMonthList());
        tmpList.addAll(getCurrentMonthList());
        tmpList.addAll(getNextMonthList());

        return getMonthList(tmpList, currentCalendar);
    }

    public static List<Week> getNextWeekMonthList(int month, int year) {
        List<Day> tmpList;
        List<Week> result = new ArrayList<Week>();
        currentMonth.set(1, month, year);
        Log.v(TAG, "Generator Current month" + month);
        tmpList = getNextMonthList();

        return getMonthList(tmpList, result);
    }

    private static List<Week> getMonthList(List<Day> tmpList, List<Week> result) {
        for (int i = 0; i < tmpList.size(); ) {
            Week week = new Week();
            int firstDay = tmpList.get(i).getWeekDay();
            int indexLost = i + (6 - firstDay);
            int lastDay;
            if (indexLost <= tmpList.size() - 1) {
                lastDay = 6;
            } else {
                lastDay = tmpList.get(tmpList.size() - 1).getWeekDay();
            }
            for (int j = 0; j <= 6; j++) {
                if (j >= firstDay && j <= lastDay) {
                    week.add(j, tmpList.get(i));
                    i++;
                } else {
                    week.add(j, null);
                }
            }
            result.add(week);
        }
        return result;
    }

    public static List<Week> getPrevWeekMonthList(int month, int year) {
        List<Day> tmpList;
        List<Week> result = new ArrayList<Week>();

        currentMonth.set(1, month, year);
        tmpList = getPreviousMonthList();
        return getMonthList(tmpList, result);
    }
}
