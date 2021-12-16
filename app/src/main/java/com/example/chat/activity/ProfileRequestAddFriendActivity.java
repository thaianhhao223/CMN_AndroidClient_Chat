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

        btnProfileRequestAddFriendAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriend(user.getUid(), id_user);
                Intent intent = new Intent(ProfileRequestAddFriendActivity.this,ListRequestAddFrActivity.class);
                startActivity(intent);
            }
        });
        btnProfileRequestAddFriendDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DenyRequestFriend(user.getUid(), id_user);
                Intent intent = new Intent(ProfileRequestAddFriendActivity.this,ListRequestAddFrActivity.class);
                startActivity(intent);
            }
        });
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

    public void AddFriend(String user,String id_user_request){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://"+IP_HOST+":3000/Friend?id_user=" + user+"&id_user_request="+id_user_request;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        try {
                            if(response.length() > 0){
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(ProfileRequestAddFriendActivity.this, "Thêm bạn thành công", Toast.LENGTH_SHORT).show();
                                CreateNewChatRoom(user,id_user_request);
                            }else{
                                Toast.makeText(ProfileRequestAddFriendActivity.this, "Thêm bạn thất bại", Toast.LENGTH_SHORT).show();
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
    public void CreateNewChatRoom(String user,String id_user_request){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://"+IP_HOST+":3000/Chatroom?id_user=" + user+"&id_user_request="+id_user_request;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        try {
                            if(response.length() > 0){
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(ProfileRequestAddFriendActivity.this, "Thêm phòng chat mới thành công", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ProfileRequestAddFriendActivity.this, "Thêm phòng chat mới thất bại", Toast.LENGTH_SHORT).show();
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
    public void DenyRequestFriend(String user,String id_user_request){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://"+IP_HOST+":3000/Friendrequest?id_user="+id_user_request+"&id_user_request="+user;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getString("message").equals("Delete success!")){
                                Toast.makeText(ProfileRequestAddFriendActivity.this, "Xóa lời mời kết bạn thành công!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ProfileRequestAddFriendActivity.this, "Xóa lời mời kết bạn thất bại!", Toast.LENGTH_SHORT).show();
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
        Log.d("String request:", stringRequest.toString());
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
