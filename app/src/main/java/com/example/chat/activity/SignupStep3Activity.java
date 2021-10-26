package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupStep3Activity extends AppCompatActivity {
    private Button btnConfirmOTP;
    private String email,password;
    private FirebaseAuth auth;
    private TextView tvBackScreen;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen3);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser;
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        tvBackScreen = findViewById(R.id.tvBackScreenSignUpStep3);
        btnConfirmOTP = findViewById(R.id.btnResendEmail);

        auth.createUserWithEmailAndPassword(email.toString(),password.toString()).addOnCompleteListener(
                SignupStep3Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupStep3Activity.this,"Create user!", Toast.LENGTH_LONG).show();
                            FirebaseUser user = task.getResult().getUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("System:", "Email sent.");
                                    }
                                }
                            });
//                          auth.getCurrentUser().sendEmailVerification()
//                          auth.signOut();
                        }else {
                            Toast.makeText(SignupStep3Activity.this,"Fail to create user!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        btnConfirmOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep3Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        tvBackScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep3Activity.this, SignupStep2Activity.class);
                startActivity(intent);
            }
        });
    }
}
