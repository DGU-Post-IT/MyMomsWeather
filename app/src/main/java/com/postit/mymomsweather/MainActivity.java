package com.postit.mymomsweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.postit.mymomsweather.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.myPageButton.setOnClickListener((v) -> {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);
        });

        binding.showParentButton.setOnClickListener((v)->{
            Intent profileIntent = new Intent(this, ParentListActivity.class);
            startActivity(profileIntent);
        });


    }


}