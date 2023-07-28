package com.crazyostudio.friendcircle.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.adapters.ShareNotesAdapters;
import com.crazyostudio.friendcircle.adapters.StoryAdapters;
import com.crazyostudio.friendcircle.databinding.FragmentNoNetworkBinding;
import com.crazyostudio.friendcircle.databinding.FragmentShareFileBinding;
import com.crazyostudio.friendcircle.databinding.FragmentShareNotesBinding;
import com.crazyostudio.friendcircle.model.StoryModel;

import java.util.ArrayList;

public class ShareNotesFragment extends Fragment {
    FragmentShareNotesBinding binding;
    ArrayList<String> uri = new ArrayList<>();

    public ShareNotesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uri = getArguments().getStringArrayList("Notes");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShareNotesBinding.inflate(inflater,container,false);




        ShareNotesAdapters adapters;
        adapters = new ShareNotesAdapters(uri, getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.notes.setLayoutManager(layoutManager);
        binding.notes.setAdapter(adapters);
        adapters.notifyDataSetChanged();







        return binding.getRoot();
    }
}