package com.crazyostudio.friendcircle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.crazyostudio.friendcircle.adapters.ChatAdapters;
import com.crazyostudio.friendcircle.databinding.ActivityChatBinding;
import com.crazyostudio.friendcircle.model.Chat_Model;
import com.crazyostudio.friendcircle.user.SeeUserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class chat extends AppCompatActivity {

    ActivityChatBinding binding;
    String UserName,UserImage,UserId,SandId,UserBio;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        auth = FirebaseAuth.getInstance();
        UserName = getIntent().getStringExtra("name");
        UserImage = getIntent().getStringExtra("Images");
        UserBio = getIntent().getStringExtra("Bio");
        SandId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        UserId = getIntent().getStringExtra("UserId");

        binding.toolbar2.setOnClickListener(view -> {
            Intent intent = new Intent(this, SeeUserProfile.class);
            intent.putExtra("name",UserName);
            intent.putExtra("Images",UserImage);
            intent.putExtra("Bio",UserBio);
            startActivity(intent);
        });


        binding.username.setText(UserName);
        Glide.with(this).load(UserImage).into(binding.userImage);
        binding.BackBts.setOnClickListener(view -> finish());
        final ArrayList<Chat_Model> ChatModels = new ArrayList<>();
        final ChatAdapters chatAdapters = new ChatAdapters(ChatModels,this);
        binding.recyclerView2.setAdapter(chatAdapters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView2.setLayoutManager(layoutManager);

        final String sanderRoom = SandId + UserId;
        final String recRoom = UserId + SandId;

        firebaseDatabase.getReference().child("chats").child(sanderRoom).addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatModels.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    Chat_Model _ChatModel = snapshot1.getValue(Chat_Model.class);

                    ChatModels.add(_ChatModel);
                }
                chatAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.SandBts.setOnClickListener(view -> {
            if (!binding.InputText.getText().toString().isEmpty()) {
                final Chat_Model Chat =  new Chat_Model(SandId,binding.InputText.getText().toString());
                binding.InputText.setText("");
                Chat.setTime(new Date().getTime());
                firebaseDatabase.getReference().child("chats").child(sanderRoom).push().setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).push().setValue(Chat).addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));
            }
            else {
                Toast.makeText(this, "Enter your text", Toast.LENGTH_SHORT).show();
            }
        });
    }
}