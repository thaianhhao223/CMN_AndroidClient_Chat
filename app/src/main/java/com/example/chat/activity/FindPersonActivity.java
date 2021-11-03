package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class FindPersonActivity extends AppCompatActivity {
    private ImageView imgViewFindPersonBack;
    private Button btnFindPerson;
    private EditText edtFindpersonbyphonenumber;
    private String phonenumber;
    private FirebaseAuth auth;
    private FirebaseUser user;

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
                phonenumber = edtFindpersonbyphonenumber.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(FindPersonActivity.this);
                String url = "http://192.168.1.107:3000/Users/phonenumber?phonenumber=" + phonenumber;
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
                                    String id_user = "";
                                    try {
                                        object = new JSONObject(response);
                                        id_user = object.getString("id_user");
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                    if (id_user.equals(user.getUid())){
                                        Toast.makeText(FindPersonActivity.this, "Đây là số điện thoại của bạn!", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Intent intent = new Intent(FindPersonActivity.this, PersonActivity.class);
                                        intent.putExtra("id_user",id_user);
                                        startActivity(intent);
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
        });

        imgViewFindPersonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPersonActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }
}
