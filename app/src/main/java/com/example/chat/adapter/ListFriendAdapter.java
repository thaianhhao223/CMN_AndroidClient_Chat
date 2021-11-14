package com.example.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chat.CricleImage;
import com.example.chat.R;
import com.example.chat.activity.ContactActivity;
import com.example.chat.activity.ConversationPersonalActivity;
import com.example.chat.activity.FindPersonActivity;
import com.example.chat.activity.OptionFriendActivity;
import com.example.chat.entity.User;
import com.example.chat.handler.IPCONFIG;
import com.example.chat.listener.ItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListFriendAdapter extends RecyclerView.Adapter<ListFriendAdapter.UserViewHolder>{
    private final String IP_HOST = IPCONFIG.getIp_config();

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<User> listUser = new ArrayList<>();

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String id_chatroom;

    public ListFriendAdapter(Context context, ArrayList<User> listUser) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listUser = listUser;
    }


    @NonNull
    @Override
    public ListFriendAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.item_friend,parent,false);
        return new ListFriendAdapter.UserViewHolder(mItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFriendAdapter.UserViewHolder holder, int position) {
        User mCurrent = listUser.get(position);
        String str;
        str = mCurrent.getName();
        holder.name.setText(str);
        String image;
        image = mCurrent.getUrl_avatar();
        Glide.with(context).load(image).into(holder.image);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                GetChatRoom(user.getUid(),mCurrent.getId_user());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, ConversationPersonalActivity.class);
                        intent.putExtra("id_user",mCurrent.getId_user());
                        intent.putExtra("name",mCurrent.getName());
                        intent.putExtra("id_chatroom",id_chatroom);
                        context.startActivity(intent);
                    }
                },100);
            }
        });
        holder.btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OptionFriendActivity.class);
                intent.putExtra("id_user",mCurrent.getId_user());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }
    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView name;
        public ImageView btnOption;
        public CricleImage image;
        ListFriendAdapter adapter;
        private ItemClickListener itemClickListener;

        public UserViewHolder(@NonNull View itemView, ListFriendAdapter adapter) {
            super(itemView);
            name = itemView.findViewById(R.id.tvItemFriendName);
            btnOption = itemView.findViewById(R.id.imgItemFriendOption);
            image = itemView.findViewById(R.id.imgItemFriendAva);
            itemView.setOnClickListener(UserViewHolder.this);
            itemView.setOnLongClickListener(UserViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
    }

    public void GetChatRoom(String user,String friend){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://"+IP_HOST+":3000/ConversationPersonal?id_user=" + user+"&id_user_friend="+friend;
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
                                Toast.makeText(context, "Lấy id_roomchat thất bại", Toast.LENGTH_SHORT).show();
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
