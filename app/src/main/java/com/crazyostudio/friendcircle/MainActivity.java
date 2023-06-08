package com.crazyostudio.friendcircle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.friendcircle.Chats.GroupChat;
import com.crazyostudio.friendcircle.adapters.UserInfoAdapters;
import com.crazyostudio.friendcircle.databinding.ActivityMainBinding;
import com.crazyostudio.friendcircle.model.UserInfo;
import com.crazyostudio.friendcircle.user.AboutActivity;
import com.crazyostudio.friendcircle.user.SignUp;
import com.crazyostudio.friendcircle.user.User_Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseDatabase users;
    FirebaseAuth auth;
    UserInfoAdapters userInfoAdapters;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final String[] PERMISSIONS_BELOW_10 = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final String[] PERMISSIONS_10_AND_ABOVE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE
    };
//    ActivityResultLauncher<Intent> resultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance();
        getUser();

        String[] permissions;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissions = PERMISSIONS_BELOW_10;
        } else {
            permissions = PERMISSIONS_10_AND_ABOVE;
        }

        requestPermissions(permissions);

    }

    private void requestPermissions(String[] permissions) {
        if (!arePermissionsGranted(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
            }
        // Permissions are already granted, proceed with your logic


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//
//                try {
//                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
//                    startActivity(intent);
//                } catch (Exception ex) {
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    startActivity(intent);
//                }
//            }
//        }
    }

    private boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (areAllPermissionsGranted(grantResults)) {
                // All permissions are granted, proceed with your logic
            } else {
                // Permissions are not granted, handle the denied permissions
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private boolean areAllPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    void getUser() {
        ArrayList<UserInfo> userInfoS = new ArrayList<>();
        userInfoAdapters = new UserInfoAdapters(userInfoS, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(userInfoAdapters);
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
        else if (item.getItemId() == R.id.about) {
//            Toast.makeText(this, "Coming soon   ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        else if (item.getItemId() == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, SignUp.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}