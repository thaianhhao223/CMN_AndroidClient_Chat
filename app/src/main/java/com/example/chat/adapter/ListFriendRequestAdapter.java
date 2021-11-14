package com.example.chat.adapter;

import android.app.Activity;
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
import com.example.chat.activity.ConversationPersonalActivity;
import com.example.chat.activity.ListRequestAddFrActivity;
import com.example.chat.activity.PersonActivity;
import com.example.chat.activity.ProfileRequestAddFriendActivity;
import com.example.chat.entity.User;
import com.example.chat.handler.IPCONFIG;
import com.example.chat.listener.ItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListFriendRequestAdapter extends RecyclerView.Adapter<ListFriendRequestAdapter.UserViewHolder> {
    private final String IP_HOST = IPCONFIG.getIp_config();

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<User> listUser = new ArrayList<>();

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    public ListFriendRequestAdapter(Context context, ArrayList<User> listUser) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.item_request_friends,parent,false);
        return new UserViewHolder(mItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User mCurrent = listUser.get(position);
        String str;
        str = mCurrent.getName();
        holder.name.setText(str);
        String image;
        image = mCurrent.getUrl_avatar();
        Glide.with(context).load(image).into(holder.image);

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriend(user.getUid(),mCurrent.getId_user(),position);
            }
        });
        holder.btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DenyRequestFriend(user.getUid(),mCurrent.getId_user(),position);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProfileRequestAddFriendActivity.class);
                intent.putExtra("id_user",mCurrent.getId_user());
                context.startActivity(intent);
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProfileRequestAddFriendActivity.class);
                intent.putExtra("id_user",mCurrent.getId_user());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }
    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        public TextView name;
        public ImageView btnAccept,btnDeny;
        public CricleImage image;
        ListFriendRequestAdapter adapter;
        private ItemClickListener itemClickListener;

        public UserViewHolder(@NonNull View itemView, ListFriendRequestAdapter adapter) {
            super(itemView);
            name = itemView.findViewById(R.id.tvItemListRequestName);
            btnAccept = itemView.findViewById(R.id.imgViewItemListRequestAccept);
            btnDeny = itemView.findViewById(R.id.imgViewItemListRequestDeny);
            image = itemView.findViewById(R.id.imgItemListRequestAva);

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
    public void AddFriend(String user,String id_user_request,int position){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://"+IP_HOST+":3000/Friend?id_user=" + user+"&id_user_request="+id_user_request;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        try {
                            if(response.length() > 0){
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(context, "Thêm bạn thành công", Toast.LENGTH_SHORT).show();
                                CreateNewChatRoom(user,id_user_request);
                                listUser.remove(position);
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(context, "Thêm bạn thất bại", Toast.LENGTH_SHORT).show();
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
    public void CreateNewChatRoom(String user,String id_user_request){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://"+IP_HOST+":3000/Chatroom?id_user=" + user+"&id_user_request="+id_user_request;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        try {
                            if(response.length() > 0){
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(context, "Thêm phòng chat mới thành công", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Thêm phòng chat mới thất bại", Toast.LENGTH_SHORT).show();
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
    public void DenyRequestFriend(String user,String id_user_request,int position){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://"+IP_HOST+":3000/Friendrequest?id_user="+id_user_request+"&id_user_request="+user;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object.getString("message").equals("Delete success!")){
                                Toast.makeText(context, "Xóa lời mời kết bạn thành công!", Toast.LENGTH_SHORT).show();
                                listUser.remove(position);
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(context, "Xóa lời mời kết bạn thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Erro:", error.toString());
            }
        });
        Log.d("String request:", stringRequest.toString());
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
