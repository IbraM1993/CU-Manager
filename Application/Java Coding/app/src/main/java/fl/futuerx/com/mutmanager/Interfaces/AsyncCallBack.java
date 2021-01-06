package fl.futuerx.com.mutmanager.Interfaces;


public interface AsyncCallBack<T, K> {

    void OnStart();
    void OnCompleted(T result);
    void OnFailed(K error);
}
