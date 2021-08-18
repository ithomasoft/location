package com.ithomasoft.location.location;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ithomasoft.location.OnLocationUpdatedListener;
import com.ithomasoft.location.SmartLocation;
import com.ithomasoft.location.location.provider.ILocationProvider;
import com.ithomasoft.location.model.LocationParams;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Administrator
 */
public class LocationControl {
    private static final Map<String, ILocationProvider> MAPPING = new WeakHashMap<>();

    private LocationParams params;
    private ILocationProvider provider;
    private boolean oneFix;

    public LocationControl(@NonNull SmartLocation smartLocation, @NonNull ILocationProvider locationProvider) {
        params = LocationParams.BEST_EFFORT;
        oneFix = false;

        if (!MAPPING.containsKey(locationProvider.getLocationTag())) {
            MAPPING.put(locationProvider.getLocationTag(), locationProvider);
        }
        provider = MAPPING.get(locationProvider.getLocationTag());

        provider.init(smartLocation.getContext(), smartLocation.isLogger());

    }

    public LocationControl config(@NonNull LocationParams params) {
        this.params = params;
        return this;
    }

    public LocationControl once() {
        this.oneFix = true;
        return this;
    }

    public LocationControl continuous() {
        this.oneFix = false;
        return this;
    }

    @Nullable
    public Location getLastLocation() {
        return provider.getLastLocation();
    }

    public LocationControl get() {
        return this;
    }

    public void start(OnLocationUpdatedListener listener) {
        if (provider == null) {
            throw new RuntimeException("A provider must be initialized");
        }
        provider.start(listener, params, oneFix);
    }

    public void stop() {
        provider.stop();
    }
}
