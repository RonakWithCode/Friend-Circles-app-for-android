package com.crazyostudio.friendcircle.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.databinding.AddimagelayoutBinding;

import java.util.ArrayList;

public class ImageAdapters extends RecyclerView.Adapter<ImageAdapters.ImageAdaptersViewHolder> {
    ArrayList<String> image;
    Context context;

    public ImageAdapters(ArrayList<String> image, Context context) {
        this.image = image;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageAdapters.ImageAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageAdapters.ImageAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.addimagelayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapters.ImageAdaptersViewHolder holder, int position) {
        String imagePos = image.get(position);

        holder.binding.image.setImageURI(Uri.parse(imagePos));
        holder.binding.removeImage.setOnClickListener(r->{

        });
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    public static class ImageAdaptersViewHolder extends RecyclerView.ViewHolder {
        AddimagelayoutBinding binding;
        public ImageAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AddimagelayoutBinding.bind(itemView);
        }
    }
}
