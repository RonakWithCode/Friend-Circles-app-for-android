package com.crazyostudio.friendcircle.user;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.friendcircle.MainActivity;
import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.databinding.ActivitySignUpBinding;
import com.crazyostudio.friendcircle.model.UserInfo;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    ProgressDialog bar;
    boolean IsFakeDone = false;
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
            boolean fakePinBool = false;

            if (bar.isShowing()) {
                bar.dismiss();
                Dialog Pin_dialog = new Dialog(this);
                Pin_dialog.setContentView(R.layout.pindialogbox);
                Pin_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                Pin_dialog.setCancelable(false);
                Pin_dialog.show();

                EditText fakePin = Pin_dialog.findViewById(R.id.fakePin);
                EditText realPin = Pin_dialog.findViewById(R.id.real_pin);
                FloatingActionButton button = Pin_dialog.findViewById(R.id.create_Btu);
                fakePin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (fakePin.getText().toString().length()==4) {
                            IsFakeDone = true;
                            realPin.requestFocus();

//                            fakePin.getNextFocusDownId();
                        }
                        else    {
                            IsFakeDone = false;
                            fakePin.setError("Length of a number is 4");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (fakePin.getText().toString().length()==4) {
                            IsFakeDone = true;
//                            fakePin.getNextFocusDownId();
                        }
                        else {
                            IsFakeDone = false;
                            fakePin.setError("Length of a number is 4");
                        }
                    }
                });
                realPin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!(fakePin.getText().toString().length() == 4)) {
                            fakePin.requestFocus();
                        }
                        if (realPin.getText().toString().length()==4) {
                            button.setVisibility(View.VISIBLE);

                            //                            fakePin.getNextFocusDownId();
                        }
                        else    {
                            realPin.setError("Length of a number is 4");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!(fakePin.getText().toString().length() == 4)) {
                            fakePin.requestFocus();
                        }
                        if (realPin.getText().toString().length()==4) {
                            button.setVisibility(View.VISIBLE);
                            realPin.clearFocus();
//                            fakePin.getNextFocusDownId();
                        }
                        else {
                            realPin.setError("Length of a number is 4");
                        }
                    }
                });
                button.setOnClickListener(view -> {
                    Map<String, String> pins = new HashMap<>();
                    pins.put("fakePin",fakePin.getText().toString());
                    pins.put("realPin",realPin.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("pin").child(Auth.getUid()).setValue(pins).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Pin_dialog.dismiss();
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(SignUp.this, "ReTry", Toast.LENGTH_SHORT).show();
                                fakePin.setText("");
                                realPin.setText("");
                                button.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                });

            }
        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}