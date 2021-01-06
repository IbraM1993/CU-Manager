package fl.futuerx.com.mutmanager.Interfaces;

public interface GenericCallBack {
    public void onCallSuccess(Object response);
    public void onCallFailed(Object response);
    public void onEnd(Object response);
}
