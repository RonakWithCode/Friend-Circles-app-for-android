package com.crazyostudio.friendcircle.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.crazyostudio.friendcircle.R;
import com.crazyostudio.friendcircle.databinding.FragmentCreateStoryBinding;

import java.util.Random;

public class CreateStoryFragment extends Fragment {
    FragmentCreateStoryBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateStoryBinding.inflate(inflater,container,false);
//        binding.changeTextStyle.setOnClickListener(v->{
//            if (binding.changeTextStyle.get().equals(R.drawable.text_nomal)){
//                binding.changeTextStyle.setBackgroundResource(R.drawable.font_bold);
//                binding.text.setTypeface(Typeface.DEFAULT_BOLD);
//                Toast.makeText(getContext(), "Change it to DEFAULT_BOLD", Toast.LENGTH_SHORT).show();

//            }
//        });
        binding.changeColorButton.setOnClickListener(v -> {
            Random random = new Random();
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            binding.colorChangingImageView.changeBackgroundColor(color);
        });

        return binding.getRoot();
//            <com.makeramen.roundedimageview.RoundedImageView
//        android:id="@+id/changeTextStyle"
//        android:layout_width="44dp"
//        android:layout_height="44dp"
//        android:layout_alignParentTop="true"
//        android:layout_marginTop="14dp"
//        android:layout_marginEnd="8dp"
//        android:layout_toStartOf="@+id/changeColorButton"
//        android:adjustViewBounds="true"
//        android:contentDescription="@string/tap_to_change_colors"
//        android:scaleType="fitXY"
//        android:src="@drawable/text_nomal"
//        app:riv_border_color="#333333"
//        app:riv_oval="true" />

    }
}