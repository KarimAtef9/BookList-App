package com.example.booklist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private int loaderId = 0;
    private ListView booksListView;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.INVISIBLE);
        TextView textView = findViewById(R.id.no_books);
        textView.setText(R.string.start);

        adapter = new BookAdapter(this, new ArrayList<Book>());
        final LoaderManager.LoaderCallbacks<ArrayList<Book>> object = this;

        Button button = findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                Log.i(LOG_TAG, "Search Button Clicked.");
                //checking for internet connection first
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean isConnected = (cm.getActiveNetworkInfo() != null);

                if (isConnected) {
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
                    progressBar.setVisibility(View.VISIBLE);
                    TextView textView = findViewById(R.id.no_books);
                    textView.setText("");
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    android.app.LoaderManager loaderManager = getLoaderManager();
                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.initLoader(loaderId, null, object);

                    Log.v("MainActivity.java", "--------- current loader id is : " + loaderId);
                    Log.v(LOG_TAG, "Loader manager initiated");
                } else {
                    TextView emptyView = findViewById(R.id.no_books);
                    emptyView.setText(R.string.no_internet);
                    ProgressBar progressBar = findViewById(R.id.loading_spinner);
                    progressBar.setVisibility(View.INVISIBLE);
                }


                booksListView = findViewById(R.id.books_list);
                booksListView.setAdapter(adapter);


                booksListView.setEmptyView(findViewById(R.id.no_books));
            }
        });




    }


    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "BookLoader created in onCreateLoader");
        String url = makeUrl();
        return new BookLoader(this, url);
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<Book>> loader, final ArrayList<Book> books) {
        // Clear the adapter of previous books data
        adapter.clear();
        // If there is a valid list of {@link books}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
        TextView emptyView = (TextView) findViewById(R.id.no_books);
        emptyView.setText(R.string.no_books);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.INVISIBLE);
        Log.v(LOG_TAG, "adapter updated in onLoadFinished");
        getLoaderManager().destroyLoader(loaderId);
        Log.v("MainActivity.java", "--------- current loader id is : " + loaderId);
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Log.i("MainActivity.java", "the clicked book is : " + books.get(position));
                Intent bookIntent = new Intent(MainActivity.this, BookActivity.class);
                bookIntent.putExtra("book", books.get(position));
//                bookIntent.put
                startActivity(bookIntent);

            }
        });
    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<Book>> loader) {
        adapter.clear();
        Log.v(LOG_TAG, "adapter cleared in onLoaderReset");
    }

    private String makeUrl() {
        String url = "https://www.googleapis.com/books/v1/volumes?q=";
        EditText searchBook = findViewById(R.id.book_search);
        EditText searchAuthor = findViewById(R.id.author_search);

        if (searchBook.getText() != null && searchBook.getText().length() > 0) {
            url += searchBook.getText();
        }

        if (searchAuthor.getText() != null && searchAuthor.getText().length() > 0) {
            url += "+inauthor:";
            url += searchAuthor.getText();
        }

        url = url.replaceAll(" ", "+");
        Log.i("MainActivity.java", "from makeUrl method the new url is : " + url);
        return url;
    }


}
