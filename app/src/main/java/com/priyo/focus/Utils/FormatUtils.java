package com.priyo.focus.Utils;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import java.util.Date;

/**
 * Created by Priyabrata Naskar on 03-03-2022.
 */
public class FormatUtils {

    /**
     * Format time in MM : SS or MMM : SS format
     * @param seconds: Total Time in Second
     * @return String with formatted Time
     */
    public static String formatTime(int seconds){
        int min = (int) (seconds/60.0);
        int sec = seconds%60;
        if (min<100) {
            return String.format("%02d : %02d", min, sec);
        }else {
            return String.format("%03d : %02d", min, sec);
        }
    }

    /**
     * Calculating percentage of work done
     * @param totalTime
     * @param timeRemaining
     * @return {percentage} + 1
     */
    public static int calculatePercentage(double totalTime, double timeRemaining){
        double timeElapsed = totalTime - timeRemaining;
        int percentage = (int)((timeElapsed*100)/totalTime);
        return percentage + 1;
    }

    /**
     *
     * @return formatted date in d MMM format
     * e.g: 03 January
     */
    public static String getDateMonth() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     *
     * @return formatted formatted current time hh:mm format
     * e.g: 11:00, 20:00
     */
    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
