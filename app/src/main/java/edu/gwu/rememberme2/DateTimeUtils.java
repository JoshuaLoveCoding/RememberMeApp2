package edu.gwu.rememberme2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static String getTimeString(Calendar calendar) {
        return getTimeString(calendar.getTime());
    }

    public static String getTimeString(Date date) {
        String pattern = "EEE, LLL d, hh:mm aaa";
        // If the year of the date is different from the current year
        // show the year in the string instead of the day of the week.
        return new SimpleDateFormat(pattern).format(date);
    }
}
