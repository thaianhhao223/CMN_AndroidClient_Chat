package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chat.R;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupStep3Activity extends AppCompatActivity {
    private Button btnResendEmail,btnSignUpDone;
    private String email,password,fullname,birthday,phonenumber,address;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView tvBackScreen;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen3);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        fullname = intent.getStringExtra("fullname");
        birthday = intent.getStringExtra("birthday");
        phonenumber = intent.getStringExtra("phonenumber");
        address = intent.getStringExtra("address");

        tvBackScreen = findViewById(R.id.tvBackScreenSignUpStep3);
        btnResendEmail = findViewById(R.id.btnResendEmail);
        btnSignUpDone = findViewById(R.id.btnSignUpDone);

        user.sendEmailVerification();
        CreateNewUser(user.getUid().toString(), fullname, birthday, phonenumber, address);



        btnResendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification();
                Toast.makeText(SignupStep3Activity.this, "Đã gửi lại email xác nhận!", Toast.LENGTH_SHORT).show();
            }
        });
        btnSignUpDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep3Activity.this, LoginActivity.class);
                auth.signOut();
                intent.putExtra("email",email.toString());
                intent.putExtra("password",password.toString());
                startActivity(intent);
            }
        });
        tvBackScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep3Activity.this, SignupStep2Activity.class);
                intent.putExtra("email",email.toString());
                intent.putExtra("password",password.toString());
                intent.putExtra("fullname", fullname);
                intent.putExtra("birthday",birthday);
                intent.putExtra("address", address);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
            }
        });
    }
    private void CreateNewUser(String idUser, String fullname, String birthday, String phonenumber, String address){
        // Instantiate the RequestQueue.
        String[] replacebirthday = birthday.split("/");
        String chuoiNamSinh = replacebirthday[2] +"-"+ replacebirthday[1]+"-"+ replacebirthday[0];
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.107:3000/Users?id_user="+idUser+"&name="+fullname+"&birthday="+chuoiNamSinh+"&phonenumber="+phonenumber+"&address="+address;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);

                            if(object.getString("message").equals("Insert success!")){
                                Toast.makeText(SignupStep3Activity.this, "Thêm người dùng thành công", Toast.LENGTH_SHORT).show();
                            }else{
                                Log.d("Troi dat oi",object.toString());
                                Toast.makeText(SignupStep3Activity.this, "respone has error", Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupStep3Activity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });

        Log.d("stringRequest",stringRequest.toString());
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
