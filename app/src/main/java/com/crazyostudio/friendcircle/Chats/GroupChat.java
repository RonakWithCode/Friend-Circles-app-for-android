package com.crazyostudio.friendcircle.Chats;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.adapters.ChatAdapters;
import com.crazyostudio.friendcircle.databinding.ActivityGroupChatBinding;
import com.crazyostudio.friendcircle.model.Chat_Model;
import com.crazyostudio.friendcircle.model.UserInfo;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class GroupChat extends AppCompatActivity {
    ActivityGroupChatBinding binding;
    UserInfo _userInfo;
    String UserName, UserImage, UserBio;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    ChatAdapters chatAdapters;
    private StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            try {
                binding.getRoot().setBackgroundColor(R.color.black);
//                binding.getRoot().setBackgroundResource(R.drawable.bglay);

            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            try {
                binding.getRoot().setBackgroundResource(R.drawable.bglayout);
            }catch (Exception ignored){

            }
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        reference = FirebaseStorage.getInstance().getReference("ChatImage");
        auth = FirebaseAuth.getInstance();

        binding.BackBts.setOnClickListener(view -> finish());

        ArrayList<Chat_Model> ChatModels = new ArrayList<>();
        chatAdapters = new ChatAdapters(ChatModels, this);
        binding.recyclerView2.setAdapter(chatAdapters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView2.setLayoutManager(layoutManager);

        firebaseDatabase.getReference().child("group").child("public").addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatModels.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    Chat_Model _ChatModel = snapshot1.getValue(Chat_Model.class);

                    ChatModels.add(_ChatModel);
                }
                chatAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseDatabase.getReference("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (Objects.equals(snapshot1.getKey(), auth.getUid())) {
                        _userInfo = snapshot1.getValue(UserInfo.class);
                        assert _userInfo != null;
                        UserName = _userInfo.getName();
                        UserBio = _userInfo.getBio();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.SandBts.setOnClickListener(view -> {
            if (!binding.InputText.getText().toString().isEmpty()) {
                final Chat_Model Chat = new Chat_Model(auth.getUid(), binding.InputText.getText().toString(), false, UserName, UserBio, UserImage, true);
                binding.InputText.setText("");
                firebaseDatabase.getReference().child("group").child("public").push().setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("re").child("group").push().setValue(Chat).addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));
            } else {
                Toast.makeText(this, "Enter your text", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageBts.setOnClickListener(view ->
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(800, 800)
                        .start(999));


    }

    private String getfilleExtension(Uri Uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(Uri));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (data.getData() != null && requestCode == 999) {
            final Uri dataUri = data.getData();
//            binding.userImage.setImageURI(dataUri);
            StorageReference file = reference.child(System.currentTimeMillis() + "." + getfilleExtension(dataUri));
            Toast.makeText(this, "Image Sand ", Toast.LENGTH_SHORT).show();
            file.putFile(dataUri).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(uri -> {
//                final Chat_Model Chat =  new Chat_Model(auth.getUid(), uri.toString(),true);
                final Chat_Model Chat = new Chat_Model(auth.getUid(), uri.toString(), true, UserName, UserBio, UserImage, true);

                firebaseDatabase.getReference().child("group").child("public").push().setValue(Chat)
                        .addOnSuccessListener(unused ->
                                chatAdapters.notifyDataSetChanged()).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }));

        }
    }
}