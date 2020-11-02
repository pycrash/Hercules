package com.example.hercules.utils.LoginSliderUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hercules.R;

public class SlideFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    @StringRes
    private static final int[] PAGE_TITLES =
            new int[]{R.string.slider1_title, R.string.slider2_title, R.string.slider3_title};
    private static final int[] TEXT_TITLES =
            new int[]{R.string.slider1_info, R.string.slider2_info, R.string.slider3_info};
    @StringRes
    private static final int[] PAGE_IMAGE =
            new int[]{
                    R.raw.protein, R.raw.variety, R.raw.authentic
            };
    private SliderViewModel sliderViewModel;

    public static SlideFragment newInstance(int index) {
        SlideFragment fragment = new SlideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sliderViewModel = ViewModelProviders.of(this).get(SliderViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        sliderViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_slide_fragment, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        final LottieAnimationView imageView = root.findViewById(R.id.imageView);
        final TextView textLabel = root.findViewById(R.id.text_label);
        sliderViewModel.getText().observe(getViewLifecycleOwner(), index -> {
            textView.setText(PAGE_TITLES[index]);
            imageView.setAnimation(PAGE_IMAGE[index]);
            textLabel.setText(TEXT_TITLES[index]);
        });
        return root;
    }
}