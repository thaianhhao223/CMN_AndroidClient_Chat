package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;

public class OptionAuthActivity extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextView tvLogOut;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_auth);
        tvLogOut = findViewById(R.id.tvAuthLogout);

        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intent = new Intent(OptionAuthActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void signOut() {
        auth.signOut();
    }
}
