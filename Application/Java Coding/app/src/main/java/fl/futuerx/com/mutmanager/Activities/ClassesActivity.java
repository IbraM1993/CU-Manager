package fl.futuerx.com.mutmanager.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fl.futuerx.com.mutmanager.Adapter.GenericFragmentAdapter;
import fl.futuerx.com.mutmanager.Asynctask.GetCoursesTask;
import fl.futuerx.com.mutmanager.Fragments.ClassesListFragment;
import fl.futuerx.com.mutmanager.Helpers.DataManager;
import fl.futuerx.com.mutmanager.Helpers.UIHelper;
import fl.futuerx.com.mutmanager.Interfaces.AsyncCallBack;
import fl.futuerx.com.mutmanager.Interfaces.DelegateCall;
import fl.futuerx.com.mutmanager.Models.Course;
import fl.futuerx.com.mutmanager.NFCReader.AttendanceActivity;
import fl.futuerx.com.mutmanager.R;


public class ClassesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(DataManager.getInstance().getCurrent(this).name);
        fetchData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_classes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_sign_out){
            DataManager.getInstance().setInstructor(ClassesActivity.this, null);
            finish();
        }
        return true;
    }

    private void fetchData() {

        new GetCoursesTask(ClassesActivity.this
                , DataManager.getInstance().getCurrent(ClassesActivity.this).id
                , new AsyncCallBack<List<Course>, String>(){

            @Override
            public void OnStart() {
                ShowProgress(true);

            }

            @Override
            public void OnCompleted(List<Course> result) {
                ShowProgress(false);
                if(result != null ){
                    publishData(result);
                } else {
                    OnFailed("Data Empty!");
                }
            }

            @Override
            public void OnFailed(String error) {
                ShowProgress(false);
                // TODO
                UIHelper.getInstance().ShowErrorPopUp(ClassesActivity.this, "Fetching Failed", error, null);
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void publishData(List<Course> result) {
        ViewPager pager = findViewById(R.id.activity_classes_pager);
        List<Course> monCourses = new ArrayList<>();
        List<Course> tueCourses = new ArrayList<>();
        List<Course> wedCourses = new ArrayList<>();
        List<Course> thuCourses = new ArrayList<>();
        List<Course> friCourses = new ArrayList<>();
        List<Course> satCourses = new ArrayList<>();
        for(Course c : result){
            if(c.mon == 1){
                monCourses.add(c);
            }
            if (c.tue == 1){
                tueCourses.add(c);
            }
            if (c.wed == 1){
                wedCourses.add(c);
            }
            if (c.thu == 1){
                thuCourses.add(c);
            }
            if (c.fri == 1){
                friCourses.add(c);
            }
            if (c.sat == 1){
                satCourses.add(c);
            }
        }
//        DelegateCall<Course> onCourseSelected = new DelegateCall<Course>() {
//            @Override
//            public Course Invoke(Course returnedObject) {
//                startActivity(
//                        new Intent(ClassesActivity.this, AttendanceActivity.class)
//                        .putExtra(AttendanceActivity.CLASS_ID         , returnedObject.id)
//                        .putExtra(AttendanceActivity.CLASS_START_TIME , returnedObject.getStart_time())
//                        .putExtra(AttendanceActivity.CLASS_END_TIME   , returnedObject.getEnd_time())
//                        .putExtra(AttendanceActivity.CLASS_CODE       , returnedObject.code)
//                        .putIntegerArrayListExtra(AttendanceActivity.CLASS_DAY_OF_WEEK, returnedObject.getDaysOfWeek())
//                );
//                return null;
//            }
//        };
        ClassesListFragment monfragment = ClassesListFragment.init(-1, new DelegateCall<Course>() {
            @Override
            public Course Invoke(Course returnedObject) {
                startActivity(
                        new Intent(ClassesActivity.this, AttendanceActivity.class)
                                .putExtra(AttendanceActivity.CLASS_ID         , returnedObject.id)
                                .putExtra(AttendanceActivity.CLASS_START_TIME , returnedObject.getStart_time())
                                .putExtra(AttendanceActivity.CLASS_END_TIME   , returnedObject.getEnd_time())
                                .putExtra(AttendanceActivity.CLASS_CODE       , returnedObject.code)
                                .putExtra(AttendanceActivity.CLASS_DAY_OF_WEEK_SELECTED       , Calendar.MONDAY)
                                .putIntegerArrayListExtra(AttendanceActivity.CLASS_DAY_OF_WEEK, returnedObject.getDaysOfWeek())
                );
                return null;
            }
        }, Calendar.MONDAY    , monCourses);
        ClassesListFragment tuefragment = ClassesListFragment.init(-1, new DelegateCall<Course>() {
            @Override
            public Course Invoke(Course returnedObject) {
                startActivity(
                        new Intent(ClassesActivity.this, AttendanceActivity.class)
                                .putExtra(AttendanceActivity.CLASS_ID         , returnedObject.id)
                                .putExtra(AttendanceActivity.CLASS_START_TIME , returnedObject.getStart_time())
                                .putExtra(AttendanceActivity.CLASS_END_TIME   , returnedObject.getEnd_time())
                                .putExtra(AttendanceActivity.CLASS_CODE       , returnedObject.code)
                                .putExtra(AttendanceActivity.CLASS_DAY_OF_WEEK_SELECTED       , Calendar.TUESDAY)
                                .putIntegerArrayListExtra(AttendanceActivity.CLASS_DAY_OF_WEEK, returnedObject.getDaysOfWeek())
                );
                return null;
            }
        }, Calendar.TUESDAY   , tueCourses);
        ClassesListFragment wedfragment = ClassesListFragment.init(-1, new DelegateCall<Course>() {
            @Override
            public Course Invoke(Course returnedObject) {
                startActivity(
                        new Intent(ClassesActivity.this, AttendanceActivity.class)
                                .putExtra(AttendanceActivity.CLASS_ID         , returnedObject.id)
                                .putExtra(AttendanceActivity.CLASS_START_TIME , returnedObject.getStart_time())
                                .putExtra(AttendanceActivity.CLASS_END_TIME   , returnedObject.getEnd_time())
                                .putExtra(AttendanceActivity.CLASS_CODE       , returnedObject.code)
                                .putExtra(AttendanceActivity.CLASS_DAY_OF_WEEK_SELECTED       , Calendar.WEDNESDAY)
                                .putIntegerArrayListExtra(AttendanceActivity.CLASS_DAY_OF_WEEK, returnedObject.getDaysOfWeek())
                );
                return null;
            }
        }, Calendar.WEDNESDAY , wedCourses);
        ClassesListFragment thufragment = ClassesListFragment.init(-1, new DelegateCall<Course>() {
            @Override
            public Course Invoke(Course returnedObject) {
                startActivity(
                        new Intent(ClassesActivity.this, AttendanceActivity.class)
                                .putExtra(AttendanceActivity.CLASS_ID         , returnedObject.id)
                                .putExtra(AttendanceActivity.CLASS_START_TIME , returnedObject.getStart_time())
                                .putExtra(AttendanceActivity.CLASS_END_TIME   , returnedObject.getEnd_time())
                                .putExtra(AttendanceActivity.CLASS_CODE       , returnedObject.code)
                                .putExtra(AttendanceActivity.CLASS_DAY_OF_WEEK_SELECTED       , Calendar.THURSDAY)
                                .putIntegerArrayListExtra(AttendanceActivity.CLASS_DAY_OF_WEEK, returnedObject.getDaysOfWeek())
                );
                return null;
            }
        }, Calendar.THURSDAY  , thuCourses);
        ClassesListFragment frifragment = ClassesListFragment.init(-1, new DelegateCall<Course>() {
            @Override
            public Course Invoke(Course returnedObject) {
                startActivity(
                        new Intent(ClassesActivity.this, AttendanceActivity.class)
                                .putExtra(AttendanceActivity.CLASS_ID         , returnedObject.id)
                                .putExtra(AttendanceActivity.CLASS_START_TIME , returnedObject.getStart_time())
                                .putExtra(AttendanceActivity.CLASS_END_TIME   , returnedObject.getEnd_time())
                                .putExtra(AttendanceActivity.CLASS_CODE       , returnedObject.code)
                                .putExtra(AttendanceActivity.CLASS_DAY_OF_WEEK_SELECTED       , Calendar.FRIDAY)
                                .putIntegerArrayListExtra(AttendanceActivity.CLASS_DAY_OF_WEEK, returnedObject.getDaysOfWeek())
                );
                return null;
            }
        }, Calendar.FRIDAY    , friCourses);
        ClassesListFragment satfragment = ClassesListFragment.init(-1, new DelegateCall<Course>() {
            @Override
            public Course Invoke(Course returnedObject) {
                startActivity(
                        new Intent(ClassesActivity.this, AttendanceActivity.class)
                                .putExtra(AttendanceActivity.CLASS_ID         , returnedObject.id)
                                .putExtra(AttendanceActivity.CLASS_START_TIME , returnedObject.getStart_time())
                                .putExtra(AttendanceActivity.CLASS_END_TIME   , returnedObject.getEnd_time())
                                .putExtra(AttendanceActivity.CLASS_CODE       , returnedObject.code)
                                .putExtra(AttendanceActivity.CLASS_DAY_OF_WEEK_SELECTED       , Calendar.SATURDAY)
                                .putIntegerArrayListExtra(AttendanceActivity.CLASS_DAY_OF_WEEK, returnedObject.getDaysOfWeek())
                );
                return null;
            }
        }, Calendar.SATURDAY  , satCourses);
        List<Fragment> courses = new ArrayList<>();
        courses.add(monfragment);
        courses.add(tuefragment);
        courses.add(wedfragment);
        courses.add(thufragment);
        courses.add(frifragment);
        courses.add(satfragment);
        List<String> titles = new ArrayList<>();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        titles.add(dayFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        titles.add(dayFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        titles.add(dayFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        titles.add(dayFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        titles.add(dayFormat.format(new Date(calendar.getTimeInMillis())));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        titles.add(dayFormat.format(new Date(calendar.getTimeInMillis())));
        GenericFragmentAdapter adapter = new GenericFragmentAdapter(getSupportFragmentManager(), courses, titles);
        pager.setAdapter(adapter);

        // SET PAGER ON TODAY
        pager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY);
    }

    private void ShowProgress(boolean show) {
        findViewById(R.id.activity_progress).setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
