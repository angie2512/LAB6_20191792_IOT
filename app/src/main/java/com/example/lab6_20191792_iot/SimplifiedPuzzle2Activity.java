package com.example.lab6_20191792_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab6_20191792_iot.databinding.ActivitySimplifiedPuzzle2Binding;

public class SimplifiedPuzzle2Activity extends AppCompatActivity {

    ActivitySimplifiedPuzzle2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySimplifiedPuzzle2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.pantallaprincipal.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}