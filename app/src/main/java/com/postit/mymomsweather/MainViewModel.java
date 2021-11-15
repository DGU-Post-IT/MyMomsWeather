package com.postit.mymomsweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.mymomsweather.Model.AudioRecord;
import com.postit.mymomsweather.Model.EmotionRecord;
import com.postit.mymomsweather.Model.ParentUser;

public class MainViewModel extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = null;
    MutableLiveData<EmotionRecord> latestEmotion = new MutableLiveData<EmotionRecord>();
    ListLiveData<ParentUser> parentUserListLiveData = new ListLiveData<>();

    public MainViewModel(){
        currentUser = auth.getCurrentUser();
        fetchParentList();
    }

    MutableLiveData<EmotionRecord> getLatestEmotion(){
        if(latestEmotion == null){
            latestEmotion = new MutableLiveData<>();
        }
        return latestEmotion;
    }

    void fetchLatestEmotion(String parentID){
        db.collection("users").document(parentID)
                .collection("emotionRecord").orderBy("time", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            EmotionRecord er = doc.toObject(EmotionRecord.class);
                            latestEmotion.setValue(er);

                        }
                    }
                });
    }

    ListLiveData<ParentUser> getParentUserListLiveData(){
        if(parentUserListLiveData==null){
            latestEmotion = new MutableLiveData<>();
            parentUserListLiveData = new ListLiveData<>();
        }
        return parentUserListLiveData;
    }

    void fetchParentList(){
        if(auth.getCurrentUser()==null){
            latestEmotion.setValue(null);
            parentUserListLiveData.clear(true);
            return;
        }
        db.collection("users")
                .whereArrayContains("follower",auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        String firstId = null;
                        parentUserListLiveData.clear(false);
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            if(firstId==null){
                                firstId=doc.getId();
                            }
                            ParentUser parent = doc.toObject(ParentUser.class);
                            parentUserListLiveData.add(parent);
                        }
                        fetchLatestEmotion(firstId);

                    }
                });
    }

    void checkUser(){
        if(auth.getCurrentUser() ==null ||
                currentUser == null||
                auth.getCurrentUser().getUid()!=currentUser.getUid()){
            fetchParentList();
        }
    }


}
