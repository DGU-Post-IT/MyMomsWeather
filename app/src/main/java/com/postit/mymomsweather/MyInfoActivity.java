package com.postit.mymomsweather;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.postit.mymomsweather.Model.ParentUser;
import com.postit.mymomsweather.databinding.ActivityUserInfoRegisterBinding;

import java.util.Date;

public class MyInfoActivity extends AppCompatActivity {
    String TAG = "MyInfoActivity";

    ActivityUserInfoRegisterBinding binding;

    FirebaseAuth auth;
    FirebaseFirestore db;

    String token;
    ParentUser parent = new ParentUser();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getFcmToken();

        initEditTExt();
        bindSexSelectButton();
        bindSaveButton();

    }

    private void initEditTExt() {
        binding.birthYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 2) {
                    binding.birthYear.setText(editable.toString().substring(0, 2));
                }
            }
        });
        binding.birthMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 2) {
                    binding.birthMonth.setText(editable.toString().substring(0, 2));
                }
            }
        });
        binding.birthDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 2) {
                    binding.birthDay.setText(editable.toString().substring(0, 2));
                }
            }
        });
    }

    private void bindSaveButton() {
        binding.saveButton.setOnClickListener((v) -> {
            int birthYear;
            int birthMonth;
            int birthDay;
            try {
                birthYear = Integer.parseInt(binding.birthYear.getText().toString());
                birthMonth = Integer.parseInt(binding.birthMonth.getText().toString());
                birthDay = Integer.parseInt(binding.birthDay.getText().toString());
                if(birthMonth<1||birthMonth>12||birthDay<1||birthDay>31) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "올바른 생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            Date birthdate = new Date(birthYear, birthMonth - 1, birthDay);
            parent.setBirthdate(birthdate);
            if (parent.getSex() == 0) {
                Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (auth.getCurrentUser() == null) {
                Toast.makeText(this, "계정 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            parent.setEmail(auth.getCurrentUser().getEmail());
            String name = binding.nameEditText.getText().toString();
            if (name.equals("")) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            parent.setName(name);

            if (token == null) {
                Toast.makeText(this, "알 수 없는 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                getFcmToken();
            }
            parent.setFcm(token);

            registerUserInfo(parent, auth.getCurrentUser().getUid());


        });
    }


    private void bindSexSelectButton() {
        binding.maleButton.setOnClickListener((v) -> {
            binding.maleButton.setSelected(true);
            binding.femaleButton.setSelected(false);
            parent.setSex(1);
        });
        binding.femaleButton.setOnClickListener((v) -> {
            binding.maleButton.setSelected(false);
            binding.femaleButton.setSelected(true);
            parent.setSex(2);
        });
    }

    void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        Log.d(TAG, token);
//                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void registerUserInfo(ParentUser user, String key) {
        db.collection("users").document(key)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("upload!!", "Error writing document", e);
                    }
                });
    }

}

