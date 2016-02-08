package com.github.mosberger.helloannotationprocessor.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mosberger.annotationplayground.annotations.AnnotatedActivity;
import com.github.mosberger.annotationplayground.annotations.DataBinding;
import com.github.mosberger.annotationplayground.annotations.ViewModel;
import com.github.mosberger.helloannotationprocessor.R;
import com.github.mosberger.helloannotationprocessor.databinding.ActivityMainBinding;
import com.github.mosberger.helloannotationprocessor.viewmodel.MainViewModel;

@AnnotatedActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewModel("setMainViewModel")
    MainViewModel mainViewModel;

    @DataBinding
    ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityUtil.bind(this);
        setSupportActionBar(binding.toolbar);
    }
}
