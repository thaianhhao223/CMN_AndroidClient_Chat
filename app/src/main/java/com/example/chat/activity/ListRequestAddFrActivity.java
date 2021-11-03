package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.example.chat.adapter.ListFriendRequestAdapter;
import com.example.chat.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListRequestAddFrActivity extends AppCompatActivity {
    private ImageView imgViewListRequestAdđFrBack;
    private RecyclerView recyclerView;
    private ListFriendRequestAdapter listFriendRequestAdapter;
    private ArrayList<User> listUser = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_request_addfr);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        GetUserRequest(user.getUid());

        imgViewListRequestAdđFrBack = findViewById(R.id.imgViewListRequestAdđFrBack);
        recyclerView = findViewById(R.id.rcvListRequestFr);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listFriendRequestAdapter = new ListFriendRequestAdapter(ListRequestAddFrActivity.this, listUser);
                recyclerView.setAdapter(listFriendRequestAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ListRequestAddFrActivity.this));
            }
        },1000);


        imgViewListRequestAdđFrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListRequestAddFrActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }

    public void GetUserRequest(String user){
        RequestQueue queue = Volley.newRequestQueue(ListRequestAddFrActivity.this);
        String url = "http://192.168.1.107:3000/Friendrequest/listUser?id_user=" + user;
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
