package com.postit.mymomsweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.postit.mymomsweather.databinding.ActivityMainBinding;

import java.nio.file.Files;


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

        Button btn_calllog = (Button) findViewById(R.id.btn_calllog);
        btn_calllog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calllogIntent = new Intent(getApplicationContext(), CallLogActivity.class);
                startActivity(calllogIntent);
            }
        });


    }


}