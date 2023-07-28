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
import com.crazyostudio.friendcircle.databinding.AllnoteslayoutBinding;
import com.crazyostudio.friendcircle.fragmentLoad;
import com.crazyostudio.friendcircle.model.SubjectModel;

import java.util.ArrayList;

public class NotesAdapters extends RecyclerView.Adapter<NotesAdapters.NotesAdaptersViewHolder> {

    ArrayList<SubjectModel> models;
    Context context;

    public NotesAdapters(ArrayList<SubjectModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesAdapters.NotesAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesAdapters.NotesAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.allnoteslayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapters.NotesAdaptersViewHolder holder, int position) {
        SubjectModel subjectModel = models.get(position);
        Glide.with(context).load(subjectModel.getUri()).into(holder.binding.NotesIcon);
        holder.binding.notesName.setText(subjectModel.getSubName());
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, fragmentLoad.class);
            intent.putExtra("LoadID","shareList");
            intent.putExtra("Notes",subjectModel.getNotes());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class NotesAdaptersViewHolder extends RecyclerView.ViewHolder {
        AllnoteslayoutBinding binding;
        public NotesAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AllnoteslayoutBinding.bind(itemView);
        }
    }
}
