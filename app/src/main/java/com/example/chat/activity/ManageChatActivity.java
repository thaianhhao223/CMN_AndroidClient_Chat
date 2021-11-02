package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;

public class ManageChatActivity extends AppCompatActivity {
    private ImageView imageViewOptionAuth ,imgViewManagerContactregular;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_chat);
        imageViewOptionAuth = findViewById(R.id.imgViewAddFriend);
        imgViewManagerContactregular = findViewById(R.id.imgViewManagerContactregular);

        imgViewManagerContactregular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageChatActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
        imageViewOptionAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageChatActivity.this, OptionAuthActivity.class);
                startActivity(intent);
            }
        });
    }
}
