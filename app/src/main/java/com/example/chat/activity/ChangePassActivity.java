package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    private EditText edtChangePassOldpass, edtChangePassNewpass,edtChangePassPassAgain;
    private Button btnChangePassComfirm;
    private TextView tvBackScreenChangePass;
    private FirebaseAuth auth;
    private FirebaseUser usercheck,user;
    private String email,comfirmpass,newpass,password;
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
        usercheck = auth.getCurrentUser();

        email = user.getEmail();




        btnChangePassComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newpass = edtChangePassNewpass.getText().toString().trim();
                comfirmpass = edtChangePassPassAgain.getText().toString().trim();
                password = edtChangePassOldpass.getText().toString().trim();

                if(Valid(newpass,comfirmpass)){
                    auth.signOut();
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(ChangePassActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        Toast.makeText(getApplicationContext(),"Sai mật khẩu cũ!",Toast.LENGTH_LONG).show();
                                    } else {
                                        user.updatePassword(comfirmpass.toString().trim())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(ChangePassActivity.this, "Đã cập nhật mật khẩu, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(ChangePassActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
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
            Toast.makeText(getApplicationContext(), "Mật khẩu mới quá ngắn (ít nhất 6 kí tự)!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!pass.equals(comfirm)){
            Toast.makeText(getApplicationContext(), "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
