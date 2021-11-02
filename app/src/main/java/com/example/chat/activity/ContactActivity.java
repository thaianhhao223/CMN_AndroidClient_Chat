package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;

public class ContactActivity extends AppCompatActivity {
    private ImageView imgViewContactChatRegular, imgViewAddFriend;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_book);

        imgViewContactChatRegular = findViewById(R.id.imgViewContactChatRegular);
        imgViewAddFriend = findViewById(R.id.imgViewAddFriend);
        imgViewContactChatRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ManageChatActivity.class);
                startActivity(intent);
            }
        });
    }
}
