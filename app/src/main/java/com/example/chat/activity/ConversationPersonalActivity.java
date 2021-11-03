package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.example.chat.adapter.ConversationAdapter;
import com.example.chat.adapter.ListFriendAdapter;
import com.example.chat.entity.Message;
import com.example.chat.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConversationPersonalActivity extends AppCompatActivity {
    private ImageView imgViewConversationPersonalBack ,imgViewConversationPersonalSend;
    private TextView tvNameReceive;
    private EditText edtSendMessage;
    private ArrayList<Message> listMessage;
    private String id_chatroom;

    private RecyclerView recyclerView;
    private ConversationAdapter conversationAdapter;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String id_user,id_friend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_self);

        imgViewConversationPersonalSend = findViewById(R.id.imgViewConversationPersonalSend);
        imgViewConversationPersonalBack = findViewById(R.id.imgViewConversationPersonalBack);
        tvNameReceive = findViewById(R.id.tvNameReceive);
        edtSendMessage = findViewById(R.id.edtSendMessage);

        recyclerView = findViewById(R.id.rcvConversationPersonal);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id_user = user.getUid();
        Intent intent = getIntent();
        tvNameReceive.setText(intent.getStringExtra("name"));
        id_friend = intent.getStringExtra("id_user");

        Log.d("id_user",id_user);
        Log.d("id_friend",id_friend);
        GetChatRoom(id_user,id_friend);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetMessageStored(id_chatroom);
            }
        },500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                conversationAdapter = new ConversationAdapter(ConversationPersonalActivity.this, listMessage,id_user);
                recyclerView.setAdapter(conversationAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ConversationPersonalActivity.this));
            }
        },1000);

        imgViewConversationPersonalSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSendMessage.getText().toString().length() > 0)
                    StoredATextMessage(edtSendMessage.getText().toString());
                else
                    Toast.makeText(ConversationPersonalActivity.this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
            }
        });
        imgViewConversationPersonalBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConversationPersonalActivity.this, ManageChatActivity.class);
                startActivity(intent);
            }
        });
    }
    public void GetChatRoom(String user,String friend){
        RequestQueue queue = Volley.newRequestQueue(ConversationPersonalActivity.this);
        String url = "http://192.168.1.107:3000/ConversationPersonal?id_user=" + user+"&id_user_friend="+friend;
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
                                Toast.makeText(ConversationPersonalActivity.this, "Lấy id_roomchat thất bại", Toast.LENGTH_SHORT).show();
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
    public void GetMessageStored(String room){
        RequestQueue queue = Volley.newRequestQueue(ConversationPersonalActivity.this);
        String url = "http://192.168.1.107:3000/MessageStored?id_chatroom=" + room;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JSONObject tam = null;
                        JSONArray arraytam = new JSONArray(),jsonArray = null;
                        String str = null;
                        try {
                            tam = new JSONObject(response);
                            arraytam = (JSONArray) tam.get("Data");
                            tam = (JSONObject) arraytam.get(0);
                            str = (String) tam.getString("content");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Type listType = new TypeToken<List<Message>>(){}.getType();
                        listMessage = gson.fromJson(str, listType);
                        for (int i = 0; i < listMessage.size(); i++){
                            Log.d("User",listMessage.get(i).toString());
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
    public void StoredATextMessage(String message){
        RequestQueue queue = Volley.newRequestQueue(ConversationPersonalActivity.this);
        String url = "http://192.168.1.107:3000/MessageStored?id_chatroom="+id_chatroom+"&id_send="+id_user+
                "&message="+message;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            if(response.length() > 0){
                                Log.d("message","Gửi thành công");
                            }else{
                                Log.d("message","Gửi thất bại");
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
