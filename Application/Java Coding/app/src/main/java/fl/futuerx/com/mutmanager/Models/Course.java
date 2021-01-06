package fl.futuerx.com.mutmanager.Models;


import java.util.ArrayList;
import java.util.Calendar;

public class Course {

    public int     id;
    public String  code;
    public String  room;
    public int     instructor_id;
    private String  start_time;
    private String  end_time;
    public int mon;
    public int tue;
    public int wed;
    public int thu;
    public int fri;
    public int sat;
    public int students;

    public String getStart_time() {
        if(start_time.endsWith(":00")){
            return start_time.substring(0, start_time.length() - 3);
        } else {
            return start_time;
        }

    }

    public String getEnd_time() {
        if(end_time.endsWith(":00")){
            return end_time.substring(0, end_time.length() - 3);
        } else {
            return end_time;
        }
    }

    public ArrayList<Integer> getDaysOfWeek() {
        ArrayList<Integer> list = new ArrayList<>();
        if(mon == 1){
            list.add(Calendar.MONDAY);
        }
        if(tue == 1){
            list.add(Calendar.TUESDAY);
        }
        if(wed == 1){
            list.add(Calendar.WEDNESDAY);
        }
        if(thu == 1){
            list.add(Calendar.THURSDAY);
        }
        if(fri == 1){
            list.add(Calendar.FRIDAY);
        }
        if(sat == 1){
            list.add(Calendar.SATURDAY);
        }
        return list;
    }
}
