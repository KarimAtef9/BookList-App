package com.example.booklist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> books;
    public BookAdapter(Context context, ArrayList<Book> bookList) {
        super(context, 0, bookList);
        this.books = bookList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }

        TextView category = listItemView.findViewById(R.id.category);
        TextView title = listItemView.findViewById(R.id.title);
        TextView author = listItemView.findViewById(R.id.author);
        TextView date = listItemView.findViewById(R.id.date);
        ImageView cover = listItemView.findViewById(R.id.book_image);

        Book currentBook = books.get(position);

        category.setText(currentBook.getCategory());
        title.setText(currentBook.getTitle());
        author.setText(currentBook.getAuthor());
        date.setText(currentBook.getPublishDate());
        cover.setImageBitmap(currentBook.getCoverImage());




        return listItemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
