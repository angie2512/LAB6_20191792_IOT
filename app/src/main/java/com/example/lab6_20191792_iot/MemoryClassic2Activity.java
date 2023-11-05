package com.example.lab6_20191792_iot;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.lab6_20191792_iot.databinding.ActivityMemoryClassic2Binding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MemoryClassic2Activity extends AppCompatActivity {

    int pairsMatched = 0;
    private int totalImages;

    ActivityMemoryClassic2Binding binding;
    FirebaseFirestore db;
    ArrayList<Uri> imageUris;
    private boolean[] cardFlipped;
    private ArrayList<FrameLayout> cardViews;
    private Uri reverseImageUri; // Imagen para el reverso de la tarjeta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemoryClassic2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        // Obtén las imágenes seleccionadas del intent
        imageUris = getIntent().getParcelableArrayListExtra("imageUris");

        int totalImages = imageUris.size();

        if (totalImages < 2) {
            Toast.makeText(this, "Debes subir al menos 2 imágenes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reverseImageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.trasero);

        int rows, columns;

        // Calcula las dimensiones del tablero
        if (totalImages % 2 == 0) {
            // Si la cantidad de imágenes es par
            rows = totalImages / 2;
            columns = 2;
        } else {
            // Si la cantidad de imágenes es impar
            rows = (totalImages + 1) / 2;
            columns = 2;
        }

        // Distribuye las imágenes aleatoriamente
        List<Uri> shuffledImageUris = new ArrayList<>(imageUris);
        Collections.shuffle(shuffledImageUris);
        List<Uri> distributedImageUris = new ArrayList<>(totalImages * 2);

        // Replicar las imágenes
        for (Uri imageUri : shuffledImageUris) {
            distributedImageUris.add(imageUri);
            distributedImageUris.add(imageUri);
        }

        // Mezcla las imágenes para obtener la distribución deseada
        Collections.shuffle(distributedImageUris);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        gridLayout.setRowCount(rows);
        gridLayout.setColumnCount(columns);

        cardViews = new ArrayList<>();
        cardFlipped = new boolean[totalImages * 2];

        for (int i = 0; i < totalImages * 2; i++) {
            FrameLayout cardView = createCardView(i);
            ImageView cardImage = createCardImage();
            cardView.addView(cardImage);
            gridLayout.addView(cardView);
            cardViews.add(cardView);
        }
    }



    private FrameLayout createCardView(final int cardIndex) {
        FrameLayout cardView = new FrameLayout(this);
        cardView.setLayoutParams(new GridLayout.LayoutParams());
        cardView.setOnClickListener(view -> flipCard(cardIndex));
        return cardView;
    }
    private void displayGameCompletionMessage() {
        // Muestra un Toast que indica que el juego ha finalizado
        Toast.makeText(this, "Finalizó el juego", Toast.LENGTH_SHORT).show();
    }

    private ImageView createCardImage() {
        ImageView cardImage = new ImageView(this);
        cardImage.setLayoutParams(new FrameLayout.LayoutParams(400, 400)); // Tamaño fijo (100x100 píxeles)
        cardImage.setImageURI(reverseImageUri); // Configura el reverso de la tarjeta
        cardImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return cardImage;
    }

    private int firstFlippedCardIndex = -1;


    private void flipCard(int cardIndex) {
        if (!cardFlipped[cardIndex]) {
            FrameLayout cardView = cardViews.get(cardIndex);
            ImageView cardImage = (ImageView) cardView.getChildAt(0);
            cardFlipped[cardIndex] = true;

            cardImage.setImageURI(imageUris.get(cardIndex / 2));

            if (firstFlippedCardIndex == -1) {
                firstFlippedCardIndex = cardIndex;
            } else {
                if (imageUris.get(firstFlippedCardIndex / 2).equals(imageUris.get(cardIndex / 2))) {
                    pairsMatched++;
                    if (pairsMatched == totalImages) {
                        displayGameCompletionMessage();
                    }
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        closeCard(firstFlippedCardIndex);
                        closeCard(cardIndex);
                    }, 1000);
                }
                firstFlippedCardIndex = -1;
            }
        }
    }


    private void closeCard(int cardIndex) {
        FrameLayout cardView = cardViews.get(cardIndex);
        ImageView cardImage = (ImageView) cardView.getChildAt(0);
        cardImage.setImageURI(reverseImageUri);
        cardFlipped[cardIndex] = false;
    }

    private int shuffleCount = 2;

    public void onShuffleButtonClick(View view) {
        if (shuffleCount > 0) {
            shuffleHiddenImages();
            shuffleCount--;

            if (shuffleCount == 0) {
                Button shuffleButton = findViewById(R.id.aleatorizar);
                shuffleButton.setEnabled(false);
            }
        }
    }

    private void shuffleHiddenImages() {
        List<Uri> hiddenImageUris = new ArrayList<>();

        for (int i = 0; i < totalImages * 2; i++) {
            if (!cardFlipped[i]) {
                hiddenImageUris.add(imageUris.get(i / 2));
            }
        }

        Collections.shuffle(hiddenImageUris);

        int hiddenIndex = 0;
        for (int i = 0; i < totalImages * 2; i++) {
            if (!cardFlipped[i]) {
                ImageView cardImage = (ImageView) cardViews.get(i).getChildAt(0);
                cardImage.setImageURI(hiddenImageUris.get(hiddenIndex));
                hiddenIndex++;
            }
        }
    }

    private int helpCount = 2;

    public void onHelpButtonClick(View view) {
        if (helpCount > 0) {
            for (int i = 0; i < totalImages * 2; i++) {
                if (!cardFlipped[i]) {
                    flipCard(i);
                    break;
                }
            }

            helpCount--;

            if (helpCount == 0) {
                Button helpButton = findViewById(R.id.ayuda);
                helpButton.setEnabled(false);
            }
        }
    }


}
