package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    private EditText edtChangePassOldpass, edtChangePassNewpass,edtChangePassPassAgain;
    private Button btnChangePassComfirm;
    private TextView tvBackScreenChangePass;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String email,oldpass,newpass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass);

        edtChangePassOldpass = findViewById(R.id.edtChangePassOldpass);
        edtChangePassNewpass = findViewById(R.id.edtChangePassNewpass);
        edtChangePassPassAgain = findViewById(R.id.edtChangePassPassAgain);
        btnChangePassComfirm = findViewById(R.id.btnChangePassComfirm);
        tvBackScreenChangePass = findViewById(R.id.tvBackScreenChangePass);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        tvBackScreenChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassActivity.this, ManageChatActivity.class);
                startActivity(intent);
            }
        });
    }
    public boolean Valid(String pass, String comfirm){
        if(pass.length() < 6){
            Toast.makeText(getApplicationContext(), "Mật khẩu quá ngắn (ít nhất 6 kí tự)!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!pass.equals(comfirm)){
            Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
