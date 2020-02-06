package com.example.booklist;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class Book implements Parcelable {
    // implements Parcelable to be easily send from activity to another
    private String title;
    private String author;
    private String category;
    private String publishDate;
    private String coverUrl;
    private Bitmap coverImage;
    private String description;

    public Book (String title, String author, String category,
                 String publishDate, String coverUrl, Bitmap coverImage, String description) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.publishDate = publishDate;
        this.coverUrl = coverUrl;
        this.coverImage = coverImage;
        this.description = description;
    }

    protected Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        category = in.readString();
        publishDate = in.readString();
        coverUrl = in.readString();
        coverImage = in.readParcelable(Bitmap.class.getClassLoader());
        description = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Bitmap getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Bitmap coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(category);
        parcel.writeString(publishDate);
        parcel.writeString(coverUrl);
        parcel.writeParcelable(coverImage, i);
        parcel.writeString(description);
    }
}
