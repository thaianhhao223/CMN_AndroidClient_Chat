package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;

public class ConversationPersonalActivity extends AppCompatActivity {
    private ImageView imgViewConversationPersonalBack;
    private TextView tvNameReceive;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_self);

        imgViewConversationPersonalBack = findViewById(R.id.imgViewConversationPersonalBack);
        tvNameReceive = findViewById(R.id.tvNameReceive);

        Intent intent = getIntent();
        tvNameReceive.setText(intent.getStringExtra("name"));
        imgViewConversationPersonalBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConversationPersonalActivity.this, ManageChatActivity.class);
                startActivity(intent);
            }
        });
    }
}
