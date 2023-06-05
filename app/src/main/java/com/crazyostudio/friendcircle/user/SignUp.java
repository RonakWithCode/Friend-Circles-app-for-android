package com.crazyostudio.friendcircle.user;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.friendcircle.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.link.setOnClickListener(view ->
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://crazy-studio-website.web.app/PrivacyPolicy.html"));
            startActivity(browserIntent);
        });



        binding.button.setOnClickListener(view -> {
            if (binding.Number.getText().toString().length() == 10) {
                if (binding.checkBox.isChecked()) {
                    Intent intent = new Intent(this, SignUpOTP.class);
                    intent.putExtra("number", binding.Number.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignUp.this, LockMangerActivity.class));
            finish();
        }
    }
}