package com.example.myapp;

import java.util.Date;

public class Post {
    private final String postId; // ID único do post
    private final String content;
    private final String author;
    private final Date date;

    public Post(String postId, String content, String author, Date date) {
        this.postId = postId;
        this.content = content;
        this.author = author;
        this.date = date;
    }

    public String getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public char getTitle() {
        return 0;
    }
}
