package com.postit.mymomsweather.presentation.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.postit.mymomsweather.presentation.community.CommunityActivity;
import com.postit.mymomsweather.presentation.game.GameActivity;
import com.postit.mymomsweather.presentation.profile.ProfileActivity;
import com.postit.mymomsweather.R;
import com.postit.mymomsweather.model.EmotionRecord;
import com.postit.mymomsweather.model.ParentUser;
import com.postit.mymomsweather.databinding.ActivityMainBinding;
import com.postit.mymomsweather.presentation.calendar.CalendarActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    final int CALL_LOG_READ_PERMISSION = 100;

    private MainViewModel model;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        model = new ViewModelProvider(this).get(MainViewModel.class);
        getWindow().setWindowAnimations(0);

        binding.loginButton.setOnClickListener((v)->{
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        binding.layoutGocalendar.setOnClickListener((v)->{
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        });

        binding.layoutGocommunity.setOnClickListener((v)->{
            Intent intent = new Intent(this, CommunityActivity.class);
            startActivity(intent);
        });

        binding.layoutGogame.setOnClickListener((v)->{
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });

        checkPermission();
        initViewPager();
        initParentRecyclerView();


    }

    private void checkPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, CALL_LOG_READ_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CALL_LOG_READ_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    model.fetchParentList();
                }
        }
    }

    private void initViewPager() {
        InfoBoardAdapter adapter = new InfoBoardAdapter(this);
        binding.infoViewPager.setAdapter(adapter);
        model.getLatestEmotion().observe(this, new Observer<EmotionRecord>() {
            @Override
            public void onChanged(EmotionRecord emotionRecord) {
                ((InfoBoardAdapter)binding.infoViewPager.getAdapter()).setItem(emotionRecord);
            }
        });
    }

    private void initParentRecyclerView(){
        binding.refreshView.setOnRefreshListener(()->{
            model.fetchParentList();
            binding.refreshView.setRefreshing(false);
        });

        ParentListAdapter adapter = new ParentListAdapter(this);
        binding.parentListView.setAdapter(adapter);
        binding.parentListView.setLayoutManager
                (new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        model.getParentUserListLiveData().observe(this, new Observer<ArrayList<ParentUser>>() {
            @Override
            public void onChanged(ArrayList<ParentUser> parentUsers) {
                ((ParentListAdapter)binding.parentListView.getAdapter()).setItem(parentUsers);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.checkUser();
    }
}