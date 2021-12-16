package com.postit.mymomsweather;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.mymomsweather.Model.ChildUser;
import com.postit.mymomsweather.Model.ParentUser;
import com.postit.mymomsweather.databinding.ActivityProfileBinding;

import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    static String TAG = "Login Activity";

    private ActivityProfileBinding binding;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bindLoginButton();
        bindSignUpButton();
        bindInfoRegisterButton();
        bindParentFindButton();
//        timepicker_set();

    }

    private void bindParentFindButton() {
        binding.parentFindButton.setOnClickListener((v)->{
            Intent intent = new Intent(this, ParentRegisterActivity.class);
            startActivity(intent);
        });
    }

    private void bindInfoRegisterButton() {
        binding.registerUserInfoButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, MyInfoActivity.class);
            startActivity(intent);
        });
    }

    private void bindSignUpButton() {
        binding.signUpButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initViews();
    }

    private void initViews() {
        FirebaseUser user = auth.getCurrentUser();
        updateUI(user);
    }

    private boolean checkLogin() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }

    void updateUI(FirebaseUser user) {
        if (user == null) {
            binding.idEditText.setText(null);
            binding.idEditText.setEnabled(true);
            binding.passwordEditText.setEnabled(true);
            binding.idEditText.setText("");
            binding.accountButtonLayout.setVisibility(View.VISIBLE);
            binding.accountInfoButtonLayout.setVisibility(View.GONE);
            binding.passwordEditText.setText("");
            binding.loginButton.setText("로그인");
        } else {
            binding.idEditText.setText(user.getEmail());
            binding.idEditText.setEnabled(false);
            binding.passwordEditText.setText("******");
            binding.passwordEditText.setEnabled(false);
            binding.accountInfoButtonLayout.setVisibility(View.VISIBLE);
            binding.accountButtonLayout.setVisibility(View.GONE);
            binding.loginButton.setText("로그아웃");
        }
    }

    void bindLoginButton() {
        binding.loginButton.setOnClickListener((v) -> {
            if (auth.getCurrentUser() == null) {
                String id = binding.idEditText.getEditableText().toString();
                String password = binding.passwordEditText.getEditableText().toString();
                if (id.equals("")) {
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                requestLogin(id, password);
            } else {
                binding.progressView.setVisibility(View.VISIBLE);
                auth.signOut();
                updateUI(null);
                binding.progressView.setVisibility(View.GONE);
            }
        });
    }

    void requestLogin(String id, String password) {
        binding.progressView.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        binding.progressView.setVisibility(View.GONE);
                    }
                });
    }

}
