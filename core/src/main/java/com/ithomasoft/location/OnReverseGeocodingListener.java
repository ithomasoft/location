package com.ithomasoft.location;

import android.location.Address;
import android.location.Location;

import java.util.List;

/**
 * 逆地理编码
 *
 * @author ithomasoft
 */
public interface OnReverseGeocodingListener {
    void onAddressResolved(Location original, List<Address> results);
}
