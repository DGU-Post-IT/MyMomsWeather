package com.postit.mymomsweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.CallLog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.os.BuildCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.mymomsweather.Model.CallRecord;
import com.postit.mymomsweather.Model.ParentUser;
import com.postit.mymomsweather.databinding.ActivityCalendarBinding;
import com.postit.mymomsweather.databinding.ActivityMainBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {
    final int CALL_LOG_READ_PERMISSION = 1000;
    ActivityCalendarBinding binding;

    ArrayList<CallRecord> arr = new ArrayList<>();
    String lookfor;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getParentList();


        binding.calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                for (CallRecord cr :
                        arr) {
                    if(
                            day.getDate().isEqual(cr.getDate())
                    ){
                        return true;
                    }

                }
                return false;
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(AppCompatResources.getDrawable(getApplicationContext(),R.drawable.ic_baseline_circle_24));
            }
        });

        binding.calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                widget.invalidateDecorators();
            }
        });

    }

    void getParentList(){
        db.collection("users")
                .whereArrayContains("follower",auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            ParentUser parent = doc.toObject(ParentUser.class);
                            this.lookfor = parent.getPhone();
                            Log.d("tag",lookfor);


                        }
                        getCallHistory();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CALL_LOG_READ_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getCallHistory();
                }
        }
    }

    public void getCallHistory() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, CALL_LOG_READ_PERMISSION);
            return;
        }
        String[] callSet = new String[]{
                CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet,
                null, null, null);
        if (c.getCount() == 0) {
            return;
        }

        c.moveToFirst();
        ArrayList<CallRecord> arr = new ArrayList<>();

        do {
            if (c.getString(2).equals(lookfor)) {
                CallRecord temp = new CallRecord();
                long callData = c.getLong(0);
                temp.setDate(LocalDate.ofEpochDay(callData/1000/60/60/24));

                if (c.getInt(1) == CallLog.Calls.INCOMING_TYPE){
                    temp.setType(false);
                }else{
                    temp.setType(true);
                }

                temp.setPhoneNumber(c.getString(2));
                temp.setDuration(Long.parseLong(c.getString(3)));

                arr.add(temp);
            }

        } while (c.moveToNext());

        c.close();
        this.arr = arr;
        binding.calendarView.invalidateDecorators();
    }
}
