package com.dungdoan.imageviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button prevButton, nextButton;
    int[] imageIds = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.left, R.drawable.right};
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleAssignIds();

        imageView.setImageResource(imageIds[currentIndex]);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex > 0) {
                    currentIndex--;
                    imageView.setImageResource(imageIds[currentIndex]);
                } else if (currentIndex == 0) {
                    currentIndex = imageIds.length - 1;
                    imageView.setImageResource(imageIds[currentIndex]);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex < imageIds.length - 1) {
                    currentIndex++;
                    imageView.setImageResource(imageIds[currentIndex]);
                } else if (currentIndex == imageIds.length - 1) {
                    currentIndex = 0;
                    imageView.setImageResource(imageIds[currentIndex]);
                }
            }
        });
    }

    private void handleAssignIds() {
        imageView = findViewById(R.id.image_viewer);
        prevButton = findViewById(R.id.btn_previous);
        nextButton = findViewById(R.id.btn_next);
    }
}