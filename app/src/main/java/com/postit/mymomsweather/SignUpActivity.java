package com.postit.mymomsweather;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.postit.mymomsweather.databinding.ActivityProfileBinding;
import com.postit.mymomsweather.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

     ActivitySignUpBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}
