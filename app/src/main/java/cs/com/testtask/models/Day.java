package cs.com.testtask.models;

import android.text.format.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AlexCs on 6/19/2014.
 */
public class Day {
    private static Time date = new Time();;

    public String imgURL;
    public int day;
    public int month;
    public int year;

    private int dayOfWeek;
    private boolean isCurrentMonthDay;
    private String monthName;

    public Day(long dateInMilliseconds , String imgURL, boolean isCurrMonthDay){
        this.date.set(dateInMilliseconds);
        this.imgURL = imgURL;

        this.day = date.monthDay;
        this.month =  date.month;
        this.year = date.year;
        this.dayOfWeek = date.weekDay;
        this.isCurrentMonthDay = isCurrMonthDay;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateInMilliseconds);
        monthName = new SimpleDateFormat("MMM").format(cal.getTime());

    }

    public int getWeekDay(){
        return dayOfWeek;
    }

    public boolean isCurrentMonthDay(){
        return isCurrentMonthDay;
    }

    public void setIsCorrentMonthDay(boolean isCurrent){
        isCurrentMonthDay = isCurrent;
    }

    public String getMonthName(){
        return monthName;
    }
}
