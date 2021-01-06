package fl.futuerx.com.mutmanager.Asynctask;

import android.app.Activity;

import fl.futuerx.com.mutmanager.BackEnd.MUT_ATTENDANCE_API;
import fl.futuerx.com.mutmanager.BackEnd.apiRequestObject;
import fl.futuerx.com.mutmanager.BackEnd.apiResponseObject;
import fl.futuerx.com.mutmanager.Interfaces.AsyncCallBack;

public class UpdateAttendanceTask extends RetrofitCallTask<apiResponseObject> {

    private final int attendanceID;
    private final int status;
    public UpdateAttendanceTask(Activity activity, int attendanceID,  int status, AsyncCallBack<apiResponseObject, String> callBack) {
        super(activity, callBack);
        this.attendanceID = attendanceID;
        this.status = status;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        apiRequestObject request = new apiRequestObject();
        request.id      = attendanceID;
        request.type    = status;
        MakeCall( MUT_ATTENDANCE_API.getInstance().getApiInstance().updateAttendace(request));
        return null;
    }
}
