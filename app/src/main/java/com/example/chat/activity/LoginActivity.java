package com.example.chat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextView tvSignUp;
    private TextView edtEmailSignin;
    private TextView edtPasswordSignin;
    private Button btnLogin;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String email, password;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified() == true) {
            startActivity(new Intent(LoginActivity.this, ManageChatActivity.class));
            finish();
        }
        edtEmailSignin = findViewById(R.id.edtPhoneNumberLogin);
        edtPasswordSignin = findViewById(R.id.edtPasswordLogin);

        btnLogin = findViewById(R.id.btnLogin);

        tvSignUp = findViewById(R.id.tvSign_up);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        if (email != null)
            edtEmailSignin.setText(email);
        if (password != null)
            edtPasswordSignin.setText(password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailSignin.getText().toString();
                final String password = edtPasswordSignin.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(getApplicationContext(),"Sai tài khoản hoặc mật khẩu!",Toast.LENGTH_LONG).show();
                                } else {
                                    if (auth.getCurrentUser().isEmailVerified()){
                                        Intent intent = new Intent(LoginActivity.this, ManageChatActivity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Vui lòng xác nhận email!", Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                }
                            }
                        });

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupStep1Activity.class);
                startActivity(intent);
            }
        });

    }

}