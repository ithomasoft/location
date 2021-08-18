package com.ithomasoft.location.location.provider;

import android.content.Context;
import android.location.Location;

import com.ithomasoft.location.OnLocationUpdatedListener;
import com.ithomasoft.location.model.LocationParams;


public interface ILocationProvider {

    void init(Context context, boolean logger);

    void start(OnLocationUpdatedListener listener, LocationParams params, boolean singleUpdate);

    void stop();

    Location getLastLocation();

    /**
     * 获取位置数据的标识
     *
     * @return
     */
    String getLocationTag();
}
