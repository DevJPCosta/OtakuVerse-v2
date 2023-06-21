package com.example.myapp;
import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.List;

public class Post {
    private String postId;
    private String title;
    private String content;
    private String author;
    private String authorId;
    private Date date;
    private List<String> commentIds;

    // Construtor vazio necessário para o Firestore
    public Post() {
        // Construtor vazio necessário para desserialização do Firebase Firestore
    }
    public Post(String postId, String postTitle, String postContent, String author, String authorId, Date currentDate) {
        this.postId = postId;
        this.title = postTitle;
        this.content = postContent;
        this.author = author;
        this.authorId = authorId;
        this.date = currentDate;
    }

    public Post(String postId, String title, String content, String author, String authorId, Date date, List<String> commentIds) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorId = authorId;
        this.date = date;
        this.commentIds = commentIds;
    }

    public String getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Date getDate() {
        return date;
    }

    @Exclude
    public List<String> getCommentIds() {
        return commentIds;
    }
}
