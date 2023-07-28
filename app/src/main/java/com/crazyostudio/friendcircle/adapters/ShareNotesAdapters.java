package com.crazyostudio.friendcircle.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.databinding.ImagesharenoteslayoutBinding;
import com.crazyostudio.friendcircle.fullscreen;

import java.util.ArrayList;

public class ShareNotesAdapters extends RecyclerView.Adapter<ShareNotesAdapters.ShareNotesAdaptersViewHolder> {

    ArrayList<String> uri;
    Context context;

    public ShareNotesAdapters(ArrayList<String> uri, Context context) {
        this.uri = uri;
        this.context = context;
    }

    @NonNull
    @Override
    public ShareNotesAdapters.ShareNotesAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShareNotesAdapters.ShareNotesAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.imagesharenoteslayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShareNotesAdapters.ShareNotesAdaptersViewHolder holder, int position) {
        String imagePos = uri.get(position);
        Glide.with(context).load(imagePos).into(holder.binding.image);
        holder.binding.image.setOnLongClickListener(v -> false);
        holder.binding.image.setOnClickListener(view->{
            Intent intent = new Intent(context, fullscreen.class);
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.binding.image.getDrawable();
//            Bitmap bitmap = bitmapDrawable.getBitmap();
            intent.putExtra("image", imagePos);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return uri.size();
    }

    public static class ShareNotesAdaptersViewHolder extends RecyclerView.ViewHolder {
        ImagesharenoteslayoutBinding binding;
        public ShareNotesAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ImagesharenoteslayoutBinding.bind(itemView);
        }
    }
}
