package com.example.lab6_20191792_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab6_20191792_iot.databinding.ActivityLoginBinding;
import com.example.lab6_20191792_iot.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {



    ActivityMainBinding binding;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        binding.button.setOnClickListener(view -> {
            Intent intent = new Intent(this, SimplifiedPuzzleActivity.class);
            startActivity(intent);
        });
        binding.button2.setOnClickListener(view -> {
            Intent intent = new Intent(this, MemoryClassicActivity.class);
            startActivity(intent);
        });

    }
}