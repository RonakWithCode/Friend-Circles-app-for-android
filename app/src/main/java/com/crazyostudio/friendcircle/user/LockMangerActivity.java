package com.crazyostudio.friendcircle.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.friendcircle.MainActivity;
import com.crazyostudio.friendcircle.databinding.ActivityLockMangerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LockMangerActivity extends AppCompatActivity {
    ActivityLockMangerBinding binding;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockMangerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        binding.pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.pass.getText().toString().length()==4) {
                    binding.floatingActionButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.pass.getText().toString().length()==4) {
                    binding.floatingActionButton.setVisibility(View.VISIBLE);
                }

            }
        });
        binding.floatingActionButton.setOnClickListener(view->
            FirebaseDatabase.getInstance().getReference().child("pin").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnCompleteListener(task -> {
                DataSnapshot dataSnapshot = task.getResult();
                String fakePin = Objects.requireNonNull(dataSnapshot.child("fakePin").getValue()).toString();
                String realPin = Objects.requireNonNull(dataSnapshot.child("realPin").getValue()).toString();
                if (binding.pass.getText().toString().equals(realPin)) {
                    startActivity(new Intent(LockMangerActivity.this, MainActivity.class));
                    finish();
                }
                else if (binding.pass.getText().toString().equals(fakePin)){
                    startActivity(new Intent(LockMangerActivity.this,FakeActivity.class));
                }else {
                    binding.pass.setError("Password -:- ");
                }
            }));
    }
}


