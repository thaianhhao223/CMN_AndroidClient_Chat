package com.example.chat.adapter;

import android.app.Activity;
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
import com.example.chat.activity.ListRequestAddFrActivity;
import com.example.chat.activity.PersonActivity;
import com.example.chat.entity.User;

import java.util.ArrayList;

public class ListFriendRequestAdapter extends RecyclerView.Adapter<ListFriendRequestAdapter.UserViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<User> listUser = new ArrayList<>();
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
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }
    public class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView btnAccept,btnDeny;
        public CricleImage image;
        ListFriendRequestAdapter adapter;
        public UserViewHolder(@NonNull View itemView, ListFriendRequestAdapter adapter) {
            super(itemView);
            name = itemView.findViewById(R.id.tvItemListRequestName);
            btnAccept = itemView.findViewById(R.id.imgViewItemListRequestAccept);
            btnDeny = itemView.findViewById(R.id.imgViewItemListRequestDeny);
            image = itemView.findViewById(R.id.imgItemListRequestAva);
        }
    }
}
