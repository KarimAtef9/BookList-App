package com.example.booklist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookActivity extends AppCompatActivity {
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);


        TextView title = findViewById(R.id.book_title);
        TextView author = findViewById(R.id.book_author);
        TextView date = findViewById(R.id.book_date);
        TextView description = findViewById(R.id.book_description);
        TextView category = findViewById(R.id.book_category);
        ImageView imageView = findViewById(R.id.book_cover);

        book = getIntent().getParcelableExtra("book");
        Log.i("BookActivity.java", "the clicked book is : " + book);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        category.setText(book.getCategory());
        date.setText(book.getPublishDate());
        description.setText(book.getDescription());
        imageView.setImageBitmap(book.getCoverImage());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fullScreen = new Intent(BookActivity.this, FullScreen.class);
                Bitmap image = book.getCoverImage();
                fullScreen.putExtra("image", image);
                startActivity(fullScreen);
            }
        });

    }

}
