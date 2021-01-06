package fl.futuerx.com.mutmanager.Interfaces;

import android.location.Location;

public interface LocationCallBack<T, R> {

    void GetLocation(T param, DelegateCall<Location> locationHandler, boolean requestAccurate);
    R HandleLocation(Location location);
}
