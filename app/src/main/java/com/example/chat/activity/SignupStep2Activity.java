package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;

public class SignupStep2Activity extends AppCompatActivity {
    private Button btnComFirmInforUser;
    private TextView edtFullNameSignUp,edtPhoneNumberSignUp,edtAddressSignUp,tvBackScreen;
    private EditText edtBirthdaySignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen2);
        String email,password,fullname,address,phonenumber,birthday;

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        fullname = intent.getStringExtra("fullname");
        birthday = intent.getStringExtra("birthday");
        phonenumber = intent.getStringExtra("phonenumber");
        address = intent.getStringExtra("address");

        edtPhoneNumberSignUp = findViewById(R.id.edtPhoneNumberSignUp);
        edtFullNameSignUp = findViewById(R.id.edtFullNameSignUp);
        edtAddressSignUp = findViewById(R.id.edtAddressSignUp);
        edtBirthdaySignUp = findViewById(R.id.edtBirthdaySignUP);
        tvBackScreen = findViewById(R.id.tvBackScreenSignUpStep2);
        btnComFirmInforUser = findViewById(R.id.btnSignUpStep2Confirm);

        if (fullname != null && fullname.length() > 0)
            edtFullNameSignUp.setText(fullname);
        if (birthday != null && birthday.length() > 0) {
            birthday.replace("-", "/");
            edtBirthdaySignUp.setText(birthday);
        }
        if (phonenumber != null && phonenumber.length() > 0)
            edtPhoneNumberSignUp.setText(phonenumber);
        if (address != null && address.length() > 0)
            edtAddressSignUp.setText(address);

        edtFullNameSignUp.setText("Thái Anh Hào");
        edtBirthdaySignUp.setText("10/11/2000");
        edtAddressSignUp.setText("Phú Mỹ Phú Tân An Giang");
        edtPhoneNumberSignUp.setText("0362977707");

        btnComFirmInforUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep2Activity.this, SignupStep3Activity.class);
                intent.putExtra("email",email.toString());
                intent.putExtra("password",password.toString());
                intent.putExtra("fullname",edtFullNameSignUp.getText().toString());
                intent.putExtra("birthday",edtBirthdaySignUp.getText().toString() );
                intent.putExtra("phonenumber",edtPhoneNumberSignUp.getText().toString());
                intent.putExtra("address", edtAddressSignUp.getText().toString());
                startActivity(intent);
            }
        });
        tvBackScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep2Activity.this, SignupStep1Activity.class);
                intent.putExtra("email",email.toString());
                intent.putExtra("password",password.toString());
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
