package com.example.booklist;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        ImageView imageView = findViewById(R.id.full_screen_image);
        Bitmap image = getIntent().getParcelableExtra("image");
        imageView.setImageBitmap(image);
    }
}
