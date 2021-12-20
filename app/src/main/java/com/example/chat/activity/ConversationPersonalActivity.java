package com.example.chat.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chat.R;
import com.example.chat.adapter.ConversationAdapter;
import com.example.chat.entity.Message;
import com.example.chat.handler.IPCONFIG;
import com.example.chat.service.FileUploadService;
import com.example.chat.service.OttkpClient;
import com.example.chat.socket.SocketClient;
import com.example.chat.ulti.MultipartUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConversationPersonalActivity extends AppCompatActivity {
    private final String IP_HOST = IPCONFIG.getIp_config();

    private ImageView imgViewConversationPersonalBack ,imgViewConversationPersonalSend,imgConversationChooseImage;
    private TextView tvNameReceive;
    private EditText edtSendMessage;
    private ArrayList<Message> listMessage = new ArrayList<>();
    private String id_chatroom;

    private RecyclerView recyclerView;
    private ConversationAdapter conversationAdapter;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String id_user,id_friend;

    private SocketClient mSocket;
    private Socket socket;

    private final static int SELECT_PHOTO = 12345;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_self);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        imgConversationChooseImage = findViewById(R.id.imgConversationChooseImage);
        imgViewConversationPersonalSend = findViewById(R.id.imgViewConversationPersonalSend);
        imgViewConversationPersonalBack = findViewById(R.id.imgViewConversationPersonalBack);
        tvNameReceive = findViewById(R.id.tvNameReceive);
        imgViewConversationPersonalSend = findViewById(R.id.edtSendMessage);
        edtSendMessage = findViewById(R.id.edtMess);
        recyclerView = findViewById(R.id.rcvConversationPersonal);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id_user = user.getUid();
        Intent intent = getIntent();
        tvNameReceive.setText(intent.getStringExtra("name"));
        id_friend = intent.getStringExtra("id_user");

        Log.d("id_user",id_user);
        id_chatroom = intent.getStringExtra("id_chatroom");
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
                socket.connect();
                socket.emit("joinroom",socket.id(),id_chatroom);
                conversationAdapter = new ConversationAdapter(ConversationPersonalActivity.this, listMessage,id_user);
                recyclerView.setAdapter(conversationAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ConversationPersonalActivity.this));
                if(listMessage != null)
                    recyclerView.scrollToPosition(listMessage.size()-1);
            }
        },1500);
        socket = mSocket.getmSocket();
        Log.d("SocketID:",socket.id());
        socket.on("recive_message_"+id_chatroom, onNewMessage);
        imgViewConversationPersonalSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSendMessage.getText().toString().length() > 0){
                    StoredATextMessage("text",edtSendMessage.getText().toString());
                    Message message = new Message();
                    message.setId_send(user.getUid());
                    message.setType("text");
                    message.setMessage(edtSendMessage.getText().toString());
                    //UpdateChatroom(message);
                    socket.emit("user_send_text",edtSendMessage.getText().toString(),id_chatroom,id_user);
                    edtSendMessage.setText("");
                }
                else
                    Toast.makeText(ConversationPersonalActivity.this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
            }
        });

        imgConversationChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            Log.d("file path", imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            File file = new File(imagePath);
            Uri uri = data.getData();
            StoredAFile(file,uri);
            //imageView.setImageBitmap(bitmap);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            // Do something with the bitmap
//            byte[] imageInByte = stream.toByteArray();
//            Log.d("Image",imageInByte.toString());
//            String imageEncoded = Base64.encodeToString(imageInByte, Base64.DEFAULT);
//            StoredAImageMessage(bitmap);

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }
    public void GetMessageStored(String room){
        RequestQueue queue = Volley.newRequestQueue(ConversationPersonalActivity.this);
        String url = "http://"+IP_HOST+":3000/MessageStored?id_chatroom=" + room;
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
    public void StoredATextMessage(String type, String message){
//        Message messagetam = new Message();
//        messagetam.setId_send(id_user);
//        messagetam.setMessage(message);
//        messagetam.setType(type);
//        listMessage.add(messagetam);
//        conversationAdapter.notifyItemInserted(listMessage.size()-1);
//        recyclerView.scrollToPosition(listMessage.size()-1);

        String fileUrl = "";
        if(!type.equalsIgnoreCase("text") ){
            fileUrl = message;
            String[] tenfile = message.split("/");
            message = tenfile[tenfile.length-1];
        }
        long currentmili = System.currentTimeMillis();
        RequestQueue queue = Volley.newRequestQueue(ConversationPersonalActivity.this);
        String url = "http://"+IP_HOST+":3000/MessageStored?id_chatroom="+id_chatroom+"&id_send="+id_user+
                "&message="+message+"&type="+type+"&sendAt="+currentmili+"&fileUrl="+fileUrl; //
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            if(response.length() > 0){
                                Log.d("Server",response.toString());
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
    public void StoredAFile(File file,Uri uri){
        OttkpClient ottkpClient = new OttkpClient();

        try {
            String response = ottkpClient.storgeAFile(file);
            JSONObject tam = new JSONObject(response);
            String url = tam.getString("url");
            StoredATextMessage("image",url);
            Log.d("url file:", url);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    public void UpdateChatroom(Message message){
        RequestQueue queue = Volley.newRequestQueue(ConversationPersonalActivity.this);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://"+IP_HOST+":3000/MessageStored?id_chatroom="+id_chatroom+"&id_user_lastsend="+id_user+
                "&message_newest="+message.getMessage()+"&datetime_newest="+currentDate;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.length() > 0){
                            Log.d("message","Cập nhật chatroom thành công");
                        }else{
                            Log.d("message","Cập nhật chatroom thất bại");
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
                    Message messagetam = new Message();
                    messagetam.setId_send(usersend);
                    messagetam.setMessage(message);
                    messagetam.setType("text");
                    listMessage.add(messagetam);
                    conversationAdapter.notifyItemInserted(listMessage.size()-1);
                    recyclerView.scrollToPosition(listMessage.size()-1);
                }
            });
        }
    };

}
