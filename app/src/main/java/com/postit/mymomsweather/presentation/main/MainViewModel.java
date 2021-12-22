package com.postit.mymomsweather.presentation.main;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.mymomsweather.util.KoreanTime;
import com.postit.mymomsweather.ListLiveData;
import com.postit.mymomsweather.model.CallRecord;
import com.postit.mymomsweather.model.EmotionRecord;
import com.postit.mymomsweather.model.ParentUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainViewModel extends AndroidViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    private Context context = getApplication().getApplicationContext();

    FirebaseUser currentUser = null;
    MutableLiveData<EmotionRecord> latestEmotion = new MutableLiveData<EmotionRecord>();
    ListLiveData<ParentUser> parentUserListLiveData = new ListLiveData<>();
    MutableLiveData<HashMap<Long,Long>> _dayCall =new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
        currentUser = auth.getCurrentUser();
        fetchParentList();
    }

    MutableLiveData<EmotionRecord> getLatestEmotion() {
        if (latestEmotion == null) {
            latestEmotion = new MutableLiveData<>();
        }
        return latestEmotion;
    }

    void fetchYesterdayEmotion(String parentID) {
        db.collection("users").document(parentID)
                .collection("emotionRecord").orderBy("time", Query.Direction.DESCENDING)
                .limit(2)
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            EmotionRecord er = doc.toObject(EmotionRecord.class);
                            if(KoreanTime.toKoreaDay(er.getTime().getTime())==KoreanTime.koreaToday()-1){
                                latestEmotion.setValue(er);
                                break;
                            }else{
                                latestEmotion.setValue(er.setEmotion(4));
                            }
                        }
                    }
                });
    }

    ListLiveData<ParentUser> getParentUserListLiveData() {
        if (parentUserListLiveData == null) {
            latestEmotion = new MutableLiveData<>();
            parentUserListLiveData = new ListLiveData<>();
        }
        return parentUserListLiveData;
    }

    void fetchParentList() {
        if (auth.getCurrentUser() == null) {
            latestEmotion.setValue(null);
            parentUserListLiveData.clear(true);
            return;
        }
        db.collection("users")
                .whereArrayContains("follower", auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        String firstId = null;
                        parentUserListLiveData.clear(false);
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            if (firstId == null) {
                                firstId = doc.getId();
                            }
                            ParentUser parent = doc.toObject(ParentUser.class);
                            parent.setCallDuration("0");
                            parentUserListLiveData.add(parent);
                        }
                        if(firstId==null) return;
                        fetchYesterdayEmotion(firstId);
                        calcCallLogDuration();

                    }
                });
    }

    void calcCallLogDuration() {

        String[] callSet = new String[]{
                CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};

        ArrayList<ParentUser> parentUsers = getParentUserListLiveData().getValue();

        Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet,
                null, null, "date DESC");
        if (c.getCount() == 0) {
            return;
        }

        c.moveToFirst();
        do {
            CallRecord temp = new CallRecord();
            String phoneNumber = c.getString(2);
            String duration = c.getString(3);
            long callDate = c.getLong(0);
//            if(KoreanTime.koreaToday() - KoreanTime.toKoreaDay(callDate) > 7){
//                break;
//            }

            String callDate_month = new SimpleDateFormat("yyyy년 MM월", Locale.KOREAN).format(callDate+32400000);
            long now = System.currentTimeMillis();
            String now_month = new SimpleDateFormat("yyyy년 MM월", Locale.KOREAN).format(now+32400000);
            if(!callDate_month.equals(now_month)){
                break;
            }

            Log.d("Monthly_time",callDate+"");
            for (int i = 0; i < parentUsers.size(); i++) {
                if(parentUsers.get(i).getPhone().equals(phoneNumber)){
                    String newDuration = String.valueOf(Long.parseLong(parentUsers.get(i).getCallDuration())+Long.parseLong(duration));
                    parentUsers.get(i).setCallDuration(newDuration);
                }
            }
        } while (c.moveToNext());

        c.close();
    }

    void checkUser() {
        if (auth.getCurrentUser() == null ||
                currentUser == null ||
                auth.getCurrentUser().getUid() != currentUser.getUid()) {
            fetchParentList();
        }
    }


}
