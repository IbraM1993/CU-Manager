package fl.futuerx.com.mutmanager.Asynctask;

import android.app.Activity;

import fl.futuerx.com.mutmanager.BackEnd.MUT_ATTENDANCE_API;
import fl.futuerx.com.mutmanager.BackEnd.loginRequest;
import fl.futuerx.com.mutmanager.Helpers.DataManager;
import fl.futuerx.com.mutmanager.Interfaces.AsyncCallBack;
import fl.futuerx.com.mutmanager.Models.Instructor;

public class LoginTask extends RetrofitCallTask<Instructor> {

    private final String username;
    private final String password;

    public LoginTask(Activity activity, String username, String password, AsyncCallBack<Instructor, String> asyncCallBack) {
        super(activity, asyncCallBack);
        this.username = username;
        this.password = DataManager.getInstance().getMD5EncryptedString(password);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        loginRequest request = new loginRequest(username, password);
        MakeCall( new MUT_ATTENDANCE_API().getApiInstance().loginWithCredential(request));
        return null;
    }
}