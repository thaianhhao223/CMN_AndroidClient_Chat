package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.example.chat.handler.IPCONFIG;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupStep1Activity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private Button btnDangKy;
    private TextView edtEmailSignUp, edtComfirmPassSignUp,edtPassSignUp, tvBackScreen;
    private String email,password;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_screen);

        edtComfirmPassSignUp = findViewById(R.id.edtComfirmPassSignUp);
        edtPassSignUp = findViewById(R.id.edtPassSignUp);
        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        tvBackScreen = findViewById(R.id.tvBackScreenSignUpStep1);
        btnDangKy = findViewById(R.id.btnSignUpComfirm);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        edtEmailSignUp.setText(email);
        edtPassSignUp.setText(password);
        edtComfirmPassSignUp.setText(password);

        edtEmailSignUp.setText("thienprocs@gmail.com");
        edtPassSignUp.setText("Nhoxrin223");
        edtComfirmPassSignUp.setText("Nhoxrin223");

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailSignUp.getText().toString().trim();
                String password = edtPassSignUp.getText().toString().trim();
                String comfirmpassword = edtComfirmPassSignUp.getText().toString().trim();
                if(Valid(password, comfirmpassword, email)){
                    createAccountFireBase(email,password);
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
    public boolean Valid(String pass, String comfirm, String email){
//        String reg = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+)$";
//        if(!email.matches(reg)){
//            Toast.makeText(getApplicationContext(), "Sai định dạng email!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
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
    public void createAccountFireBase(String emailFB, String passwordFB){
        auth.createUserWithEmailAndPassword(emailFB.toString(),passwordFB.toString()).addOnCompleteListener(
                SignupStep1Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupStep1Activity.this, "Tài khoản hợp lệ!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupStep1Activity.this, SignupStep2Activity.class);
                            intent.putExtra("email",emailFB.toString());
                            intent.putExtra("password",passwordFB.toString());
                            startActivity(intent);
                        }else {
                            Toast.makeText(SignupStep1Activity.this, "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}
