package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.example.chat.handler.IPCONFIG;

public class InforApplicationActivity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private ImageView imgViewInforApplicationBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infor_application);

        imgViewInforApplicationBack = findViewById(R.id.imgViewInforApplicationBack);

        imgViewInforApplicationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InforApplicationActivity.this, OptionAuthActivity.class);
                startActivity(intent);
            }
        });
    }
}
