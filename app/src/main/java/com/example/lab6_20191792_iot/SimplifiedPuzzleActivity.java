package com.example.lab6_20191792_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.lab6_20191792_iot.databinding.ActivitySimplifiedPuzzle2Binding;
import com.example.lab6_20191792_iot.databinding.ActivitySimplifiedPuzzleBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class SimplifiedPuzzleActivity extends AppCompatActivity {

    ActivitySimplifiedPuzzleBinding binding;
    private FirebaseFirestore db;
    private ImageView imagenSeleccionada;
    private static final int SELECT_PICTURE = 1;

    private int[][] gameBoard; // Matriz para el tablero del juego
    private int emptyCellX, emptyCellY; // Posición de la casilla vacía
    private ImageView[][] imageViews; // Matriz de ImageViews para las casillas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySimplifiedPuzzleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        imagenSeleccionada = findViewById(R.id.imagenSeleccionada);

        binding.subirimagen.setOnClickListener(view -> {
            // Abre la galería para seleccionar una imagen
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), SELECT_PICTURE);
        });

        binding.comenzarpuzzle.setOnClickListener(view -> {
            Intent intent = new Intent(this, SimplifiedPuzzle2Activity.class);

            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imagenSeleccionada.setImageBitmap(bitmap);

                // Guarda la Uri de la imagen en un campo de instancia o en el Intent
                Intent intent = new Intent(this, SimplifiedPuzzle2Activity.class);
                intent.putExtra("selectedImageUri", selectedImageUri.toString());
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
