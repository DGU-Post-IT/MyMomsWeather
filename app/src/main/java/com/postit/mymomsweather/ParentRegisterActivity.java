package com.postit.mymomsweather;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.postit.mymomsweather.databinding.ActivityParentRegisterBinding;

public class ParentRegisterActivity extends AppCompatActivity {

    ActivityParentRegisterBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bindParentRegisterButton();

    }

    private void bindParentRegisterButton() {
        binding.registerButton.setOnClickListener((v) -> {
            String parentEmail = binding.emailEditText.getText().toString();
            if (parentEmail == null || parentEmail.equals("")) {
                return;
            }
            searchUserIdByEmail(parentEmail);

        });

    }

    private void searchUserIdByEmail(String email) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Log.d("TAG", "No User Found");
                                Toast.makeText(getApplicationContext(), "사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (task.getResult().size() > 1) {
                                Log.d("TAG", "Too Many User Found");
                                return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String parentID = document.getId();
                                addRequestToParent(parentID);
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void addRequestToParent(String parentID) {
        db.collection("users").document(parentID)
                .update("requested", FieldValue.arrayUnion(auth.getCurrentUser().getUid()));

    }
}

