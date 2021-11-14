package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.example.chat.R;
import com.example.chat.adapter.ChatRoomAdapter;
import com.example.chat.adapter.ListFriendAdapter;
import com.example.chat.entity.ChatRoom;
import com.example.chat.entity.User;
import com.example.chat.handler.IPCONFIG;
import com.example.chat.socket.SocketClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ManageChatActivity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private ImageView imageViewOptionAuth ,imgViewManagerContactregular, idManagerConversationPersonLight;
    private SocketClient mSocket;
    private Socket socket;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private RecyclerView recyclerView;
    private ChatRoomAdapter chatRoomAdapter;
    private ArrayList<ChatRoom> listChatroom = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_chat);
        imageViewOptionAuth = findViewById(R.id.imgAuthOption);
        imgViewManagerContactregular = findViewById(R.id.imgViewManagerConversationContactregular);
        idManagerConversationPersonLight = findViewById(R.id.idManagerConversationPersonLight);


        // Get firebase user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        // Connect Socket
        mSocket = new SocketClient();
        mSocket.connectSocket();
        socket = mSocket.getmSocket();
        SocketClient.setmSocket(socket);
        socket.on("recive_massage", onNewMessage);
        socket.emit("connection");
        socket.emit("user_login", user.getUid().toString());

        GetAllChatroom(user.getUid());

        recyclerView = findViewById(R.id.rcvChatRoom);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatRoomAdapter = new ChatRoomAdapter(ManageChatActivity.this,listChatroom);
                recyclerView.setAdapter(chatRoomAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ManageChatActivity.this));
            }
        },2000);


        imgViewManagerContactregular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageChatActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
        idManagerConversationPersonLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageChatActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        imageViewOptionAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageChatActivity.this, OptionAuthActivity.class);
                startActivity(intent);
            }
        });
    }
    public void GetAllChatroom(String user){
        RequestQueue queue = Volley.newRequestQueue(ManageChatActivity.this);
        String url = "http://"+IP_HOST+":3000/Chatroom?id_user=" + user;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<ChatRoom>>(){}.getType();
                        listChatroom = gson.fromJson(response, listType);
                        for (int i = 0; i < listChatroom.size(); i++){
                            Log.d("Chatroom",listChatroom.get(i).toString());
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

    private Emitter.Listener onNewMessage =  new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message, usersend;
                    message = data.optString("message");
                    usersend = data.optString("userSend");
                    Log.d("data:",message + usersend);
                }
            });
        }
    };
}
