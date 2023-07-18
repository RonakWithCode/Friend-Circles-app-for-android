package com.crazyostudio.friendcircle.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.StroyManger;
import com.crazyostudio.friendcircle.databinding.FragmentStoryBinding;
import com.crazyostudio.friendcircle.model.ImageUtils;

public class StoryFragment extends Fragment {
    FragmentStoryBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStoryBinding.inflate(inflater,container,false);
        binding.storyAddBtu.setOnClickListener(view->{
            startActivity(new Intent(getContext(), StroyManger.class));

        });

        return binding.getRoot();
    }
}