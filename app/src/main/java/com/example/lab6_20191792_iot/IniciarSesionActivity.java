package com.example.lab6_20191792_iot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab6_20191792_iot.databinding.ActivityIniciarSesionBinding;
import com.example.lab6_20191792_iot.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class IniciarSesionActivity extends AppCompatActivity {

    FirebaseFirestore db;

    ActivityIniciarSesionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIniciarSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        binding.btnIngresar.setOnClickListener(view -> {
            String usuarioIngresado = ((TextInputEditText) binding.inputEmail.getEditText()).getText().toString();
            String contrasenaIngresada = ((TextInputEditText) binding.inputPasswd.getEditText()).getText().toString();
            validarUsuario(usuarioIngresado,contrasenaIngresada);

        });

    }

    public void validarUsuario(String email, String password){
        db.collection("usuarios")
                .whereEqualTo("correo", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Las credenciales son válidas
                            Intent intent = new Intent(IniciarSesionActivity.this, MainActivity.class);
                            intent.putExtra("correoAlumno", email);
                            startActivity(intent);
                        } else {
                            // Las credenciales son incorrectas
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("Correo o contraseña incorrecta. Vuelva a ingresar sus datos.")
                                    .setTitle("Aviso")
                                    .setPositiveButton("Aceptar", (dialog, which) -> {
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
    }

}