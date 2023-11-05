package com.example.lab6_20191792_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab6_20191792_iot.databinding.ActivityIniciarSesionBinding;
import com.example.lab6_20191792_iot.databinding.ActivityLoginBinding;

public class IniciarSesionActivity extends AppCompatActivity {

    ActivityIniciarSesionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIniciarSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnIngresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }
}