package com.github.mosberger.helloannotationprocessor.view;

import android.support.v7.app.AppCompatActivity;

import com.github.mosberger.annotationplayground.annotations.AnnotatedActivity;
import com.github.mosberger.annotationplayground.annotations.DataBinding;
import com.github.mosberger.annotationplayground.annotations.ViewModel;
import com.github.mosberger.helloannotationprocessor.R;
import com.github.mosberger.helloannotationprocessor.databinding.ActivityMainBinding;
import com.github.mosberger.helloannotationprocessor.viewmodel.MainViewModel;

@AnnotatedActivity(layout = R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewModel(setter = "setMainViewModel")
    MainViewModel mainViewModel;

    @DataBinding
    ActivityMainBinding binding;
}
