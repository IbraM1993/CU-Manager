package fl.futuerx.com.mutmanager.Asynctask;

import android.app.Activity;

import java.util.List;

import fl.futuerx.com.mutmanager.BackEnd.MUT_ATTENDANCE_API;
import fl.futuerx.com.mutmanager.BackEnd.apiRequestObject;
import fl.futuerx.com.mutmanager.Interfaces.AsyncCallBack;
import fl.futuerx.com.mutmanager.Models.Course;

public class GetCoursesTask extends RetrofitCallTask<List<Course>> {

    private final int instructor_id;

    public GetCoursesTask(Activity activity, int instructor_id, AsyncCallBack<List<Course>, String> callBack) {
        super(activity, callBack);
        this.instructor_id = instructor_id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        apiRequestObject request = new apiRequestObject();
        request.id = instructor_id;
        MakeCall( MUT_ATTENDANCE_API.getInstance().getApiInstance().getClasses(request));
        return null;
    }

}
