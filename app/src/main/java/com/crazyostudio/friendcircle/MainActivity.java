package com.crazyostudio.friendcircle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.friendcircle.Chats.GroupChat;
import com.crazyostudio.friendcircle.adapters.UserInfoAdapters;
import com.crazyostudio.friendcircle.databinding.ActivityMainBinding;
import com.crazyostudio.friendcircle.model.UserInfo;
import com.crazyostudio.friendcircle.user.Login;
import com.crazyostudio.friendcircle.user.User_Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    ActivityMainBinding binding;
    FirebaseDatabase users;
    FirebaseAuth auth;
    UserInfoAdapters userInfoAdapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance();
        getUser();

    }


    void getUser() {
        ArrayList<UserInfo> userInfoS = new ArrayList<>();
        userInfoAdapters = new UserInfoAdapters(userInfoS, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(userInfoAdapters);
        firestore = FirebaseFirestore.getInstance();
        users.getReference().child("UserInfo").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfoS.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserInfo userInfo = snapshot1.getValue(UserInfo.class);
                    if (!Objects.equals(snapshot1.getKey(), auth.getUid())) {
                        assert userInfo != null;
                        userInfo.setUserid(snapshot1.getKey());
                        userInfoS.add(userInfo);

                    }
                    userInfoAdapters.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainitemmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Profile) {
            startActivity(new Intent(MainActivity.this, User_Profile.class));

        }
        else if (item.getItemId() == R.id.group_chats) {
//            Toast.makeText(this, "Coming soon   ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, GroupChat.class));
        }
        else if (item.getItemId() == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}