package com.example.chat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;

public class ManageChatActivity extends AppCompatActivity {
    private ImageView imageViewOptionAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_chat);
        imageViewOptionAuth = findViewById(R.id.imgAuthOption);

        imageViewOptionAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageChatActivity.this, OptionAuthActivity.class);
                startActivity(intent);
            }
        });
    }
}
