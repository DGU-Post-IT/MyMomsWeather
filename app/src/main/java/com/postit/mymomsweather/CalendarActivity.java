package com.postit.mymomsweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.postit.mymomsweather.Model.EmotionRecord;
import com.postit.mymomsweather.databinding.ActivityCalendarBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {
    final int CALL_LOG_READ_PERMISSION = 1000;
    int[] colorClassArray = new int[]{Color.RED, Color.BLUE, Color.GRAY, Color.MAGENTA};

    ActivityCalendarBinding binding;

    private CalendarViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, CALL_LOG_READ_PERMISSION);
        }

        model = new ViewModelProvider(this).get(CalendarViewModel.class);
        binding.setVm(model);

        bindPanelButton();
        initDailyPanel();
        initWeeklyPanel();
        initMonthlyPanel();
        initCalendarView();

        model.fetchParentList();

    }

    private void bindPanelButton() {
        binding.dailyPanelButton.setOnClickListener((v)->{
            model.selectedView.setValue(0);
            binding.invalidateAll();
        });
        binding.weeklyPanelButton.setOnClickListener((v)->{
            model.selectedView.setValue(1);
            binding.invalidateAll();
        });
        binding.monthlyPanelButton.setOnClickListener((v)->{
            model.selectedView.setValue(2);
            binding.invalidateAll();
        });
    }

    private void initMonthlyPanel() {
        binding.monthlyPieChart.setDrawEntryLabels(true);
        binding.monthlyPieChart.setUsePercentValues(false);
//        binding.monthlyPieChart.setScrollBarSize(12);
        binding.monthlyPieChart.setCenterTextSize(20);
        binding.monthlyPieChart.setEntryLabelTextSize(12);

        model.monthlyEmotionStat.observe(this, new Observer<HashMap<Integer, Integer>>() {
            @Override
            public void onChanged(HashMap<Integer, Integer> hm) {
                ArrayList<PieEntry> monthlyData = new ArrayList<>();
                monthlyData.add(new PieEntry(hm.getOrDefault(0, 0), "행복"));
                monthlyData.add(new PieEntry(hm.getOrDefault(1, 0), "무기력"));
                monthlyData.add(new PieEntry(hm.getOrDefault(2, 0), "화남"));
                monthlyData.add(new PieEntry(hm.getOrDefault(3, 0), "슬픔"));
                PieDataSet pieDataSet = new PieDataSet(monthlyData,"");
                pieDataSet.setColors(colorClassArray);
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(12);
                pieData.setValueTextColor(Color.WHITE);
                binding.monthlyPieChart.setData(pieData);
                binding.monthlyPieChart.invalidate();

            }
        });

    }
    private void initWeeklyPanel() {
        binding.weeklyPieChart.setDrawEntryLabels(true);
        binding.weeklyPieChart.setUsePercentValues(false);
        binding.weeklyPieChart.setCenterTextSize(20);
        binding.weeklyPieChart.setEntryLabelTextSize(12);
        model.weeklyEmotionStat.observe(this, new Observer<HashMap<Integer, Integer>>() {
            @Override
            public void onChanged(HashMap<Integer, Integer> hm) {
                ArrayList<PieEntry> weeklyData = new ArrayList<>();
                weeklyData.add(new PieEntry(hm.getOrDefault(0, 0), "행복"));
                weeklyData.add(new PieEntry(hm.getOrDefault(1, 0), "무기력"));
                weeklyData.add(new PieEntry(hm.getOrDefault(2, 0), "화남"));
                weeklyData.add(new PieEntry(hm.getOrDefault(3, 0), "슬픔"));
                PieDataSet pieDataSet = new PieDataSet(weeklyData,"");
                pieDataSet.setColors(colorClassArray);
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(12);
                pieData.setValueTextColor(Color.WHITE);
                binding.weeklyPieChart.setData(pieData);
                binding.weeklyPieChart.invalidate();

            }
        });

    }

    private void initDailyPanel() {
        model.emotionRecordList.observe(this, new Observer<ArrayList<EmotionRecord>>() {
            @Override
            public void onChanged(ArrayList<EmotionRecord> emotionRecords) {
                if (emotionRecords.size() != 0 &&
                        emotionRecords.get(0).getTime().getTime() / 1000 / 60 / 60 / 24 == KoreanTime.koreaToday()) {
                    switch (emotionRecords.get(0).getEmotion()) {
                        case 0:
                            binding.dailyEmotionView.setImageResource(R.drawable.ic_outline_wb_sunny_24);
                            binding.dailyEmotionTextView.setText("좋아요");
                            break;
                        case 1:
                            binding.dailyEmotionView.setImageResource(R.drawable.ic_outline_cloud_24);
                            binding.dailyEmotionTextView.setText("별로에요");
                            break;
                        case 2:
                            binding.dailyEmotionView.setImageResource(R.drawable.ic_outline_bolt_24);
                            binding.dailyEmotionTextView.setText("화나요");
                            break;
                        case 3:
                            binding.dailyEmotionView.setImageResource(R.drawable.ic_outline_mode_night_24);
                            binding.dailyEmotionTextView.setText("슬퍼요");
                            break;
                    }
                } else {
                    binding.dailyEmotionView.setImageResource(R.drawable.ic_baseline_block_24);
                    binding.dailyEmotionTextView.setText("오늘의 기록이 없어요ㅜㅜ");
                }

            }
        });
    }

    private void initCalendarView() {
        binding.calendarView.setTitleFormatter(new DateFormatTitleFormatter(DateTimeFormatter.ofPattern("yyyy년 MM월")));
        binding.calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Log.d("calendar", String.valueOf(day.getDate().toEpochDay()));
                if (model._dayCall.getValue() == null) return false;
                Long callDurationOnDay = model._dayCall.getValue().get(day.getDate().toEpochDay());
                return callDurationOnDay != null && callDurationOnDay >= 3600;
            }
            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(AppCompatResources
                        .getDrawable(getApplicationContext(), R.drawable.ic_baseline_circle_24_3600));
            }
        });
        binding.calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Log.d("calendar", String.valueOf(day.getDate().toEpochDay()));
                if (model._dayCall.getValue() == null) return false;
                Long callDurationOnDay = model._dayCall.getValue().get(day.getDate().toEpochDay());
                return callDurationOnDay != null && callDurationOnDay >= 1800 && callDurationOnDay < 3600;
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(AppCompatResources
                        .getDrawable(getApplicationContext(), R.drawable.ic_baseline_circle_24_1800));
            }
        });
        binding.calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Log.d("calendar", String.valueOf(day.getDate().toEpochDay()));
                if (model._dayCall.getValue() == null) return false;
                Long callDurationOnDay = model._dayCall.getValue().get(day.getDate().toEpochDay());
                return callDurationOnDay != null && callDurationOnDay >= 300 && callDurationOnDay < 1800;
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(AppCompatResources
                        .getDrawable(getApplicationContext(), R.drawable.ic_baseline_circle_24_300));
            }
        });
        binding.calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Log.d("calendar", String.valueOf(day.getDate().toEpochDay()));
                if (model._dayCall.getValue() == null) return false;
                Long callDurationOnDay = model._dayCall.getValue().get(day.getDate().toEpochDay());
                return callDurationOnDay != null && callDurationOnDay >= 0 && callDurationOnDay < 300;
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(AppCompatResources
                        .getDrawable(getApplicationContext(), R.drawable.ic_baseline_circle_24_0));
            }
        });

        binding.calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                widget.invalidateDecorators();
            }
        });

        model._dayCall.observe(this, new Observer<HashMap<Long, Long>>() {
            @Override
            public void onChanged(HashMap<Long, Long> longLongHashMap) {
                Log.d("calendar", "data change observed");
                binding.calendarView.invalidateDecorators();

                binding.dailyCallLineChart.setText(longLongHashMap.getOrDefault(KoreanTime.koreaToday(), 0L).toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_LOG_READ_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    model.fetchCallHistory();
                }
        }
    }

}
