package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;

public class ListRequestAddFrActivity extends AppCompatActivity {
    private ImageView imgViewListRequestAdđFrBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_request_addfr);

        imgViewListRequestAdđFrBack = findViewById(R.id.imgViewListRequestAdđFrBack);


        imgViewListRequestAdđFrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListRequestAddFrActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }
}
