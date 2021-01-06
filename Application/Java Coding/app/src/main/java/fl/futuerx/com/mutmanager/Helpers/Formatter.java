package fl.futuerx.com.mutmanager.Helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fl.futuerx.com.mutmanager.Models.Course;


public class Formatter {
    private static final Formatter ourInstance = new Formatter();

    public static Formatter getInstance() {
        return ourInstance;
    }

    private Formatter() {
    }

    public String formatCourseDate(Course c) {
        String time = "";
        if(c.mon == 1){
            time = "M";
        }
        if(c.tue == 1){
            time +=(time.length() > 0 ? ", " : "") + "TU";
        }
        if(c.wed == 1){
            time += (time.length() > 0 ? ", " : "") + "W";
        }
        if(c.thu == 1){
            time += (time.length() > 0 ? ", " : "") + "TH";
        }
        if(c.fri == 1){
            time += (time.length() > 0 ? ", " : "") + "F";
        }
        if(c.sat == 1){
            time += (time.length() > 0 ? ", " : "") + "S";
        }
        return time;
    }

    public boolean isNow(String startTime, String endTime, ArrayList<Integer> dayOfWeek, int today){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String current = dateFormat.format(new Date());
        if(dayOfWeek == null){
            return false;
        }
        boolean b = false;
        String todayToday = formatDate(new Date());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, today);
        c.getTimeInMillis();
        if(!todayToday.equals(formatDate(c.getTime()))){
            return false;
        }
//        int firstDayOfWeek = Calendar.getInstance().getFirstDayOfWeek();
        for(int i : dayOfWeek){
            if(today == i){
                b = true;
                break;
            }
        }
        return current.compareTo(startTime) >= 0 && current.compareTo(endTime) <= 0 &&  b ;
    }

    public String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public String formatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }
}
