package com.postit.mymomsweather;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.mymomsweather.Model.AudioRecord;
import com.postit.mymomsweather.Model.ParentUser;
import com.postit.mymomsweather.databinding.ActivityParentStatusBinding;

public class ParentStatusActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ActivityParentStatusBinding binding;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //인텐트에서 부모 아이디 가져오기
        String parentID = getIntent().getExtras().getString("parentID");

        //부모 아이디 문서 하위의 음성기록 콜렉션 가져오기
        db.collection("users").document(parentID)
                .collection("audioFile").orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            AudioRecord ar = doc.toObject(AudioRecord.class);
                            addTextView(ar.getSpeakerID(),ar.getAudioLocation());


                        }
                    }
                });
    }
    void addTextView(String id, String name){
        TextView tv = new TextView(this);
        tv.setText(id+"\n"+name);
        tv.setOnClickListener((v)->{
            Intent intent = new Intent(this,ParentStatusActivity.class);
            intent.putExtra("parentID",id);
            startActivity(intent);
        });


    }


}
