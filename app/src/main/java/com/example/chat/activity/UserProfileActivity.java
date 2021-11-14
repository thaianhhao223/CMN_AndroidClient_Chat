package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chat.CricleImage;
import com.example.chat.R;
import com.example.chat.handler.IPCONFIG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private TextView tvUserProfileName,tvUserProfileBirthday,tvUserProfileAdress;
    private CricleImage imgUserProfileAvt;
    private ImageView imgUserProfileBack,imgUserProfileChangeProfile,idUserProfileContactLight,idUserProfileChatLight;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String id_friend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_sefl);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        tvUserProfileName = findViewById(R.id.tvUserProfileName);
        tvUserProfileBirthday = findViewById(R.id.tvUserProfileBirthday);
        tvUserProfileAdress = findViewById(R.id.tvUserProfileAdress);
        imgUserProfileAvt = findViewById(R.id.imgUserProfileAvt);
        imgUserProfileBack = findViewById(R.id.imgUserProfileBack);
        imgUserProfileChangeProfile = findViewById(R.id.imgUserProfileChangeProfile);
        idUserProfileContactLight = findViewById(R.id.idUserProfileContactLight);
        idUserProfileChatLight = findViewById(R.id.idUserProfileChatLight);

        GetUser(user.getUid().toString());

        imgUserProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,ManageChatActivity.class);
                startActivity(intent);
            }
        });
        idUserProfileChatLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,ManageChatActivity.class);
                startActivity(intent);
            }
        });
        idUserProfileContactLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,ContactActivity.class);
                startActivity(intent);
            }
        });
    }
    public void GetUser(String user){
        RequestQueue queue = Volley.newRequestQueue(UserProfileActivity.this);
        String url = "http://"+IP_HOST+":3000/Users/" + user;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(response.length() > 0){
                                tvUserProfileName.setText(object.getString("name"));
                                tvUserProfileAdress.setText(object.getString("address"));
                                tvUserProfileBirthday.setText(object.getString("birthday"));
                                Glide.with(UserProfileActivity.this).load(object.getString("url_avatar")).into(imgUserProfileAvt);
                            }else{
                                Toast.makeText(UserProfileActivity.this, "respone has error", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserProfileActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

