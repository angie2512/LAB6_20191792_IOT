package com.example.lab6_20191792_iot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab6_20191792_iot.databinding.ActivityMainBinding;
import com.example.lab6_20191792_iot.databinding.ActivityMemoryClassicBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryClassicActivity extends AppCompatActivity {

    ActivityMemoryClassicBinding binding;

    private static final int GALLERY_REQUEST_CODE = 1;

    private List<Uri> selectedImageUris = new ArrayList<>();
    private LinearLayout selectedImagesLayout;
    private TextView totalImagesTextView;
    private StorageReference storageReference;

    private int selectedImageCount = 0;

    private FirebaseFirestore db;
    private String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemoryClassicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        selectedImagesLayout = findViewById(R.id.selectedImagesLayout);
        totalImagesTextView = findViewById(R.id.textView2);
        db = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();


        binding.comenzarjuego.setOnClickListener(view -> {
            if (selectedImageCount < 2) {
                Toast.makeText(this, "Debes subir al menos 2 imágenes", Toast.LENGTH_SHORT).show();
            } else {
                subirImagenesAFirebaseStorage();
                ArrayList<Uri> selectedImageUris = new ArrayList<>(this.selectedImageUris);

                Intent intent = new Intent(this, MemoryClassic2Activity.class);
                intent.putParcelableArrayListExtra("imageUris", selectedImageUris);
                startActivity(intent);
            }
        });


        binding.agregarimagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagenDesdeGaleria();
            }
        });


    }

    private void seleccionarImagenDesdeGaleria() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (selectedImageCount < 15) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    selectedImageUris.add(imageUri);
                    mostrarImagenSeleccionada(imageUri);
                    actualizarRecuentoImagenes();
                    selectedImageCount++;
                }
            } else {
                Toast.makeText(MemoryClassicActivity.this, "Se ha superado el límite de fotos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void mostrarImagenSeleccionada(Uri imageUri) {
        RelativeLayout imageLayout = new RelativeLayout(this);

        ImageView imageView = new ImageView(this);
        imageView.setImageURI(imageUri);
        RelativeLayout.LayoutParams imageLayoutParams = new RelativeLayout.LayoutParams(400, 400);
        imageView.setLayoutParams(imageLayoutParams);

        Button deleteButton = new Button(this);
        deleteButton.setText("X");
        deleteButton.setTextSize(12);
        RelativeLayout.LayoutParams deleteButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        deleteButtonParams.addRule(RelativeLayout.ALIGN_TOP, imageView.getId());
        deleteButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        deleteButtonParams.setMargins(0, -100, 0, 0);
        deleteButton.setLayoutParams(deleteButtonParams);
        deleteButton.setLayoutParams(new RelativeLayout.LayoutParams(100, 100));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImageUris.remove(imageUri);
                selectedImagesLayout.removeView(imageLayout);
                actualizarRecuentoImagenes();
                selectedImageCount--;
                if (selectedImageCount < 15) {
                    binding.agregarimagenes.setEnabled(true);
                }
            }
        });

        imageLayout.addView(imageView);
        imageLayout.addView(deleteButton);

        selectedImagesLayout.addView(imageLayout);

        if (selectedImageCount >= 15) {
            binding.agregarimagenes.setEnabled(false);
        }
    }

    private void actualizarRecuentoImagenes() {
        int count = selectedImageUris.size();
        totalImagesTextView.setText("Total de imágenes seleccionadas: " + count);
    }

    private void subirImagenesAFirebaseStorage() {
        List<String> imageLinks = new ArrayList<>(); // Almacena los enlaces de las imágenes

        for (int i = 0; i < selectedImageUris.size(); i++) {
            Uri imageUri = selectedImageUris.get(i);
            String imagePath = "imagenespartidamc/" + "imagen" + (i + 1) + ".jpg";

            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imagePath);

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageLink = uri.toString();
                            imageLinks.add(imageLink); // Agrega el enlace al ArrayList

                            // Si se han subido todas las imágenes, guárdalas en Firestore
                            if (imageLinks.size() == selectedImageUris.size()) {
                                guardarEnlacesImagenesFirestore(imageLinks);
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Manejo de errores en caso de fallo en la subida
                    });
        }
    }




    private void guardarEnlacesImagenesFirestore(List<String> imageLinks) {
        CollectionReference imagenesRef = db.collection("imagenespartidamc"); // Nueva colección "imagenespartidamc"

        Map<String, Object> data = new HashMap<>();

        for (int i = 0; i < imageLinks.size(); i++) {
            data.put("imagen" + (i + 1), imageLinks.get(i));
        }
        data.put("usuarioId", usuarioId);

        imagenesRef.add(data)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al guardar los enlaces en Firestore
                })
                .addOnFailureListener(e -> {
                    // Manejo de errores en caso de fallo
                });
    }


}