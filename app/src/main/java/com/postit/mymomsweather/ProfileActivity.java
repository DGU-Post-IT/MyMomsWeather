package com.postit.mymomsweather;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.mymomsweather.databinding.ActivityProfileBinding;

import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        bindLoginButton();
        bindParenRegisterActivityButton();

    }

    private void bindParenRegisterActivityButton() {
        binding.connectParentActivityButton.setOnClickListener((v)->{
            Intent intent = new Intent(this,ParentRegisterActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        FirebaseUser user = auth.getCurrentUser();
        if(user==null){
            binding.idTextView.setText(null);
            binding.loginButton.setText("로그인하기");
        }else{
            binding.idTextView.setText(user.getEmail());
            binding.loginButton.setText("로그아웃하기");
        }
    }

    private boolean checkLogin() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }

    private void bindLoginButton() {
        binding.loginButton.setOnClickListener((v) -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                loginStart();
            } else {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                binding.idTextView.setText("로그아웃 됨");
                                binding.loginButton.setText("로그인 하기");
                            }
                        });
            }
        });

    }
    // 로그인 결과 콜백 등록
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    // 로그인 결과 콜백 함수
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            binding.idTextView.setText(user.getEmail());
            binding.loginButton.setText("로그아웃 하기");
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Toast.makeText(this,"로그인에 실패했습니다.",Toast.LENGTH_SHORT).show();

        }
    }
    //로그인 화면 시작
    void loginStart() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }
}
