package com.postit.mymomsweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;

import com.postit.mymomsweather.Model.EmotionRecord;
import com.postit.mymomsweather.Model.ParentUser;
import com.postit.mymomsweather.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private MainViewModel model;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setLifecycleOwner(this);
        model = new ViewModelProvider(this).get(MainViewModel.class);

        binding.loginButton.setOnClickListener((v)->{
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        });

        initViewPager();
        initParentRecyclerView();

        binding.refreshView.setOnRefreshListener(()->{
            model.fetchParentList();
            binding.refreshView.setRefreshing(false);
        });

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