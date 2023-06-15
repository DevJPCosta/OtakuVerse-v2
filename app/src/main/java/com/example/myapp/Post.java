package com.example.myapp;

import java.util.Date;

public class Post {
    private String postId; // ID único do post
    private String content;
    private String author;
    private Date date;

    public Post() {
        // Construtor vazio necessário para o Firebase Database
    }

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
}
