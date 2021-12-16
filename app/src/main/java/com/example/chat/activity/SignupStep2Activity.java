package com.example.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.chat.R;
import com.example.chat.handler.IPCONFIG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupStep2Activity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private Button btnComFirmInforUser;
    private TextView edtFullNameSignUp, edtPhoneNumberSignUp, edtAddressSignUp, tvBackScreen;
    private EditText edtBirthdaySignUp;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Boolean kiemtrasdt = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen2);
        String email, password, fullname, address, phonenumber, birthday;

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        fullname = intent.getStringExtra("fullname");
        birthday = intent.getStringExtra("birthday");
        phonenumber = intent.getStringExtra("phonenumber");
        address = intent.getStringExtra("address");

        edtPhoneNumberSignUp = findViewById(R.id.edtPhoneNumberSignUp);
        edtFullNameSignUp = findViewById(R.id.edtFullNameSignUp);
        edtAddressSignUp = findViewById(R.id.edtAddressSignUp);
        edtBirthdaySignUp = findViewById(R.id.edtBirthdaySignUP);
        tvBackScreen = findViewById(R.id.tvBackScreenSignUpStep2);
        btnComFirmInforUser = findViewById(R.id.btnSignUpStep2Confirm);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (fullname != null && fullname.length() > 0)
            edtFullNameSignUp.setText(fullname);
        if (birthday != null && birthday.length() > 0) {
            birthday.replace("-", "/");
            edtBirthdaySignUp.setText(birthday);
        }
        if (phonenumber != null && phonenumber.length() > 0)
            edtPhoneNumberSignUp.setText(phonenumber);
        if (address != null && address.length() > 0)
            edtAddressSignUp.setText(address);

        edtFullNameSignUp.setText("Thái Anh Hào");
        edtBirthdaySignUp.setText("10/11/2000");
        edtAddressSignUp.setText("Phú Mỹ Phú Tân An Giang");
        edtPhoneNumberSignUp.setText("0362977707");

        btnComFirmInforUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhoneNumber();
                String name = edtFullNameSignUp.getText().toString();
                String date = edtBirthdaySignUp.getText().toString();
                String phone = edtPhoneNumberSignUp.getText().toString();
                String add = edtAddressSignUp.getText().toString();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(kiemtrasdt == true)
                            CreateNewUser(email, password, user.getUid().toString(), name, date, phone, add);
                    }
                },2000);

            }
        });
        tvBackScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupStep2Activity.this, SignupStep1Activity.class);
                intent.putExtra("email", email.toString());
                intent.putExtra("password", password.toString());
                startActivity(intent);
            }
        });
    }

    public boolean ValidPassword(String pass, String comfirm, String email) {
        String reg = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+)$";
        if (!email.matches(reg)) {
            Toast.makeText(getApplicationContext(), "Sai định dạng email!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.length() < 6) {
            Toast.makeText(getApplicationContext(), "Mật khẩu quá ngắn (ít nhất 6 kí tự)!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pass.equals(comfirm)) {
            Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void CreateNewUser(String email, String password, String idUser, String fullname, String birthday, String phonenumber, String address) {
        // Instantiate the RequestQueue.
        String[] replacebirthday = birthday.split("/");
        String chuoiNamSinh = replacebirthday[2] + "-" + replacebirthday[1] + "-" + replacebirthday[0];
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://"+IP_HOST+":3000/Users?id_user=" + idUser + "&name=" + fullname
                + "&birthday=" + chuoiNamSinh + "&phonenumber=" + phonenumber + "&address=" + address;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getString("message").equals("Insert success!")) {
                                Toast.makeText(SignupStep2Activity.this, "Thêm người dùng thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupStep2Activity.this, SignupStep3Activity.class);
                                intent.putExtra("email", email.toString());
                                intent.putExtra("password", password.toString());
                                intent.putExtra("fullname", edtFullNameSignUp.getText().toString());
                                intent.putExtra("birthday", edtBirthdaySignUp.getText().toString());
                                intent.putExtra("phonenumber", edtPhoneNumberSignUp.getText().toString());
                                intent.putExtra("address", edtAddressSignUp.getText().toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupStep2Activity.this, "respone has error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupStep2Activity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        Log.d("stringRequest", stringRequest.toString());
        // Add the request to the RequestQueue.

        queue.add(stringRequest);
    }

    public void checkPhoneNumber() {
        kiemtrasdt = false;
        String phonenumber = edtPhoneNumberSignUp.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(SignupStep2Activity.this);
        String url = "http://"+IP_HOST+":3000/Users/phonenumber?phonenumber=" + phonenumber;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            kiemtrasdt = true;
                        }
                        Log.d("Response:", response.toString());
                        if (response.length() > 0) {
                            Toast.makeText(SignupStep2Activity.this, "Số điện thoại này đã được sử dụng!", Toast.LENGTH_SHORT).show();
                        } else {
                            kiemtrasdt = true;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupStep2Activity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
