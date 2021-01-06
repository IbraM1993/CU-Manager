package fl.futuerx.com.mutmanager.Asynctask;

import android.app.Activity;

import java.util.List;

import fl.futuerx.com.mutmanager.BackEnd.MUT_ATTENDANCE_API;
import fl.futuerx.com.mutmanager.BackEnd.apiRequestObject;
import fl.futuerx.com.mutmanager.Interfaces.AsyncCallBack;
import fl.futuerx.com.mutmanager.Models.Attendance;

public class GetAttendanceTask extends RetrofitCallTask<List<Attendance>> {

    private final int classId;
    private final String date;
    public GetAttendanceTask(Activity activity, int classId, String date, AsyncCallBack<List<Attendance>, String> callBack) {
        super(activity, callBack);
        this.classId = classId;
        this.date = date;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        apiRequestObject request = new apiRequestObject();
        request.id      = classId;
        request.code    = date;
        MakeCall( MUT_ATTENDANCE_API.getInstance().getApiInstance().getClassStudents(request));
        return null;
    }
}
