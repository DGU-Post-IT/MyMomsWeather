package com.postit.mymomsweather;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.postit.mymomsweather.databinding.ActivityCommunityBinding;
import com.postit.mymomsweather.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    ActivityGameBinding binding;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_game);
        setContentView(binding.getRoot());
        getWindow().setWindowAnimations(0);



        binding.layoutGohome.setOnClickListener((v)->{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });

        binding.layoutGocalendar.setOnClickListener((v)->{
            Intent intent = new Intent(this,CalendarActivity.class);
            startActivity(intent);
        });

        binding.layoutGocommunity.setOnClickListener((v)->{
            Intent intent = new Intent(this,CommunityActivity.class);
            startActivity(intent);
        });




    }
}
