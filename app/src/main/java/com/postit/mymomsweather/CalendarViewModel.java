package com.postit.mymomsweather;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.mymomsweather.Model.EmotionRecord;
import com.postit.mymomsweather.Model.ParentUser;

import java.util.Date;
import java.util.HashMap;

public class CalendarViewModel extends AndroidViewModel {


    private Context context = getApplication().getApplicationContext();


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    MutableLiveData<HashMap<Long, Long>> _dayCall = new MutableLiveData<>();
    MutableLiveData<HashMap<Integer, Integer>> monthlyEmotionStat = new MutableLiveData<>();
    MutableLiveData<HashMap<Integer, Integer>> weeklyEmotionStat = new MutableLiveData<>();
    ListLiveData<EmotionRecord> emotionRecordList = new ListLiveData<>();
    MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    MutableLiveData<String> parentID = new MutableLiveData<>();
    MutableLiveData<EmotionRecord> yesterdayEmotion = new MutableLiveData<>(new EmotionRecord(4));
    public MutableLiveData<Integer> selectedView = new MutableLiveData<>();

    public CalendarViewModel(Application application) {
        super(application);
    }

    void fetchEmotionRecord() {
        if (parentID.getValue() == null) return;
        Date today = new Date(System.currentTimeMillis());
        HashMap<Integer,Integer> monthlyMap = new HashMap<>();
        HashMap<Integer,Integer> weeklyMap = new HashMap<>();

        db.collection("users").document(parentID.getValue())
                .collection("emotionRecord")
                .whereGreaterThanOrEqualTo("time", new Date(today.getYear(), today.getMonth(), 1))
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener((task) -> {
                    emotionRecordList.clear(false);
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            EmotionRecord er = doc.toObject(EmotionRecord.class);
                            int emotion =er.getEmotion();
                            emotionRecordList.add(er);
                            if(er.getTime().getTime()/ 1000 / 60 / 60 / 24 == KoreanTime.koreaToday()-1){
                                yesterdayEmotion.setValue(er);
                            }
                            monthlyMap.put(emotion,monthlyMap.getOrDefault(emotion,0)+1);
                            if(today.getTime()-er.getTime().getTime()<1000*60*60*24*7){
                                weeklyMap.put(emotion,weeklyMap.getOrDefault(emotion,0)+1);
                            }
                            Log.d("emtion",today.compareTo(er.getTime())+" diff");
                        }
                        monthlyEmotionStat.setValue(monthlyMap);
                        weeklyEmotionStat.setValue(weeklyMap);
                    }

                });
    }

    void fetchParentList() {
        if(auth.getCurrentUser()==null)return;
        db.collection("users")
                .whereArrayContains("follower", auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            ParentUser parent = doc.toObject(ParentUser.class);
                            this.phoneNumber.setValue(parent.getPhone());
                            parentID.setValue(doc.getId());
                            Log.d("parent", parent.toString());
                        }
                        fetchCallHistory();
                        fetchEmotionRecord();
                    }
                });
    }

    public void fetchCallHistory() {

        String[] callSet = new String[]{
                CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};
        Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet,
                null, null, null);
        if (c.getCount() == 0) {
            return;
        }
        c.moveToFirst();
        HashMap<Long, Long> hm = new HashMap<>();

        do {
            if (c.getString(2).equals(phoneNumber.getValue())) {
                long callData = c.getLong(0);
                Date call_date = new Date(callData);
                Log.d("call_date", call_date+"");
                Long callDay = callData / 1000 / 60 / 60 / 24;

                Long duration = Long.parseLong(c.getString(3));
                hm.put(callDay, hm.getOrDefault(callDay, 0L) + duration);
                Log.d("Longlonghm","Day : " + String.valueOf(callDay)+" "+String.valueOf(hm.get(callDay)));
            }

        } while (c.moveToNext());

        Log.d("calendar", "call log loaded hm size : " + hm.size());

        c.close();
        _dayCall.setValue(hm);

    }

}
