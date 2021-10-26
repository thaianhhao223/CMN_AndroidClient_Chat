package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;

public class SignupStep1Activity extends AppCompatActivity {
    private Button btnDangKy;
    private TextView edtEmailSignUp, edtComfirmPassSignUp,edtPassSignUp, tvBackScreen;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_screen);

        edtComfirmPassSignUp = findViewById(R.id.edtComfirmPassSignUp);
        edtPassSignUp = findViewById(R.id.edtPassSignUp);
        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        tvBackScreen = findViewById(R.id.tvBackScreenSignUpStep1);
        btnDangKy = findViewById(R.id.btnComfirmInforUser);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailSignUp.getText().toString().trim();
                String password = edtPassSignUp.getText().toString().trim();
                String comfirmpassword = edtComfirmPassSignUp.getText().toString().trim();
                if(ValidPassword(password, comfirmpassword, email)){
                    Intent intent = new Intent(SignupStep1Activity.this, SignupStep2Activity.class);
                    intent.putExtra("email",email.toString());
                    intent.putExtra("password",password.toString());
                    startActivity(intent);
                }
            }
        });
        tvBackScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep1Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public boolean ValidPassword(String pass, String comfirm,String email){
        String reg = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+)$";
        if(!email.matches(reg)){
            Toast.makeText(getApplicationContext(), "Sai định dạng email!", Toast.LENGTH_SHORT).show();
            return false;
        }
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
