package com.crazyostudio.friendcircle.user;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.friendcircle.*;
import com.crazyostudio.friendcircle.MainActivity;
import com.crazyostudio.friendcircle.databinding.ActivitySignUpBinding;
import com.crazyostudio.friendcircle.model.UserInfo;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    ProgressDialog bar;
    boolean imageBts,bts = false;
    private StorageReference reference;
    FirebaseDatabase db;
    private Uri dataUri;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        reference = FirebaseStorage.getInstance().getReference("Image");
        bar = new ProgressDialog(this);

        //        binding.userImage.setOnLongClickListener(view -> false);
        binding.userImage.setOnClickListener(view ->
                ImagePicker.with(this)
                    .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(1));

        binding.Login.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this,Login.class));
            finish();
        });


        binding.Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.Name.getText().toString().isEmpty()) {
                    binding.Name.setError("Enter your name");
                    bts = false;
                }else {
                    bts = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.Mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.Mail.getText().toString().isEmpty()) {
                    binding.Mail.setError("Enter your email");
                    bts = false;
                }else {
                    bts = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.Password.getText().toString().isEmpty()) {
                    binding.Password.setError("Enter your Password");
                    bts = false;
                }else if (binding.Password.getText().toString().length()<6){
                    binding.Password.setError("Check your Password length 6 litter");
                    bts = false;
                }else {
                    bts = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.signup.setOnClickListener(view ->
        {
            if (!binding.Name.getText().toString().isEmpty()&&!binding.Mail.getText().toString().isEmpty() && !binding.Password.getText().toString().isEmpty()) {
//                UploadImage(dataUri);
                if (imageBts) {
                    if (bts&&!binding.Name.getText().toString().isEmpty()&&!binding.Mail.getText().toString().isEmpty()&&!binding.Password.getText().toString().isEmpty()) {
                        bar.setMessage("Wait Account is Creating");
                        bar.show();
                        CreateAccount();
                    }else {
                        Toast.makeText(this, "Check your input ", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(this, "Tab on image icon and select Image ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (data.getData() != null && requestCode==1) {
            dataUri = data.getData();
            binding.userImage.setImageURI(dataUri);
            imageBts = true;
        }
    }

    void CreateAccount() {
        Auth.createUserWithEmailAndPassword(binding.Mail.getText().toString(), binding.Password.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        UploadImage(dataUri);
                    }
                    else {
                        Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        if (bar.isShowing()) {
                            bar.dismiss();
                        }
                    }
                });
    }

    private String getfilleExtension(Uri Uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(Uri));
    }

    private void UploadImage(Uri image) {
        StorageReference file = reference.child(System.currentTimeMillis()+"."+getfilleExtension(image));
        file.putFile(image).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(uri -> {
            UserInfo userInfo;
            userInfo = new UserInfo(binding.Name.getText().toString(),"Using Friend Circle",uri.toString(),binding.Mail.getText().toString(),binding.Password.getText().toString());
            db.getReference().child("UserInfo").child(Objects.requireNonNull(Auth.getUid())).setValue(userInfo);
            if (bar.isShowing()) {
                 bar.dismiss();
                startActivity(new Intent(SignUp.this, MainActivity.class));
                finish();
            }
        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
}