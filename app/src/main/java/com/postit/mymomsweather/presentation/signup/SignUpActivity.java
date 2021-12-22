package com.postit.mymomsweather.presentation.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.postit.mymomsweather.presentation.myinfo.MyInfoActivity;
import com.postit.mymomsweather.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    FirebaseAuth auth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bindRegisterButton();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null){
            auth.signOut();
        }
    }

    private void bindRegisterButton() {
        binding.loginButton.setOnClickListener((v)->{
            String id = binding.idEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();

            if(!id.matches("^(.+)@(.+)$")){
                Toast.makeText(this,"올바른 이메일 형식을 입력하세요.",Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.length()<8){
                Toast.makeText(this,"비밀번호를 8자리 이상으로 해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(id,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"계정을 성공적으로 생성하였습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MyInfoActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"계정 생성에 실패하였습니다..",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });


    }
}