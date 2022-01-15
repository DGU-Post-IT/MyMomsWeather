package com.postit.mymomsweather.presentation.game;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.postit.mymomsweather.R;
import com.postit.mymomsweather.databinding.ActivityGameBinding;
import com.postit.mymomsweather.presentation.calendar.CalendarActivity;
import com.postit.mymomsweather.presentation.community.CommunityActivity;
import com.postit.mymomsweather.presentation.main.MainActivity;

public class GameActivity extends AppCompatActivity {

    ActivityGameBinding binding;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        setContentView(binding.getRoot());
        getWindow().setWindowAnimations(0);



        binding.layoutGohome.setOnClickListener((v)->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.layoutGocalendar.setOnClickListener((v)->{
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        });


    }
}
