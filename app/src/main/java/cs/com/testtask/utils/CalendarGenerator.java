package cs.com.testtask.utils;


import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import java.util.ArrayList;
import java.util.Random;

import cs.com.testtask.R;
import cs.com.testtask.models.Day;
import cs.com.testtask.models.Week;

/**
 * Created by AlexCs on 6/19/2014.
 */
public class CalendarGenerator {
    private static ArrayList<Day> currentCalendarList = new ArrayList<Day>();
    private static ArrayList<Day> previousCalendarList = new ArrayList<Day>();
    private static ArrayList<Day> nextCalendarList = new ArrayList<Day>();
    private static ArrayList<Week> currentCalendar = new ArrayList<Week>();
    private static Time currentMonth = new Time();

    private static String TAG = "CC_MOB";

    private static String[] imageURLs;


    public static ArrayList<Day> getCurrentMonthList() {
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
            String printString = String.valueOf(day.Day) + " " + String.valueOf(day.Month) + " " + String.valueOf(day.Year) + " " + String.valueOf(day.getWeekDay());
            Log.v(TAG, printString);

            currentCalendarList.add(day);
        }
        return currentCalendarList;
    }

    public static ArrayList<Day> getPreviousMonthList() {
        Time today = new Time();
        previousCalendarList.clear();
        today.set(currentMonth.monthDay, currentMonth.month, currentMonth.year);
        int randomID = imageURLs.length;
        Random random = new Random();
        int month;
        int year;
        if (today.month == 0) {
            month = 11;
            year = today.year - 1;
        } else {
            month = today.month - 1;
            year = today.year;
        }
        today.set(1, month, year);

        int monthDay = today.getActualMaximum(Time.MONTH_DAY);
        for (int i = 1; i <= monthDay; i++) {
            today.set(i, month, year);
            Day day = new Day(today.toMillis(false), imageURLs[random.nextInt(randomID)], false);
            String printString = String.valueOf(day.Day) + " " + String.valueOf(day.Month) + " " + String.valueOf(day.Year) + day.imgURL;
            Log.v(TAG, printString);
            previousCalendarList.add(day);
        }
        return previousCalendarList;
    }

    public static ArrayList<Day> getNextMonthList() {
        Time today = new Time();
        nextCalendarList.clear();
        today.set(currentMonth.monthDay, currentMonth.month, currentMonth.year);
        int randomID = imageURLs.length;
        Random random = new Random();

        int month;
        int year;
        if (today.month == 11) {
            month = 0;
            year = today.year + 1;
        } else {
            month = today.month + 1;
            year = today.year;
        }
        today.set(1, month, year);

        int monthDay = today.getActualMaximum(Time.MONTH_DAY);
        for (int i = 1; i <= monthDay; i++) {
            today.set(i, month, year);
            Day day = new Day(today.toMillis(false), imageURLs[random.nextInt(randomID)], false);
            String printString = String.valueOf(day.Day) + " " + String.valueOf(day.Month) + " " + String.valueOf(day.Year) + day.imgURL;
            Log.v(TAG, printString);
            nextCalendarList.add(day);
        }

        return nextCalendarList;
    }


    public static void toCurrentMonth(Context context) {
        imageURLs = context.getResources().getStringArray(R.array.image_url);
        currentMonth.setToNow();

    }


    public static ArrayList<Week> getInitData() {
        ArrayList<Day> tempList = new ArrayList<Day>();
        currentCalendar.clear();

        tempList = getPreviousMonthList();
        tempList.addAll(getCurrentMonthList());
        tempList.addAll(getNextMonthList());


        for (int i = 0; i < tempList.size(); ) {
            Week week = new Week();
            int fierstDay = tempList.get(i).getWeekDay();
            int indexLost = i + (6 - fierstDay);
            int lastDay;
            if (indexLost <= tempList.size() - 1) {
                lastDay = 6;
            } else {
                lastDay = tempList.get(tempList.size() - 1).getWeekDay();
            }
            for (int j = 0; j <= 6; j++) {
                if (j >= fierstDay && j <= lastDay) {
                    week.add(j, tempList.get(i));
                    i++;
                } else {
                    week.add(j, null);
                }
            }
            currentCalendar.add(week);
        }
        return currentCalendar;
    }

    public static ArrayList<Week> getNextWeekMonthList(int month, int year) {
        ArrayList<Day> tempList = new ArrayList<Day>();
        ArrayList<Week> result = new ArrayList<Week>();
        currentMonth.set(1, month, year);
        Log.v("BrusD", "Generator Current Month" + month);
        tempList = getNextMonthList();

        for (int i = 0; i < tempList.size(); ) {
            Week week = new Week();
            int firstDay = tempList.get(i).getWeekDay();
            int indexLost = i + (6 - firstDay);
            int lastDay;
            if (indexLost <= tempList.size() - 1) {
                lastDay = 6;
            } else {
                lastDay = tempList.get(tempList.size() - 1).getWeekDay();
            }
            for (int j = 0; j <= 6; j++) {
                if (j >= firstDay && j <= lastDay) {
                    week.add(j, tempList.get(i));
                    i++;
                } else {
                    week.add(j, null);
                }
            }
            result.add(week);
        }
        return result;
    }

    public static ArrayList<Week> getPrevWeekMonthList(int month, int year) {
        ArrayList<Day> tempList = new ArrayList<Day>();
        ArrayList<Week> result = new ArrayList<Week>();

        currentMonth.set(1, month, year);
        tempList = getPreviousMonthList();
        for (int i = 0; i < tempList.size(); ) {
            Week week = new Week();
            int fierstDay = tempList.get(i).getWeekDay();
            int indexLost = i + (6 - fierstDay);
            int lastDay;
            if (indexLost <= tempList.size() - 1) {
                lastDay = 6;
            } else {
                lastDay = tempList.get(tempList.size() - 1).getWeekDay();
            }
            for (int j = 0; j <= 6; j++) {
                if (j >= fierstDay && j <= lastDay) {
                    week.add(j, tempList.get(i));
                    i++;
                } else {
                    week.add(j, null);
                }
            }
            result.add(week);
        }
        return result;
    }
}
