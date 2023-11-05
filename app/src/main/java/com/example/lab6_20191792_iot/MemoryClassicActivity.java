package com.example.lab6_20191792_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab6_20191792_iot.databinding.ActivityMainBinding;
import com.example.lab6_20191792_iot.databinding.ActivityMemoryClassicBinding;

public class MemoryClassicActivity extends AppCompatActivity {

    ActivityMemoryClassicBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemoryClassicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.comenzarjuego.setOnClickListener(view -> {
            Intent intent = new Intent(this, MemoryClassic2Activity.class);
            startActivity(intent);
        });

    }
}