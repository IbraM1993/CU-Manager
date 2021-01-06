package fl.futuerx.com.mutmanager.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import fl.futuerx.com.mutmanager.Adapter.CoursesAdapter;
import fl.futuerx.com.mutmanager.Helpers.UIHelper;
import fl.futuerx.com.mutmanager.Interfaces.DelegateCall;
import fl.futuerx.com.mutmanager.Models.Course;
import fl.futuerx.com.mutmanager.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class ClassesListFragment extends Fragment {

    private int class_id;
    private DelegateCall<Course> onCourseSelected;
    private int dayOfWeek;
    private List<Course> dayCourses;
    private RecyclerView recycler;
    private TextView emptyText;
    public static ClassesListFragment init(int class_id, DelegateCall<Course> onCourseSelected, int dayOfWeek, List<Course> dayCourses) {
        ClassesListFragment f = new ClassesListFragment();
        f.class_id          = class_id;
        f.onCourseSelected  = onCourseSelected;
        f.dayOfWeek         = dayOfWeek;
        f.dayCourses        = dayCourses;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.fragment_recycler);
        emptyText = view.findViewById(R.id.fragment_empty);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpView();
            }
        }, UIHelper.UI_SETUP_DELAY);
    }

    private void setUpView() {
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Calendar c = Calendar.getInstance();
        int today = c.get(Calendar.DAY_OF_WEEK);
        int color = -1;
        if(dayOfWeek < today){
            color = R.color.color_previous;
        } else if(dayOfWeek == today){
            color = R.color.color_today;
        } else if(dayOfWeek > today){
            color = R.color.color_tomorrow;
        }
        CoursesAdapter adapter = new CoursesAdapter(getContext(), dayOfWeek, dayCourses, color, onCourseSelected);
        recycler.setAdapter(adapter);
        if(dayCourses == null || dayCourses.size() ==0 ){
            recycler.setBackgroundResource(color);
            if(emptyText != null)
                emptyText.setVisibility(View.VISIBLE);
        } else {
            if(emptyText != null)
                emptyText.setVisibility(View.GONE);
        }
    }

}
