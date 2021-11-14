package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.chat.entity.User;
import com.example.chat.handler.IPCONFIG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class FindPersonActivity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();

    private ImageView imgViewFindPersonBack;
    private Button btnFindPerson;
    private EditText edtFindpersonbyphonenumber;
    private String phonenumber;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Boolean checkfr =false;
    private String id_user = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        imgViewFindPersonBack = findViewById(R.id.imgViewFindPersonBack);
        edtFindpersonbyphonenumber = findViewById(R.id.edtFindpersonbyphonenumber);
        btnFindPerson = findViewById(R.id.btnFindPerson);


        btnFindPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindFriend();
            }
        });

        imgViewFindPersonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPersonActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }
    public void FindFriend(){
        phonenumber = edtFindpersonbyphonenumber.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(FindPersonActivity.this);
        String url = "http://"+IP_HOST+":3000/Users/phonenumber?phonenumber=" + phonenumber;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            Toast.makeText(FindPersonActivity.this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Response:", response.toString());
                        if (response.length() > 0) {
                            //Toast.makeText(FindPersonActivity.this, "Tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
                            JSONObject object = null;
                            try {
                                object = new JSONObject(response);
                                id_user = object.getString("id_user");
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            if (id_user.equals(user.getUid())){
                                Toast.makeText(FindPersonActivity.this, "Đây là số điện thoại của bạn!", Toast.LENGTH_SHORT).show();
                            }else {
                                CheckFriend(user.getUid(),id_user);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(checkfr == true){
                                            Intent intent = new Intent(FindPersonActivity.this, PersonActivity.class);
                                            intent.putExtra("id_user",id_user);
                                            startActivity(intent);
                                        }else {
                                            Intent intent = new Intent(FindPersonActivity.this, ProfileFriendActivity.class);
                                            intent.putExtra("id_user",id_user);
                                            startActivity(intent);
                                        }
                                    }
                                },1000);

                            }
                        } else {
                            Toast.makeText(FindPersonActivity.this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FindPersonActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void CheckFriend(String id_user,String id_friend){
        RequestQueue queue = Volley.newRequestQueue(FindPersonActivity.this);
        String url = "http://"+IP_HOST+":3000/Friend?id_user=" + id_user+"&id_user_check="+ id_friend;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            checkfr = true;
                        }
                        Log.d("Response:", response.toString());
                        if (response.length() > 0) {
                            checkfr = false;
                        } else {
                            checkfr = true;
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
