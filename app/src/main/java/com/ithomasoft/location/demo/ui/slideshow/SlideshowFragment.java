package com.ithomasoft.location.demo.ui.slideshow;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ithomasoft.location.OnLocationUpdatedListener;
import com.ithomasoft.location.OnReverseGeocodingListener;
import com.ithomasoft.location.SmartLocation;
import com.ithomasoft.location.demo.databinding.FragmentSlideshowBinding;

import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        SmartLocation.with(getContext()).location().continuous().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                SmartLocation.with(getContext()).geocoding().reverse(location, new OnReverseGeocodingListener() {
                    @Override
                    public void onAddressResolved(Location original, List<Address> results) {
                        textView.setText(original.toString()+"\r\n"+results.get(0).toString());
                    }
                });

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        SmartLocation.with(getContext()).location().stop();
        super.onDestroyView();
        binding = null;
    }
}