package com.crazyostudio.friendcircle.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.adapters.ImageAdapters;
import com.crazyostudio.friendcircle.adapters.NotesAdapters;
import com.crazyostudio.friendcircle.adapters.UserInfoAdapters;
import com.crazyostudio.friendcircle.databinding.CreatesubjectsBinding;
import com.crazyostudio.friendcircle.databinding.FragmentShareFileBinding;
import com.crazyostudio.friendcircle.model.SubjectModel;
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

// share file from a user to user

public class ShareFileFragment extends Fragment {
    FragmentShareFileBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference reference;
    ArrayList<String> subImageUri,SubNotesUri;
    ImageAdapters imageAdapters;
    CreatesubjectsBinding createsubjectsBinding;
    NotesAdapters notesAdapters;
    FirebaseDatabase users;

    Uri Data;

    public ShareFileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShareFileBinding.inflate(inflater,container,false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseStorage.getInstance().getReference("Share");
        users = FirebaseDatabase.getInstance();
        createsubjectsBinding = CreatesubjectsBinding.inflate(getLayoutInflater());
        binding.createSubject.setOnClickListener(v -> showCrateSubject());
        getNotes();

        return binding.getRoot();
    }

    private void getNotes(){
        ArrayList<SubjectModel> subjectModel = new ArrayList<>();
        notesAdapters = new NotesAdapters(subjectModel,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.subjects.setLayoutManager(layoutManager);
        binding.subjects.setAdapter(notesAdapters);
        users.getReference().child("Share").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectModel.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    SubjectModel snapshot1Value = snapshot1.getValue(SubjectModel.class);
                        subjectModel.add(snapshot1Value);
                        notesAdapters.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showCrateSubject(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(createsubjectsBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
        subImageUri = new ArrayList<>();
        imageAdapters = new ImageAdapters(subImageUri,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        createsubjectsBinding.recyclerView.setLayoutManager(layoutManager);
        createsubjectsBinding.recyclerView.setAdapter(imageAdapters);
        createsubjectsBinding.CANCEL.setOnClickListener(cancel->dialog.dismiss());
        createsubjectsBinding.save.setOnClickListener(save->{
            if (!createsubjectsBinding.subName.getText().toString().isEmpty()) {
                StorageReference file = reference.child(System.currentTimeMillis()+"."+ filletExtension(Data));
                file.putFile(Data).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(fileUri1 -> {
                    long time = System.currentTimeMillis();
                    for (int i = 0; i < subImageUri.size(); i++) {
                        uploadNotes(subImageUri.get(i));
                    }
                    SubjectModel model = new SubjectModel(createsubjectsBinding.subName.getText().toString(),fileUri1.toString(),time,SubNotesUri);
                    firebaseDatabase.getReference().child("Share").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(time+createsubjectsBinding.subName.getText().toString()).setValue(model).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            dialog.dismiss();

                        }
                    }).addOnFailureListener(e -> {
                        firebaseDatabase.getReference().child("error").child("Share_createSubject").child(System.currentTimeMillis()+"").push().setValue(e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    });
                })).addOnFailureListener(e -> {
                });

            }

            });
        createsubjectsBinding.userAvatar.setOnClickListener(v -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(159));
        createsubjectsBinding.addImage.setOnClickListener(v -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(160));
        dialog.show();
    }
    private void uploadNotes(String DataUri) {
        StorageReference file = reference.child(System.currentTimeMillis()+"."+ filletExtension(Uri.parse(DataUri)));
        file.putFile(Uri.parse(DataUri)).addOnSuccessListener(taskSnapshot -> {
            file.getDownloadUrl().addOnSuccessListener(uri -> {
                SubNotesUri.add(String.valueOf(uri));
            });

        }).addOnFailureListener(e -> {
            firebaseDatabase.getReference().child("error").child("uploadNotes").child(System.currentTimeMillis()+"").push().setValue(e.getMessage());
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
        });
    }
    private String filletExtension(Uri Uri) {
        ContentResolver cr = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(Uri));
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (data.getData() != null && requestCode==159) {
            Data = data.getData();
            createsubjectsBinding.userAvatar.setImageURI(data.getData());

        }else if (data.getData()!=null&&requestCode==160){
            subImageUri.add(data.getData().toString());
            imageAdapters.notifyDataSetChanged();
        }
    }
}
