package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.example.chat.handler.IPCONFIG;

import org.w3c.dom.Text;

public class InforApplicationActivity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private ImageView imgViewInforApplicationBack;
    private TextView tvInforApp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infor_application);

        imgViewInforApplicationBack = findViewById(R.id.imgViewInforApplicationBack);
        tvInforApp = findViewById(R.id.tvInforApp);

        String infor = "Ứng dụng chat Nhóm 2:\n"+
                "Sinh viên thực hiện: Thái Anh Hào\n"+
                "Sinh viên thực hiện: Nguyễn Ngọc Hậu\n"+
                "Ứng dụng được viết nên nhằm tạo nơi một kênh liên lạc cho các bạn trong nhóm và tìm hiểu thêm các kiến thức mới về ngành Công nghệ thông tin";
        tvInforApp.setText(infor);
        imgViewInforApplicationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InforApplicationActivity.this, OptionAuthActivity.class);
                startActivity(intent);
            }
        });
    }
}
