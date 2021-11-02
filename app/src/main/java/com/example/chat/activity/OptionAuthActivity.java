package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;

public class OptionAuthActivity extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextView tvLogOut, tvInforAuth, tvChangePass, tvInforApp;
    private ImageView imgViewOptionAuthBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_auth);
        tvLogOut = findViewById(R.id.tvAuthLogout);
        tvInforAuth = findViewById(R.id.tvInforAuth);
        tvChangePass = findViewById(R.id.tvChangePass);
        tvInforApp = findViewById(R.id.tvInforApp);
        imgViewOptionAuthBack = findViewById(R.id.imgViewOptionAuthBack);


        tvInforApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionAuthActivity.this, InforApplicationActivity.class);
                startActivity(intent);
            }
        });
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionAuthActivity.this, ChangePassActivity.class);
                startActivity(intent);
            }
        });
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intent = new Intent(OptionAuthActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        imgViewOptionAuthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionAuthActivity.this, ManageChatActivity.class);
                startActivity(intent);
            }
        });
    }
    public void signOut() {
        auth.signOut();
    }
}
