package com.example.booklist;


import android.content.Context;
import android.content.AsyncTaskLoader;


import java.util.ArrayList;

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {
    private String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        final ArrayList<Book> books = QueryUtils.extractBooks(url);
        return books;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
