package com.example.chat.adapter;

import android.content.Context;
import android.util.Log;
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
import com.example.chat.activity.UserProfileActivity;
import com.example.chat.entity.Message;
import com.example.chat.entity.User;
import com.example.chat.handler.IPCONFIG;
import com.example.chat.listener.ItemClickListener;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter{
    private final String IP_HOST = IPCONFIG.getIp_config();

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_Image_SENT = 3;
    private static final int VIEW_TYPE_MESSAGE_Image_RECEIVED  = 4;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Message> listMessage = new ArrayList<>();
    private String id_user,id_friend;
    public ConversationAdapter(Context context, ArrayList<Message> listMessage,String id_user) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listMessage = listMessage;
        this.id_user = id_user;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_right, parent, false);
            return new SendViewHolder(view);
        }
        if (viewType == VIEW_TYPE_MESSAGE_Image_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_img_right, parent, false);
            return new SendImageViewHolder(view);
        }
        if (viewType == VIEW_TYPE_MESSAGE_Image_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_img_left, parent, false);
            return new RecieveImageViewHolder(view);
        }
        if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_left, parent, false);
            return new RecieveViewHolder(view);
        }

        return null;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message =  listMessage.get(position);

        if (message.getId_send().equals(id_user) && !message.getType().equalsIgnoreCase("image") ) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        }
        if(message.getId_send().equals(id_user) && message.getType().equalsIgnoreCase("image")){
            return VIEW_TYPE_MESSAGE_Image_SENT;
        }
        if(!message.getId_send().equals(id_user) && message.getType().equalsIgnoreCase("image")){
            return VIEW_TYPE_MESSAGE_Image_RECEIVED;
        }
        else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = listMessage.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SendViewHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((RecieveViewHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_Image_RECEIVED:
                ((RecieveImageViewHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_Image_SENT:
                ((SendImageViewHolder) holder).bind(message);
                break;
        }
    }

    public void addNewMessage(Message message) {
        if (listMessage == null) listMessage = new ArrayList();
        listMessage.add(message);
        //notifyDataSetChanged();
        notifyItemInserted(listMessage.size()-1);
    }
    @Override
    public int getItemCount() {
        if (listMessage == null)
            return 0;
        return listMessage.size();
    }
    public class SendViewHolder extends RecyclerView.ViewHolder{
        public TextView tvItemChatRightMessage;
        public ImageView btnOption;
        public CricleImage image;
        ListFriendAdapter adapter;
        private ItemClickListener itemClickListener;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemChatRightMessage = itemView.findViewById(R.id.tvItemChatRightMessage);
            image = itemView.findViewById(R.id.imgViewConversationPersonalSend);
        }

        public void bind(Message message) {
            if(!message.getType().equalsIgnoreCase("text") ){
                String[] tenfile = message.getMessage().split("/");
                tvItemChatRightMessage.setText(tenfile[tenfile.length-1]);
            }else{
                tvItemChatRightMessage.setText(message.getMessage());
            }
        }
    }
    public class SendImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView tvItemChatRightMessage;
        public ImageView btnOption;
        public CricleImage image;
        ListFriendAdapter adapter;
        private ItemClickListener itemClickListener;

        public SendImageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemChatRightMessage = itemView.findViewById(R.id.imgSend);
//            image = itemView.findViewById(R.id.imgViewConversationPersonalSend);
        }

        public void bind(Message message) {
            Log.d("Url file",message.getFileUrl());
            Glide.with(context).load(message.getFileUrl()).into(tvItemChatRightMessage);
        }
    }
    public class RecieveViewHolder extends RecyclerView.ViewHolder{
        public TextView tvItemChatLeftMessage;
        public ImageView btnOption;
        public CricleImage image;
        ListFriendAdapter adapter;
        private ItemClickListener itemClickListener;

        public RecieveViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemChatLeftMessage = itemView.findViewById(R.id.tvItemChatLeftMessage);
            image = itemView.findViewById(R.id.imgViewConversationPersonalRecive);
        }

        public void bind(Message message) {
            if(!message.getType().equalsIgnoreCase("text")){
                String[] tenfile = message.getMessage().split("/");
                tvItemChatLeftMessage.setText(tenfile[tenfile.length-1]);
            }else{
                tvItemChatLeftMessage.setText(message.getMessage());
            }

        }
    }
    public class RecieveImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView tvItemChatRightMessage;
        public ImageView btnOption;
        public CricleImage image;
        ListFriendAdapter adapter;
        private ItemClickListener itemClickListener;

        public RecieveImageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemChatRightMessage = itemView.findViewById(R.id.imgReceive);
//            image = itemView.findViewById(R.id.imgViewConversationPersonalSend);
        }

        public void bind(Message message) {
            if(message.getFileUrl()!= null){
                Log.d("Url file",message.getFileUrl());
                Glide.with(context).load(message.getFileUrl()).into(tvItemChatRightMessage);
            }

        }
    }
}
