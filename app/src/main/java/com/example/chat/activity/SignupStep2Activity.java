package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;

import java.util.Date;

public class SignupStep2Activity extends AppCompatActivity {
    private Button btnComFirmInforUser;
    private TextView edtFullNameSignUp,edtPhoneNumberSignUp,edtAddressSignUp,tvBackScreen;
    private EditText edtBirthdaySignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen2);
        String email,password,fullname,address,phonenumber;
        Date birthdate;
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        edtPhoneNumberSignUp = findViewById(R.id.edtPhoneNumberLogin);
        edtFullNameSignUp = findViewById(R.id.edtFullNameSignUp);
        edtAddressSignUp = findViewById(R.id.edtAddressSignUp);
        edtBirthdaySignUp = findViewById(R.id.edtBirthdaySignUP);
        tvBackScreen = findViewById(R.id.tvBackScreenSignUpStep2);
        btnComFirmInforUser = findViewById(R.id.btnComfirmInforUser);

        btnComFirmInforUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep2Activity.this, SignupStep3Activity.class);
                intent.putExtra("email",email.toString());
                intent.putExtra("password",password.toString());
                intent.putExtra("fullname",edtFullNameSignUp.getText().toString());
                
                startActivity(intent);
            }
        });
        tvBackScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep2Activity.this, SignupStep1Activity.class);
                startActivity(intent);
            }
        });
    }
}
