package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.adapter.ListFriendAdapter;
import com.example.chat.adapter.ListFriendRequestAdapter;
import com.example.chat.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    private ImageView imgViewContactChatRegular, imgViewAddFriend;
    private TextView tvNumberRequestAddfriend;

    private RecyclerView recyclerView;
    private ListFriendAdapter listFriendAdapter;
    private ArrayList<User> listUser = new ArrayList<>();

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String id_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_book);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        GetUserFriend(user.getUid());

        imgViewContactChatRegular = findViewById(R.id.imgViewContactChatRegular);
        imgViewAddFriend = findViewById(R.id.imgViewAddFriend);
        tvNumberRequestAddfriend = findViewById(R.id.tvNumberRequestAddfriend);

        id_user = user.getUid();
        GetCountRequest(id_user);
        recyclerView = findViewById(R.id.rcvFriends);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listFriendAdapter = new ListFriendAdapter(ContactActivity.this, listUser);
                recyclerView.setAdapter(listFriendAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ContactActivity.this));
            }
        },1000);



        tvNumberRequestAddfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ListRequestAddFrActivity.class);
                startActivity(intent);
            }
        });
        imgViewAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, FindPersonActivity.class);
                startActivity(intent);
            }
        });
        imgViewContactChatRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ManageChatActivity.class);
                startActivity(intent);
            }
        });
    }

    public void GetCountRequest(String user){
        RequestQueue queue = Volley.newRequestQueue(ContactActivity.this);
        String url = "http://192.168.1.107:3000/Friendrequest/countRequest?id_user="+user;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(response.length() > 0){
                                String count = "Lời mời kết bạn ("+object.getString("count")+")";
                                tvNumberRequestAddfriend.setText(count);
                                Log.d("Count request addfr:", object.getString("count"));
                            }else{
                                Toast.makeText(ContactActivity.this, "respone has error", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ContactActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void GetUserFriend(String user){
        RequestQueue queue = Volley.newRequestQueue(ContactActivity.this);
        String url = "http://192.168.1.107:3000/Friend/listUser?id_user=" + user;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<User>>(){}.getType();
                        listUser = gson.fromJson(response, listType);
                        for (int i = 0; i < listUser.size(); i++){
                            Log.d("User",listUser.get(i).toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Erro:", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
