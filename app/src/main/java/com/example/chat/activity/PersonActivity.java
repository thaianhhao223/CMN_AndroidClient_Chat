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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chat.CricleImage;
import com.example.chat.R;
import com.example.chat.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonActivity extends AppCompatActivity {
    private String id_user, id_user_request ="";
    private CricleImage imgprofilefindAvtperson;
    private ImageView imgViewprofilfindBack;
    private TextView tvprofilefindName;
    private TextView tvprofilefindBirthday;
    private TextView tvprofilefindAdress;
    private TextView btnprofilefindAddFr;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_find);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Intent intent = getIntent();
        id_user_request = intent.getStringExtra("id_user");

        imgprofilefindAvtperson = findViewById(R.id.imgprofilefindAvtperson);
        imgViewprofilfindBack = findViewById(R.id.imgViewprofilfindBack);
        tvprofilefindName = findViewById(R.id.tvprofilefindName);
        tvprofilefindBirthday = findViewById(R.id.tvprofilefindBirthday);
        tvprofilefindAdress = findViewById(R.id.tvprofilefindAdress);
        btnprofilefindAddFr = findViewById(R.id.btnprofilefindAddFr);

        GetUser(id_user_request);
        id_user = user.getUid();
        CheckRequestAddfriend(id_user,id_user_request);



        btnprofilefindAddFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = btnprofilefindAddFr.getText().toString();
                if(status.equals("Kết bạn")){
                    RequestAddFriend(id_user,id_user_request);
                }
                else{
                    DeleteRequestAddFriend(id_user,id_user_request);
                }
            }
        });
        imgViewprofilfindBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, FindPersonActivity.class);
                startActivity(intent);
            }
        });
    }
    public void GetUser(String user){
        RequestQueue queue = Volley.newRequestQueue(PersonActivity.this);
        String url = "http://192.168.1.107:3000/Users/" + user;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(response.length() > 0){
                                    tvprofilefindName.setText(object.getString("name"));
                                    tvprofilefindAdress.setText(object.getString("address"));
                                    tvprofilefindBirthday.setText(object.getString("birthday"));
                                    Glide.with(PersonActivity.this).load(object.getString("url_avatar")).into(imgprofilefindAvtperson);
                            }else{
                                Toast.makeText(PersonActivity.this, "respone has error", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void RequestAddFriend(String user, String user_request){
        RequestQueue queue = Volley.newRequestQueue(PersonActivity.this);
        String url = "http://192.168.1.107:3000/Friendrequest?id_user="+user+"&id_user_request="+user_request;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getString("message").equals("Insert success!")){
                                if (btnprofilefindAddFr.getText().equals("Kết bạn")){
                                    btnprofilefindAddFr.setText("Hủy lời mời kết bạn");
                                }else {
                                    btnprofilefindAddFr.setText("Kết bạn");
                                }
                            }else{
                                Toast.makeText(PersonActivity.this, "respone has error", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        Log.d("String request:", stringRequest.toString());
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void DeleteRequestAddFriend(String user, String user_request){
        RequestQueue queue = Volley.newRequestQueue(PersonActivity.this);
        String url = "http://192.168.1.107:3000/Friendrequest?id_user="+user+"&id_user_request="+user_request;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getString("message").equals("Delete success!")){
                                if (btnprofilefindAddFr.getText().equals("Kết bạn")){
                                    btnprofilefindAddFr.setText("Hủy lời mời kết bạn");
                                }else {
                                    btnprofilefindAddFr.setText("Kết bạn");
                                }
                            }else{
                                Toast.makeText(PersonActivity.this, "respone has error", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        Log.d("String request:", stringRequest.toString());
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void CheckRequestAddfriend(String user, String user_request){
        RequestQueue queue = Volley.newRequestQueue(PersonActivity.this);
        String url = "http://192.168.1.107:3000/Friendrequest?id_user="+user+"&id_user_request="+user_request;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            if(response != null && response.length() > 0){
                                btnprofilefindAddFr.setText("Hủy lời mời kết bạn");
                            }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        Log.d("Check String request:", stringRequest.toString());
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
