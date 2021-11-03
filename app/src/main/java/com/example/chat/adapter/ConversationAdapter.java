package com.example.chat.adapter;

import android.content.Context;
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
import com.example.chat.entity.Message;
import com.example.chat.entity.User;
import com.example.chat.listener.ItemClickListener;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter{
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

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
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
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

        if (message.getId_send().equals(id_user)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
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
        }
    }

    @Override
    public int getItemCount() {
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

        }

        public void bind(Message message) {
            tvItemChatRightMessage.setText(message.getMessage());
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
        }

        public void bind(Message message) {
            tvItemChatLeftMessage.setText(message.getMessage());
        }
    }
}
