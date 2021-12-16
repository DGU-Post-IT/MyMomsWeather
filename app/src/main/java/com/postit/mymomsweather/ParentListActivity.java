package com.postit.mymomsweather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.mymomsweather.Model.ParentUser;
import com.postit.mymomsweather.databinding.ActivityParentListBinding;

public class ParentListActivity extends AppCompatActivity {

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ActivityParentListBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getParentList();


    }

    void getParentList(){
        db.collection("users")
                .whereArrayContains("follower",auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            ParentUser parent = doc.toObject(ParentUser.class);
                            addTextView(doc.getId(),parent.getName());

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
