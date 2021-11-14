package com.example.chat.activity;

import android.content.Intent;
import android.media.Image;
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
import com.example.chat.R;
import com.example.chat.handler.IPCONFIG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class OptionFriendActivity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private ImageView imgOptionFriendBack;
    private TextView tvOptionFriendChangeName,tvOptionFriendDelete;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String id_user,id_friend;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_delete_fr);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Intent intent = getIntent();
        id_friend = intent.getStringExtra("id_user");

        imgOptionFriendBack = findViewById(R.id.imgOptionFriendBack);
        tvOptionFriendChangeName = findViewById(R.id.tvOptionFriendChangeName);
        tvOptionFriendDelete = findViewById(R.id.tvOptionFriendDelete);

        tvOptionFriendDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFriend(user.getUid(),id_friend);
            }
        });
        imgOptionFriendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionFriendActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }
    public void DeleteFriend(String user,String id_friend){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://"+IP_HOST+":3000/Friend?id_user=" + user+"&id_friend="+id_friend;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        try {
                            if(response.length() > 0){
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(OptionFriendActivity.this, "Xóa bạn thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OptionFriendActivity.this, ContactActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(OptionFriendActivity.this, "Xóa bạn thất bại", Toast.LENGTH_SHORT).show();
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
