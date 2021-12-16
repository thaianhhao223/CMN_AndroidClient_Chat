package com.example.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat.CricleImage;
import com.example.chat.R;
import com.example.chat.activity.ConversationPersonalActivity;
import com.example.chat.entity.ChatRoom;
import com.example.chat.entity.User;
import com.example.chat.handler.IPCONFIG;
import com.example.chat.listener.ItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {
    private final String IP_HOST = IPCONFIG.getIp_config();
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ChatRoom> listChatroom = new ArrayList<>();

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String id_chatroom;

    public ChatRoomAdapter(Context context, ArrayList<ChatRoom> listChatroom) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listChatroom = listChatroom;
    }

    @NonNull
    @Override
    public ChatRoomAdapter.ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.item_list_chat,parent,false);
        return new ChatRoomAdapter.ChatRoomViewHolder(mItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomAdapter.ChatRoomViewHolder holder, int position) {
        ChatRoom mCurrent = listChatroom.get(position);
        String str;
        str = mCurrent.getName_chatroom();
        holder.name.setText(str);
        String image;
        image = mCurrent.getUrl_ava_chatroom();
        Glide.with(context).load(image).into(holder.image);
        if(mCurrent.getMessage_newest() != null)
        holder.message.setText(mCurrent.getMessage_newest());
        if(mCurrent.getDatetime_newest() != null)
        holder.datetime.setText(mCurrent.getDatetime_newest());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, ConversationPersonalActivity.class);
                        intent.putExtra("name",mCurrent.getName_chatroom());
                        intent.putExtra("id_chatroom",mCurrent.getId_chatroom());
                        context.startActivity(intent);
                    }
                },100);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listChatroom.size();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView name,message,datetime;
        public ImageView btnOption;
        public CricleImage image;
        ChatRoomAdapter adapter;
        private ItemClickListener itemClickListener;

        public ChatRoomViewHolder(@NonNull View itemView, ChatRoomAdapter adapter) {
            super(itemView);
            name = itemView.findViewById(R.id.tvItemChatRoomName);
            message = itemView.findViewById(R.id.tvItemChatRoomNewestMessage);
            datetime = itemView.findViewById(R.id.tvItemChatRoomTime);
            btnOption = itemView.findViewById(R.id.imgItemChatRoomCheck);
            image = itemView.findViewById(R.id.imgItemChatRoomAvt);

            itemView.setOnClickListener(ChatRoomAdapter.ChatRoomViewHolder.this);
            itemView.setOnLongClickListener(ChatRoomAdapter.ChatRoomViewHolder.this);
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
}
