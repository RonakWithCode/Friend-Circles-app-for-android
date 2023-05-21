package com.crazyostudio.friendcircle.Chats;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.crazyostudio.friendcircle.adapters.ChatAdapters;
import com.crazyostudio.friendcircle.databinding.ActivityChatBinding;
import com.crazyostudio.friendcircle.model.Chat_Model;
import com.crazyostudio.friendcircle.user.SeeUserProfile;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class chat extends AppCompatActivity {
    ActivityChatBinding binding;
    private StorageReference reference;
    String UserName,UserImage,UserId,SandId,UserBio;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    ChatAdapters chatAdapters;
    String sanderRoom,recRoom;

    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final String TAG = chat.class.getSimpleName();

//    private Button recordButton;
    private MediaRecorder mediaRecorder;
    private String outputFile;



    @SuppressLint("NotifyDataSetChanged")



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        reference = FirebaseStorage.getInstance().getReference("ChatImage");
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

        ArrayList<Chat_Model> ChatModels = new ArrayList<>();
        chatAdapters = new ChatAdapters(ChatModels,this);
        binding.recyclerView2.setAdapter(chatAdapters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView2.setLayoutManager(layoutManager);

        sanderRoom = SandId + UserId;
        recRoom = UserId + SandId;

        firebaseDatabase.getReference().child("chats").child(sanderRoom).addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatModels.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    Chat_Model _ChatModel = snapshot1.getValue(Chat_Model.class);
//                    if (!_ChatModel.isRead()) {
////                        firebaseDatabase.getReference().child("chats").child(sanderRoom).child(snapshot1.getKey()).child("isRead").setValue(true);
//                        firebaseDatabase.getReference().child("chats").child(recRoom).child(snapshot1.getKey()).child("isRead").setValue(true);
//                    }
                    ChatModels.add(_ChatModel);
                }
                chatAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.imageBts.setOnClickListener(view ->
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(800, 800)
                        .start(1));



//        binding.SandBts.setOnClickListener(view -> {
//            if (!binding.InputText.getText().toString().isEmpty()) {
//                final Chat_Model Chat =  new Chat_Model(SandId,binding.InputText.getText().toString(),System.currentTimeMillis(),false);
//                binding.InputText.setText("");
//                firebaseDatabase.getReference().child("chats").child(sanderRoom).push().setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).push().setValue(Chat).addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));
//            }
//            else {
//                Toast.makeText(this, "Enter your text", Toast.LENGTH_SHORT).show();
//            }
//        });
        binding.SandBts.setOnClickListener(view -> {
            if (isRecording()) {
                stopRecording();
            } else {
                if (checkPermission()) {
                    startRecording();
                } else {
                    requestPermission();
                }
            }
        });
    }
    private String filletExtension(Uri Uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(Uri));
    }


    private boolean checkPermission() {
        int result = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(chat.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        outputFile = Environment.getDownloadCacheDirectory().getAbsolutePath() + "/recording.3gp";

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
//            recordButton.setText("Stop Recording");
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "startRecording: " + e.getMessage());
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
//            recordButton.setText("Start Recording");
            Toast.makeText(this, "Recording stopped. File saved to " + outputFile, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isRecording() {
        return mediaRecorder != null;
    }





    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (data.getData() != null && requestCode==1) {
            final Uri dataUri = data.getData();
//            binding.userImage.setImageURI(dataUri);
            StorageReference file = reference.child(System.currentTimeMillis()+"."+ filletExtension(dataUri));
            Toast.makeText(this, "Image Sand ", Toast.LENGTH_SHORT).show();
            file.putFile(dataUri).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(uri -> {
                final Chat_Model Chat =  new Chat_Model(SandId,uri.toString(),true,false);
                    firebaseDatabase.getReference().child("chats").child(sanderRoom).push().setValue(Chat)
                    .addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).push().setValue(Chat).addOnSuccessListener(unused1 ->
                                    chatAdapters.notifyDataSetChanged()
                            )
                    );

            })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

        }

        }
    }
