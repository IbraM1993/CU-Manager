package fl.futuerx.com.mutmanager.Asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import fl.futuerx.com.mutmanager.Interfaces.AsyncCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallTask<T> extends AsyncTask<Void, Void, Void> {

    private final WeakReference<Activity> activity;
    private final AsyncCallBack<T, String> callBack;
    protected RetrofitCallTask(Activity activity, AsyncCallBack<T, String> callBack) {
        this.activity = new WeakReference<>(activity);
        this.callBack = callBack;
    }
    public T result;
    public String error;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(callBack != null){
            callBack.OnStart();
        }
    }


    protected void HandleResult() {
        Activity a = activity.get();
        if(a != null){
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(callBack != null){
                        if(error == null){
                            callBack.OnCompleted(result);
                        } else {
                            callBack.OnFailed(error);
                        }
                    }
                }
            });
        }

    }

    protected void MakeCall(Call<T> call) {
        try {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull Response<T> _response) {
                    if(_response.body() == null) {
                        try {
                            error =   _response.errorBody() == null ? "EMPTY ERROR " : _response.errorBody().string();
                        } catch(Exception e) {
                            e.printStackTrace();
                            error = "FAILED TO READ RESPONSE ERROR";
                        }
                    } else if (_response.code() != 200) {
                        try {
                            error =   _response.errorBody() == null ? "EMPTY ERROR STATUS : "+ _response.code() : _response.errorBody().string();
                        } catch(Exception e) {
                            e.printStackTrace();
                            error = "FAILED TO READ RESPONSE ERROR";
                        }
                    } else {

                        result = _response.body();
                    }
                    HandleResult();
                }
                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                    error = t.getMessage();
                    HandleResult();
                }
            });
        } catch(Exception ex) {
            ex.printStackTrace();
            error = ex.getMessage();
            HandleResult();
        }

    }
}
