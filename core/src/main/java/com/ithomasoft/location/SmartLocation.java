package com.ithomasoft.location;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ithomasoft.location.geocoding.GeocodingControl;
import com.ithomasoft.location.geocoding.provider.IGeocodingProvider;
import com.ithomasoft.location.geocoding.provider.SystemGeocodingProvider;
import com.ithomasoft.location.location.LocationControl;
import com.ithomasoft.location.location.provider.ILocationProvider;
import com.ithomasoft.location.location.provider.SystemLocationProvider;

/**
 * @author ithomasoft
 */
public class SmartLocation {
    private Context context;
    private boolean logger;

    /**
     * Creates the SmartLocation basic instance.
     *
     * @param context execution context
     * @param logger  是否输出日志
     */
    private SmartLocation(Context context, boolean logger) {
        this.context = context;
        this.logger = logger;
    }

    public static SmartLocation with(Context context) {
        return new Builder(context).build();
    }


    public Context getContext() {
        return context;
    }

    public boolean isLogger() {
        return logger;
    }

    /**
     * @return request handler for location operations
     */
    public LocationControl location() {
        return location(new SystemLocationProvider());
    }

    /**
     * @param provider location provider we want to use
     * @return request handler for location operations
     */
    public LocationControl location(ILocationProvider provider) {
        return new LocationControl(this, provider);
    }


    /**
     * @return request handler for geocoding operations
     */
    public GeocodingControl geocoding() {
        return geocoding(new SystemGeocodingProvider());
    }

    /**
     * @param geocodingProvider geocoding provider we want to use
     * @return request handler for geocoding operations
     */
    public GeocodingControl geocoding(IGeocodingProvider geocodingProvider) {
        return new GeocodingControl(this, geocodingProvider);
    }


    public static class Builder {
        private final Context context;
        private boolean loggingEnabled;

        public Builder(@NonNull Context context) {
            this.context = context;
            this.loggingEnabled = false;
        }

        public Builder logging(boolean enabled) {
            this.loggingEnabled = enabled;
            return this;
        }


        public SmartLocation build() {
            return new SmartLocation(context, loggingEnabled);
        }

    }
}
