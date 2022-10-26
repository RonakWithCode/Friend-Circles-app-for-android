package com.crazyostudio.friendcircle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.databinding.ReceiverBinding;
import com.crazyostudio.friendcircle.databinding.ReceiverImageBinding;
import com.crazyostudio.friendcircle.databinding.SanderImageBinding;
import com.crazyostudio.friendcircle.databinding.SenderBinding;
import com.crazyostudio.friendcircle.model.Chat_Model;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapters extends  RecyclerView.Adapter{
    ArrayList<Chat_Model> ChatModels;
    Context context;
    int SANDER_VIEW_TYPE=1,IMAGE_SANDER_VIEW_TYPE=11;
    int RECEIVER_VIEW_TYPE=2,IMAGE_RECEIVER_VIEW_TYPE=22;

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
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (ChatModels.get(position).getID().equals(FirebaseAuth.getInstance().getUid())) {
            if (ChatModels.get(position).isImage()) {
                return IMAGE_SANDER_VIEW_TYPE;
            }else {
                return SANDER_VIEW_TYPE;
            }
        }

        else {
            if (ChatModels.get(position).isImage()) {
                return IMAGE_RECEIVER_VIEW_TYPE;
            }else {
                return RECEIVER_VIEW_TYPE;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Chat_Model chatModel = ChatModels.get(position);
        if (holder.getClass()==SanderViewHolder.class){
            ((SanderViewHolder)holder).SendBinding.InputText.setText(chatModel.getMessage());
        }
        else if (holder.getClass()==ImageSanderViewHolder.class)
        {
            Glide.with(context).load(chatModel.getMessage()).into(((ImageSanderViewHolder)holder).SendBinding.SanderImageview);

//            ((ImageSanderViewHolder)holder).SendBinding.SanderImageview.setImageURI(chatModel.getMessage());
        }
        else if (holder.getClass()==ImageReceiverViewHolder.class)
        {
            Glide.with(context).load(chatModel.getMessage()).into(((ImageReceiverViewHolder)holder).Binding.Imageview);

//            ((ImageSanderViewHolder)holder).SendBinding.SanderImageview.setImageURI(chatModel.getMessage());
        }

        else {
            ((ReceiverViewHolder)holder).binding.mas.setText(chatModel.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return ChatModels.size();
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceiverBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReceiverBinding.bind(itemView);
        }
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
    public static class ImageReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceiverImageBinding Binding;
        public ImageReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            Binding = ReceiverImageBinding.bind(itemView);
        }
    }
}