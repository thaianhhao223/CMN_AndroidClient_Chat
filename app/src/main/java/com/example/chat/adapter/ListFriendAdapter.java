package com.example.chat.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.chat.activity.ContactActivity;
import com.example.chat.activity.ConversationPersonalActivity;
import com.example.chat.activity.FindPersonActivity;
import com.example.chat.entity.User;
import com.example.chat.listener.ItemClickListener;

import java.util.ArrayList;

public class ListFriendAdapter extends RecyclerView.Adapter<ListFriendAdapter.UserViewHolder>{
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<User> listUser = new ArrayList<>();


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
                Intent intent = new Intent(context, ConversationPersonalActivity.class);
                intent.putExtra("id_user",mCurrent.getId_user());
                intent.putExtra("name",mCurrent.getName());
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
}
