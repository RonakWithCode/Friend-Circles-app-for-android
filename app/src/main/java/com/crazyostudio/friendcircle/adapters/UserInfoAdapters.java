package com.crazyostudio.friendcircle.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.chat;
import com.crazyostudio.friendcircle.databinding.MainlookBinding;
import com.crazyostudio.friendcircle.model.UserInfo;

import java.util.ArrayList;

public class UserInfoAdapters extends RecyclerView.Adapter<UserInfoAdapters.UserInfoAdaptersViewHolder> {

    ArrayList<UserInfo> userInfo;
    Context context;

    public UserInfoAdapters(ArrayList<UserInfo> userInfo, Context context) {
        this.userInfo = userInfo;
        this.context = context;
    }
    @NonNull
    @Override
    public UserInfoAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserInfoAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.mainlook,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserInfoAdaptersViewHolder holder, int position) {
        UserInfo product = userInfo.get(position);
        Glide.with(context).load(product.getUserImage()).into(holder.binding.UserImage);
        holder.binding.name.setText(product.getName());
//        holder.binding.lastMss.setText(product.getBio());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, chat.class);
            intent.putExtra("name",product.getName());
            intent.putExtra("Images",product.getUserImage());
            intent.putExtra("Bio",product.getBio());
            intent.putExtra("UserId",product.getUserid());
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return userInfo.size();
    }


    public static class UserInfoAdaptersViewHolder  extends RecyclerView.ViewHolder {
        MainlookBinding binding;

        public UserInfoAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MainlookBinding.bind(itemView);
        }
    }
    }

