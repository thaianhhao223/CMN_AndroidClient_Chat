package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ProfileRequestAddFriendActivity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private TextView tvProfileRequestAddFriendName,tvProfileRequestAddFriendBirthday,tvProfileRequestAddFriendAdress;
    private CricleImage imgProfileRequestAddFriendAvt;
    private ImageView imgProfileRequestAddFriendBack;
    private LinearLayout btnProfileRequestAddFriendAccept,btnProfileRequestAddFriendDeny;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String id_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_request_add);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id_user");

        imgProfileRequestAddFriendBack = findViewById(R.id.imgProfileRequestAddFriendBack);
        tvProfileRequestAddFriendName = findViewById(R.id.tvProfileRequestAddFriendName);
        tvProfileRequestAddFriendBirthday = findViewById(R.id.tvProfileRequestAddFriendBirthday);
        tvProfileRequestAddFriendAdress = findViewById(R.id.tvProfileRequestAddFriendAdress);
        imgProfileRequestAddFriendAvt = findViewById(R.id.imgProfileRequestAddFriendAvt);
        btnProfileRequestAddFriendAccept = findViewById(R.id.btnProfileRequestAddFriendAccept);
        btnProfileRequestAddFriendDeny = findViewById(R.id.btnProfileRequestAddFriendDeny);


        GetUser(id_user);
        imgProfileRequestAddFriendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileRequestAddFriendActivity.this,ListRequestAddFrActivity.class);
                startActivity(intent);
            }
        });
    }
    public void GetUser(String user){
        RequestQueue queue = Volley.newRequestQueue(ProfileRequestAddFriendActivity.this);
        String url = "http://"+IP_HOST+":3000/Users/" + user;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(response.length() > 0){
                                tvProfileRequestAddFriendName.setText(object.getString("name"));
                                tvProfileRequestAddFriendAdress.setText(object.getString("address"));
                                tvProfileRequestAddFriendBirthday.setText(object.getString("birthday"));
                                Glide.with(ProfileRequestAddFriendActivity.this).load(object.getString("url_avatar")).into(imgProfileRequestAddFriendAvt);
                            }else{
                                Toast.makeText(ProfileRequestAddFriendActivity.this, "respone has error", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileRequestAddFriendActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
