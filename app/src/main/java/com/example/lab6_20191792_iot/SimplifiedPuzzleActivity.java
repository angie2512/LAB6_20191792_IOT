package com.example.lab6_20191792_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab6_20191792_iot.databinding.ActivitySimplifiedPuzzle2Binding;
import com.example.lab6_20191792_iot.databinding.ActivitySimplifiedPuzzleBinding;

public class SimplifiedPuzzleActivity extends AppCompatActivity {

    ActivitySimplifiedPuzzleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySimplifiedPuzzleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.comenzarpuzzle.setOnClickListener(view -> {
            Intent intent = new Intent(this, SimplifiedPuzzle2Activity.class);
            startActivity(intent);
        });
    }
}