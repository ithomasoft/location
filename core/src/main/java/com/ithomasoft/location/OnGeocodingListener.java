package com.ithomasoft.location;

import com.ithomasoft.location.model.LocationAddress;

import java.util.List;

/**
 * 地理编码
 * @author ithomasoft
 */
public interface OnGeocodingListener {
    void onLocationResolved(String name, List<LocationAddress> results);
}
