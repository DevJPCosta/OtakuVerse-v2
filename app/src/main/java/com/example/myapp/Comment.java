package com.example.myapp;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

public class Comment {
    private String commentId;
    private String postId;
    private String content;
    private String author;
    private String authorId;
    private Date datetime;

    public Comment() {
        // Construtor vazio necessário para o Firebase Firestore
    }

    public Comment(String commentId, String postId, String content, String author, String authorId, Date datetime) {
        this.commentId = commentId;
        this.postId = postId;
        this.content = content;
        this.author = author;
        this.authorId = authorId;
        this.datetime = datetime;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public static Comment fromDocumentSnapshot(DocumentSnapshot document) {
        Comment comment = new Comment();
        comment.setCommentId(document.getId());
        comment.setPostId(document.getString("postId"));
        comment.setContent(document.getString("content"));
        comment.setAuthor(document.getString("author"));
        comment.setAuthorId(document.getString("authorId"));
        comment.setDatetime(document.getDate("datetime"));
        return comment;
    }
}
