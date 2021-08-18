package com.ithomasoft.location.geocoding.provider;

import android.content.Context;
import android.location.Location;

import com.ithomasoft.location.OnGeocodingListener;
import com.ithomasoft.location.OnReverseGeocodingListener;

/**
 * @author ithomasoft
 */
public interface IGeocodingProvider {
    void init(Context context, boolean logger);

    void addName(String name, int maxResults);

    void addLocation(Location location, int maxResults);

    void start(OnGeocodingListener geocodingListener, OnReverseGeocodingListener reverseGeocodingListener);

    void stop();
}
