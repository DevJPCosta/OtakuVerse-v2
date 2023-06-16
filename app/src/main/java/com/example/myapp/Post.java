package com.example.myapp;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Post {
    private String postId;
    private String title;
    private String content;
    private String author;
    private Date date;
    private List<String> comments;
    private List<String> likes;
    private List<String> dislikes;
    private List<String> discussions;

    public Post() {
        // Construtor vazio necessário para o Firebase Database
    }

    public Post(String postId, String content, String author, Date date) {
        this.postId = postId;
        this.content = content;
        this.author = author;
        this.date = date;
        this.comments = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.discussions = new ArrayList<>();
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

    public Date getDate() {
        return date;
    }

    public List<String> getComments() {
        return comments;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public List<String> getLikes() {
        return likes;
    }

    public void toggleLike(String userId) {
        if (likes.contains(userId)) {
            likes.remove(userId);
        } else {
            likes.add(userId);
        }
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public void toggleDislike(String userId) {
        if (dislikes.contains(userId)) {
            dislikes.remove(userId);
        } else {
            dislikes.add(userId);
        }
    }

    public List<String> getDiscussions() {
        return discussions;
    }

    public void toggleDiscussion(String userId) {
        if (discussions.contains(userId)) {
            discussions.remove(userId);
        } else {
            discussions.add(userId);
        }
    }
}
