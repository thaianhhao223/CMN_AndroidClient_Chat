package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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


public class ProfileFriendActivity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private TextView tvProfileFriendName,tvProfileFriendBirthday,tvProfileFriendAdress;
    private CricleImage imgProfileFriendAvt;
    private ImageView imgProfileFriendBack,imgProfileFriendSetting;
    private LinearLayout btnProfileFriendMessage;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String id_friend;
    private String id_chatroom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friends);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Intent intent = getIntent();
        id_friend = intent.getStringExtra("id_user");

        tvProfileFriendName = findViewById(R.id.tvProfileFriendName);
        tvProfileFriendBirthday = findViewById(R.id.tvProfileFriendBirthday);
        tvProfileFriendAdress = findViewById(R.id.tvProfileFriendAdress);
        imgProfileFriendAvt = findViewById(R.id.imgProfileFriendAvt);
        imgProfileFriendBack = findViewById(R.id.imgProfileFriendBack);
        imgProfileFriendSetting = findViewById(R.id.imgProfileFriendSetting);
        btnProfileFriendMessage = findViewById(R.id.btnProfileFriendMessage);

        GetUser(id_friend);

        btnProfileFriendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetChatRoom(user.getUid(),id_friend);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ProfileFriendActivity.this, ConversationPersonalActivity.class);
                        intent.putExtra("id_user",id_friend);
                        intent.putExtra("name",tvProfileFriendName.getText().toString());
                        intent.putExtra("id_chatroom",id_chatroom);
                        startActivity(intent);
                    }
                },1000);
            }
        });
        imgProfileFriendSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileFriendActivity.this, OptionFriendActivity.class);
                intent.putExtra("id_user",id_friend);
                startActivity(intent);
            }
        });
        imgProfileFriendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileFriendActivity.this,ManageChatActivity.class);
                startActivity(intent);
            }
        });
    }
    public void GetUser(String user){
        RequestQueue queue = Volley.newRequestQueue(ProfileFriendActivity.this);
        String url = "http://"+IP_HOST+":3000/Users/" + user;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(response.length() > 0){
                                tvProfileFriendName.setText(object.getString("name"));
                                tvProfileFriendAdress.setText(object.getString("address"));
                                tvProfileFriendBirthday.setText(object.getString("birthday"));
                                Glide.with(ProfileFriendActivity.this).load(object.getString("url_avatar")).into(imgProfileFriendAvt);
                            }else{
                                Toast.makeText(ProfileFriendActivity.this, "respone has error", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileFriendActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void GetChatRoom(String user,String friend){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://"+IP_HOST+":3000/ConversationPersonal?id_user=" + user+"&id_user_friend="+friend;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        try {
                            if(response.length() > 0){
                                JSONObject object = new JSONObject(response);
                                id_chatroom = object.getString("id_chatroom");
                            }else{
                                Toast.makeText(ProfileFriendActivity.this, "Lấy id_roomchat thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Erro:", error.toString());
            }
        });
        Log.d("url",stringRequest.toString());
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
