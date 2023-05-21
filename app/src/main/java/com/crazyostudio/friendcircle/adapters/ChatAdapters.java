package com.crazyostudio.friendcircle.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.databinding.GroupchatreceiverBinding;
import com.crazyostudio.friendcircle.databinding.GroupimagereceiverBinding;
import com.crazyostudio.friendcircle.databinding.ReceiverBinding;
import com.crazyostudio.friendcircle.databinding.ReceiverImageBinding;
import com.crazyostudio.friendcircle.databinding.SanderImageBinding;
import com.crazyostudio.friendcircle.databinding.SenderBinding;
import com.crazyostudio.friendcircle.model.Chat_Model;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapters extends  RecyclerView.Adapter{
    ArrayList<Chat_Model> ChatModels;
    Context context;
    int SANDER_VIEW_TYPE=1,IMAGE_SANDER_VIEW_TYPE=11;
    int GROUP_IMAGE_RECEIVER_VIEW_TYPE=25,GROUP_RECEIVER_VIEW_TYPE =24,RECEIVER_VIEW_TYPE=2,IMAGE_RECEIVER_VIEW_TYPE=22;

    public ChatAdapters(ArrayList<Chat_Model> chatModels, Context context) {
        ChatModels = chatModels;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==SANDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
            return new SanderViewHolder(view);
        }
        else if (viewType==IMAGE_SANDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sander_image,parent,false);
            return new ImageSanderViewHolder(view);
        }
        else if (viewType==IMAGE_RECEIVER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_image,parent,false);
            return new ImageReceiverViewHolder(view);
        }
        else if (viewType==GROUP_IMAGE_RECEIVER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.groupimagereceiver,parent,false);
            return new ImageGroupReceiverViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        SANDER
            if (ChatModels.get(position).getID().equals(FirebaseAuth.getInstance().getUid())) {
            if (ChatModels.get(position).isImage()) {
                return IMAGE_SANDER_VIEW_TYPE;

            }
        else {
                return SANDER_VIEW_TYPE;
            }
        }

        else {
            if (ChatModels.get(position).isImage()) {
                if (ChatModels.get(position).isGroup()){
                    return GROUP_IMAGE_RECEIVER_VIEW_TYPE;
                }
                else {
                    return IMAGE_RECEIVER_VIEW_TYPE;
                }
            }
            else if (ChatModels.get(position).isGroup()){
//                if (ChatModels.get(position).isImage()){
//                    return GROUP_IMAGE_RECEIVER_VIEW_TYPE;
//                }
//                else {
                    return GROUP_RECEIVER_VIEW_TYPE;
//                }
            }
            else {
                return RECEIVER_VIEW_TYPE;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat_Model chatModel = ChatModels.get(position);
        if (holder.getClass()==SanderViewHolder.class){
            ((SanderViewHolder)holder).SendBinding.messageText.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);

            ((SanderViewHolder)holder).SendBinding.messageTime.setText(time);

        }
        else if (holder.getClass()==ImageSanderViewHolder.class)
        {
            Glide.with(context).load(chatModel.getMessage()).into(((ImageSanderViewHolder)holder).SendBinding.SanderImageview);
            ((ImageSanderViewHolder) holder).SendBinding.SanderImageview.setOnClickListener(view -> {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(chatModel.getMessage())));

            });

        }
        else if (holder.getClass()==ImageGroupReceiverViewHolder.class)
        {
            ((ImageGroupReceiverViewHolder) holder).GroupBinding.SanderName.setText(chatModel.getSanderName());
            Glide.with(context).load(chatModel.getMessage()).into(((ImageGroupReceiverViewHolder)holder).GroupBinding.RImageview);
            ((ImageGroupReceiverViewHolder) holder).GroupBinding.RImageview.setOnClickListener(view -> {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(chatModel.getMessage())));

            });
            
            
        }

        else if (holder.getClass()==ImageReceiverViewHolder.class)
        {
            Glide.with(context).load(chatModel.getMessage()).into(((ImageReceiverViewHolder)holder).Binding.Imageview);
            ((ImageReceiverViewHolder) holder).Binding.Imageview.setOnClickListener(view -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(chatModel.getMessage()))));


        }
        else if (holder.getClass()==GroupReceiverViewHolder.class)
        {
            ((GroupReceiverViewHolder)holder).GroupBinding.SanderName.setText(chatModel.getSanderName());
            ((GroupReceiverViewHolder)holder).GroupBinding.mas.setText(chatModel.getMessage());
        }
        else {

            ((ReceiverViewHolder)holder).binding.messageText.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);

            ((ReceiverViewHolder)holder).binding.messageTime.setText(time);
        }

    }

    @Override
    public int getItemCount() {
        return ChatModels.size();
    }

    public static class SanderViewHolder extends RecyclerView.ViewHolder {
        SenderBinding SendBinding;
        public SanderViewHolder(@NonNull View itemView) {
            super(itemView);
            SendBinding = SenderBinding.bind(itemView);
        }
    }
    public static class ImageSanderViewHolder extends RecyclerView.ViewHolder {
        SanderImageBinding SendBinding;
        public ImageSanderViewHolder(@NonNull View itemView) {
            super(itemView);
            SendBinding = SanderImageBinding.bind(itemView);
        }
    }
    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceiverBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReceiverBinding.bind(itemView);
        }
    }
    public static class ImageReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceiverImageBinding Binding;
        public ImageReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            Binding = ReceiverImageBinding.bind(itemView);
        }
    }
    public static class GroupReceiverViewHolder extends RecyclerView.ViewHolder {
        GroupchatreceiverBinding GroupBinding;

        public GroupReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            GroupBinding = GroupchatreceiverBinding.bind(itemView);
        }
    }
    public static class ImageGroupReceiverViewHolder extends RecyclerView.ViewHolder {
            GroupimagereceiverBinding GroupBinding;

            public ImageGroupReceiverViewHolder(@NonNull View itemView) {
                super(itemView);
                GroupBinding = GroupimagereceiverBinding.bind(itemView);
            }
    }
}