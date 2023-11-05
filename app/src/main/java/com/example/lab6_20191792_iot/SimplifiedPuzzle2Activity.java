package com.example.lab6_20191792_iot;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.lab6_20191792_iot.databinding.ActivitySimplifiedPuzzle2Binding;

import java.util.ArrayList;
import java.util.List;

public class SimplifiedPuzzle2Activity extends AppCompatActivity {

    ActivitySimplifiedPuzzle2Binding binding;
    GridLayout gridLayout;
    List<Bitmap> puzzlePieces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySimplifiedPuzzle2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gridLayout = findViewById(R.id.gridLayout);

        Intent intent = getIntent();
        String selectedImageUriString = intent.getStringExtra("selectedImageUri");


        //ImageView imageView = findViewById(R.id.gridLayout);
        //imageView.setImageURI(Uri.parse(selectedImageUriString));

        // Split the image into 3x3 pieces
        splitImageIntoPieces(selectedImageUriString);

        // Create buttons for each puzzle piece and set their backgrounds
        for (int i = 0; i < 9; i++) {
            Button button = new Button(this);
            button.setLayoutParams(new GridLayout.LayoutParams());
            button.setBackground(new BitmapDrawable(getResources(), puzzlePieces.get(i)));
            gridLayout.addView(button);
        }

        binding.pantallaprincipal.setOnClickListener(view -> {
            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
        });
    }

    private void splitImageIntoPieces(String selectedImageUriString) {
        Uri selectedImageUri = Uri.parse(selectedImageUriString);
        Bitmap originalImage = decodeUri(selectedImageUri);
        int pieceWidth = originalImage.getWidth() / 3;
        int pieceHeight = originalImage.getHeight() / 3;
        puzzlePieces = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = j * pieceWidth;
                int y = i * pieceHeight;
                Bitmap piece = Bitmap.createBitmap(originalImage, x, y, pieceWidth, pieceHeight);
                puzzlePieces.add(piece);
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            final int REQUIRED_SIZE = 100; // Tamaño deseado para la imagen
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;

            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
