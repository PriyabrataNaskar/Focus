package com.priyo.focus;

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
    
    public static int calculatePercentage(double totalTime, double timeRemaining){
        double timeElapsed = totalTime - timeRemaining;
        int percentage = (int)((timeElapsed*100)/totalTime);
        return percentage;
    }
}
