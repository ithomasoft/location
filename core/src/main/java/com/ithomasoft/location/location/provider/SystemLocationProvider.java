package com.ithomasoft.location.location.provider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.ithomasoft.location.OnLocationUpdatedListener;
import com.ithomasoft.location.model.LocationAccuracy;
import com.ithomasoft.location.model.LocationParams;
import com.ithomasoft.location.tools.LocationStore;
import com.ithomasoft.location.tools.SmartLogger;

/**
 * 系统位置信息提供者
 *
 * @author ithomasoft
 */
public class SystemLocationProvider implements ILocationProvider, LocationListener {
    private OnLocationUpdatedListener listener;
    private Context context;
    private LocationStore locationStore;

    private LocationManager locationManager;

    private SmartLogger logger;

    @Override
    public void init(Context context, boolean isLogger) {
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
        locationStore = new LocationStore(context);
        logger = new SmartLogger(isLogger);
    }

    @Override
    public void start(OnLocationUpdatedListener listener, LocationParams params, boolean singleUpdate) {
        this.listener = listener;
        Criteria criteria = getProvider(params);
        if (singleUpdate) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                logger.i("权限检查失败。请在设置位置之前在您的应用程序中处理它");
                return;
            }
            locationManager.requestSingleUpdate(criteria, this, Looper.getMainLooper());
        } else {
            locationManager.requestLocationUpdates(
                    params.getInterval(), params.getDistance(), criteria, this, Looper.getMainLooper());
        }
    }


    @Override
    public void stop() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public Location getLastLocation() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                return location;
            }
        }

        Location location = locationStore.get(getLocationTag());
        if (location != null) {
            return location;
        }
        return null;
    }

    @Override
    public String getLocationTag() {
        return "SYSTEM";
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        logger.d("onLocationChanged", location);
        if (listener != null) {
            listener.onLocationUpdated(location);
        }
        if (locationStore != null) {
            logger.d("存储在 SharedPreferences");
            locationStore.put(getLocationTag(), location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


    private Criteria getProvider(LocationParams params) {
        final LocationAccuracy accuracy = params.getAccuracy();
        final Criteria criteria = new Criteria();
        switch (accuracy) {
            case HIGH:
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setPowerRequirement(Criteria.POWER_HIGH);
                break;
            case MEDIUM:
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                criteria.setHorizontalAccuracy(Criteria.ACCURACY_MEDIUM);
                criteria.setVerticalAccuracy(Criteria.ACCURACY_MEDIUM);
                criteria.setBearingAccuracy(Criteria.ACCURACY_MEDIUM);
                criteria.setSpeedAccuracy(Criteria.ACCURACY_MEDIUM);
                criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
                break;
            case LOW:
            case LOWEST:
            default:
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                criteria.setHorizontalAccuracy(Criteria.ACCURACY_LOW);
                criteria.setVerticalAccuracy(Criteria.ACCURACY_LOW);
                criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
                criteria.setSpeedAccuracy(Criteria.ACCURACY_LOW);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
        }
        return criteria;
    }

}
