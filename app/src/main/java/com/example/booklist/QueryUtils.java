package com.example.booklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() {

    }

    // return url object of the given url string
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    // converts the input stream of the json response into a string
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    // make the http request and handel the connection , returning with the JSON response string
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null ;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // return the list of books found is the json response
    public static ArrayList<Book> extractBooks(String jsonUrl) {
        ArrayList<Book> books = new ArrayList<>();
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(createUrl(jsonUrl));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the http request in extraction of books", e);
        }
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
//                Log.i("QueryUtils.java", "********************* i is : " + String.valueOf(i));
                JSONObject currentObject = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = currentObject.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                String author = "";
                if (volumeInfo.has("authors")) {
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    author = authorsArray.getString(0);
                    for (int j = 1; j < authorsArray.length(); j++) {
                        author += ", ";
                        author += authorsArray.getString(j);
                    }
                }
                Log.i("QueryUtils.java", "********************* author is : " + author);

                String date = "";
                if (volumeInfo.has("publishedDate")) {
                    date = volumeInfo.getString("publishedDate");
                    date = ((date.length() > 10) ? date.substring(0,10) : date);
                }

                String category;
                if (volumeInfo.has("categories")) {
                    JSONArray categoryArray = volumeInfo.getJSONArray("categories");
                    category = categoryArray.getString(0);
                    for (int j = 1; j < categoryArray.length(); j++) {
                        category += ", ";
                        category += categoryArray.getString(j);
                    }
                } else {
                    category = "Category not specified";
                }
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String coverUrl = imageLinks.getString("thumbnail");
                Bitmap coverImage = downloadImage(coverUrl);
                String description = "";
                if (volumeInfo.has("description")) {
                    description = volumeInfo.getString("description");
                }
                books.add(new Book(title, author, category, date, coverUrl, coverImage, description));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        return books;
    }

    // to download the image of the book
    private static Bitmap downloadImage(String url) {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

}
