package com.crazyostudio.friendcircle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.crazyostudio.friendcircle.fragment.FullScreenFragment;
import com.crazyostudio.friendcircle.fragment.SelfFullScreenFragment;
import com.crazyostudio.friendcircle.fragment.StoryFragment;
import com.crazyostudio.friendcircle.model.noNetworkFragment;

public class fragmentLoad extends AppCompatActivity {
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_load);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Toast.makeText(this, "ha", Toast.LENGTH_SHORT).show();
        if (!getIntent().getStringExtra("LoadID").isEmpty()) {
             id = getIntent().getStringExtra("LoadID");
        }else {
            finish();
        }
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String Type = intent.getStringExtra("Type");
        String Id = intent.getStringExtra("Id");
        String userDP = intent.getStringExtra("userDP");
        String color = intent.getStringExtra("color");
        String text = intent.getStringExtra("text");
        String time = intent.getStringExtra("time");
        Bundle data = new Bundle();

        switch (id){
            case "network":
                // Load the fragment in the container
                fragmentTransaction.replace(R.id.loader, new noNetworkFragment());
                fragmentTransaction.commit();
                break;
            case "StoryLoaderAll":
                data.putString("userName", userName);
                data.putString("Type", Type);
                data.putString("Id", Id);
                data.putString("userDP", userDP);
                data.putString("color", color);
                data.putString("text", text);
                data.putString("time", time);
                Fragment f = new FullScreenFragment();
                f.setArguments(data);
                fragmentTransaction.replace(R.id.loader,f);
                fragmentTransaction.commit();
                break;
            case "StoryLoaderSelf":
                data.putString("userName", userName);
                data.putString("Type", Type);
                data.putString("Id", Id);
                data.putString("userDP", userDP);
                data.putString("color", color);
                data.putString("text", text);
                data.putString("time", time);
                Fragment fullScreenFragment = new SelfFullScreenFragment();
                fullScreenFragment.setArguments(data);
                fragmentTransaction.replace(R.id.loader,fullScreenFragment);
                fragmentTransaction.commit();
                break;
        }
    }
}