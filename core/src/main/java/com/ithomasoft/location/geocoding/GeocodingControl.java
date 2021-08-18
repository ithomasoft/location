package com.ithomasoft.location.geocoding;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.ithomasoft.location.OnGeocodingListener;
import com.ithomasoft.location.OnReverseGeocodingListener;
import com.ithomasoft.location.SmartLocation;
import com.ithomasoft.location.geocoding.provider.IGeocodingProvider;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author ithomasoft
 */
public class GeocodingControl {
    private static final Map<Context, IGeocodingProvider> MAPPING = new WeakHashMap<>();

    private final SmartLocation smartLocation;
    private IGeocodingProvider provider;
    private boolean directAdded = false;
    private boolean reverseAdded = false;

    public GeocodingControl(@NonNull SmartLocation smartLocation, @NonNull IGeocodingProvider geocodingProvider) {
        this.smartLocation = smartLocation;
        if (!MAPPING.containsKey(smartLocation.getContext())) {
            MAPPING.put(smartLocation.getContext(), geocodingProvider);
        }
        provider = MAPPING.get(smartLocation.getContext());
        provider.init(smartLocation.getContext(), smartLocation.isLogger());
    }

    public GeocodingControl get() {
        return this;
    }

    /**
     * 逆地理编码
     *
     * @param location
     * @param reverseGeocodingListener
     */
    public void reverse(@NonNull Location location, @NonNull OnReverseGeocodingListener reverseGeocodingListener) {
        add(location);
        start(reverseGeocodingListener);
    }

    /**
     * 地理编码
     *
     * @param name
     * @param geocodingListener
     */
    public void direct(@NonNull String name, @NonNull OnGeocodingListener geocodingListener) {
        add(name);
        start(geocodingListener);
    }

    public GeocodingControl add(@NonNull Location location) {
        reverseAdded = true;
        provider.addLocation(location, 1);
        return this;
    }

    public GeocodingControl add(@NonNull Location location, int maxResults) {
        reverseAdded = true;
        provider.addLocation(location, maxResults);
        return this;
    }

    public GeocodingControl add(@NonNull String name) {
        directAdded = true;
        provider.addName(name, 1);
        return this;
    }

    public GeocodingControl add(@NonNull String name, int maxResults) {
        directAdded = true;
        provider.addName(name, maxResults);
        return this;
    }

    public void start(OnGeocodingListener geocodingListener) {
        start(geocodingListener, null);
    }

    public void start(OnReverseGeocodingListener reverseGeocodingListener) {
        start(null, reverseGeocodingListener);
    }

    /**
     * Starts the geocoder conversions, for either direct geocoding (name to location) and reverse geocoding (location to address).
     *
     * @param geocodingListener        will be called for name to location queries
     * @param reverseGeocodingListener will be called for location to name queries
     */
    public void start(OnGeocodingListener geocodingListener, OnReverseGeocodingListener reverseGeocodingListener) {
        if (provider == null) {
            throw new RuntimeException("A provider must be initialized");
        }
        if (directAdded && geocodingListener == null) {
            throw new RuntimeException("添加了一些地方进行地理编码，但未指定侦听器！");
        }
        if (reverseAdded && reverseGeocodingListener == null) {
            throw new RuntimeException("为反向地理编码添加了一些地方，但未指定侦听器！");
        }

        provider.start(geocodingListener, reverseGeocodingListener);
    }

    /**
     * Cleans up after the geocoder calls. Will be needed for avoiding possible leaks in registered receivers.
     */
    public void stop() {
        provider.stop();
    }
}
