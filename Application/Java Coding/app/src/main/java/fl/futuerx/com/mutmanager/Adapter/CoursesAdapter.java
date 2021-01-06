package fl.futuerx.com.mutmanager.Adapter;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fl.futuerx.com.mutmanager.Helpers.Formatter;
import fl.futuerx.com.mutmanager.Interfaces.DelegateCall;
import fl.futuerx.com.mutmanager.Models.Course;
import fl.futuerx.com.mutmanager.R;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private final DelegateCall<Course> onRowClick;
    private final int dayOfWeek;
    private final Context context;
    @ColorRes
    private final int color;
    public CoursesAdapter(Context context, int dayOfWeek, List<Course> courseList, @ColorRes int color, DelegateCall<Course> onRowClick) {
        this.courseList = courseList;
        this.onRowClick = onRowClick;
        this.context = context;
        this.color = color;
        this.dayOfWeek = dayOfWeek;

    }

    final class CourseViewHolder extends RecyclerView.ViewHolder {
        public final TextView code;
        public final TextView room;
        public final TextView date;
        public final TextView time;
        public final TextView students;
        public CourseViewHolder(View itemView) {
            super(itemView);
            code     = itemView.findViewById(R.id.row_course_code);
            room     = itemView.findViewById(R.id.row_course_room);
            date     = itemView.findViewById(R.id.row_course_date);
            time     = itemView.findViewById(R.id.row_course_time);
            students = itemView.findViewById(R.id.row_course_students);
            itemView.setBackgroundResource(color);
            if(onRowClick != null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View v = view.findViewById(R.id.row_course_code);
                        if(v.getTag() != null && v.getTag() instanceof Course){
                            onRowClick.Invoke((Course) v.getTag());
                        }
                    }
                });
            }
        }
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.row_course, parent , false));
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course c = getCourseList().get(position);
        holder.code.setText(c.code);
        holder.code.setTextColor(Formatter.getInstance().isNow(c.getStart_time(), c.getEnd_time(), c.getDaysOfWeek(), dayOfWeek)
                ? context.getResources().getColor(R.color.current_course)
                : context.getResources().getColor(R.color.colorPrimary));
        holder.room.setText(c.room);
        holder.date.setText(c.getStart_time() + " - " + c.getEnd_time());
        holder.time.setText(Formatter.getInstance().formatCourseDate(c));
        holder.students.setText(context.getString(R.string.label_students)+c.students);
        holder.code.setTag(c);
    }

    @Override
    public int getItemCount() {
        return getCourseList().size();
    }

    public List<Course> getCourseList() {
        if(courseList == null){
            courseList = new ArrayList<>();
        }
        return courseList;
    }
}
