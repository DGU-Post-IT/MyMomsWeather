package com.postit.mymomsweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.postit.mymomsweather.databinding.ActivityCalendarBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {
    final int CALL_LOG_READ_PERMISSION = 1000;
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

        initCalendarView();

        model.fetchParentList();

    }

    private void initCalendarView() {
        binding.calendarView.setTitleFormatter(new DateFormatTitleFormatter(DateTimeFormatter.ofPattern("yyyy년 MM월")));

        binding.calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Log.d("calendar", String.valueOf(day.getDate().toEpochDay()));
                if(model._dayCall.getValue() ==null) return false;
                Long callDurationOnDay = model._dayCall.getValue().get(day.getDate().toEpochDay());
                if (callDurationOnDay!=null&&callDurationOnDay>=3600) {
                    return true;
                }
                return false;
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
                if(model._dayCall.getValue() ==null) return false;
                Long callDurationOnDay = model._dayCall.getValue().get(day.getDate().toEpochDay());
                if (callDurationOnDay!=null&&callDurationOnDay>=1800&&callDurationOnDay<3600) {
                    return true;
                }
                return false;
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
                if(model._dayCall.getValue() ==null) return false;
                Long callDurationOnDay = model._dayCall.getValue().get(day.getDate().toEpochDay());
                if (callDurationOnDay!=null&&callDurationOnDay>=300&&callDurationOnDay<1800) {
                    return true;
                }
                return false;
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
                if(model._dayCall.getValue() ==null) return false;
                Long callDurationOnDay = model._dayCall.getValue().get(day.getDate().toEpochDay());
                if (callDurationOnDay!=null&&callDurationOnDay>=0&&callDurationOnDay<300) {
                    return true;
                }
                return false;
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
