package com.crazyostudio.friendcircle.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.databinding.FragmentNoNetworkBinding;

public class noNetworkFragment extends Fragment {
    FragmentNoNetworkBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNoNetworkBinding.inflate(inflater,container,false);


//        if (!CurrentInternetConnection.getInternetConnectionType(requireContext())){
//            requireActivity().finish();
//        }
        return binding.getRoot();
    }
}