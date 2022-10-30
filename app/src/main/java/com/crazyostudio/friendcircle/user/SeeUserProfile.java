package com.crazyostudio.friendcircle.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.crazyostudio.friendcircle.databinding.ActivitySeeUserPofileBinding;

public class SeeUserProfile extends AppCompatActivity {
    ActivitySeeUserPofileBinding binding;
    String UserName,UserImage,UserBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeeUserPofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UserName = getIntent().getStringExtra("name");
        UserImage = getIntent().getStringExtra("Images");
        UserBio = getIntent().getStringExtra("Bio");

        binding.UserName.setText(UserName);
        binding.bio.setText(UserBio);
        Glide.with(this).load(UserImage).into(binding.userImage);

    }
}