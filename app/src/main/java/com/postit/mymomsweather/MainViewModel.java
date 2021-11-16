package com.postit.mymomsweather;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.mymomsweather.Model.AudioRecord;
import com.postit.mymomsweather.Model.CallRecord;
import com.postit.mymomsweather.Model.EmotionRecord;
import com.postit.mymomsweather.Model.ParentUser;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    private Context context = getApplication().getApplicationContext();

    FirebaseUser currentUser = null;
    MutableLiveData<EmotionRecord> latestEmotion = new MutableLiveData<EmotionRecord>();
    ListLiveData<ParentUser> parentUserListLiveData = new ListLiveData<>();

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

    void fetchLatestEmotion(String parentID) {
        db.collection("users").document(parentID)
                .collection("emotionRecord").orderBy("time", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            EmotionRecord er = doc.toObject(EmotionRecord.class);
                            latestEmotion.setValue(er);

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
                .whereArrayContains("follower", auth.getCurrentUser().getUid())
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
                        fetchLatestEmotion(firstId);
                        calcCallLogDuration();

                    }
                });
    }

    void calcCallLogDuration() {

        String[] callSet = new String[]{
                CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};

        ArrayList<ParentUser> parentUsers = getParentUserListLiveData().getValue();

        Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet,
                null, null, null);
        if (c.getCount() == 0) {
            return;
        }

        c.moveToFirst();
        do {
            CallRecord temp = new CallRecord();
            String phoneNumber = c.getString(2);
            String duration = c.getString(3);
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
