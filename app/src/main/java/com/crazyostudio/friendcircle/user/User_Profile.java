package com.crazyostudio.friendcircle.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.crazyostudio.friendcircle.databinding.ActivityUserProfileBinding;
import com.crazyostudio.friendcircle.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class User_Profile extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    FirebaseDatabase database;
    ProgressDialog bar;
    UserInfo _userInfo;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        bar = new ProgressDialog(this);

//        binding.Name.setText(database.getReference("UserInfo").child(auth.getUid()));
//        ArrayList<>

        database.getReference("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (Objects.equals(snapshot1.getKey(), auth.getUid())) {
                        _userInfo = snapshot1.getValue(UserInfo.class);
                        assert _userInfo != null;
                        binding.Name.setText(_userInfo.getName());
                        binding.bio.setText(_userInfo.getBio());
                        binding.Mail.setText(_userInfo.getMail());

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.Name.getText().toString().isEmpty()) {
                    binding.Name.setError("Enter you Name ");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.bio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.bio.getText().toString().isEmpty()) {
                    binding.bio.setError("Enter you bio ");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.next.setOnClickListener(view -> {
            bar.setMessage("Change in Database");
            bar.show();

            if (!binding.Name.getText().toString().isEmpty()&&!binding.bio.getText().toString().isEmpty())
            {
                database.getReference("UserInfo").child(Objects.requireNonNull(auth.getUid())).child("bio").setValue(binding.bio.getText().toString());
                database.getReference("UserInfo").child(auth.getUid()).child("name").setValue(binding.Name.getText().toString());
                if (bar.isShowing()) {bar.dismiss();}
            }
            else {
                if (bar.isShowing()) {bar.dismiss();}
                Toast.makeText(this, "Check your input", Toast.LENGTH_SHORT).show();
            }
        });
    }
}