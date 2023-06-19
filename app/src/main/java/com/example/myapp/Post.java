package com.example.myapp;

import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Date;
import java.util.List;

public class Post {
    private String postId;
    private String title;
    private String content;
    private String author;
    private String authorId; // Adicionado o campo para o ID do autor
    private Date date;
    private List<String> comments;
    private List<String> likes;
    private List<String> dislikes;
    private List<String> discussions;
    private String description; // Adicionado o campo para a descrição

    public Post() {
        // Construtor vazio necessário para o Firebase Firestore
    }

    public Post(String postId, String title, String content, String author, String authorId, Date date) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorId = authorId;
        this.date = date;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<String> dislikes) {
        this.dislikes = dislikes;
    }

    public List<String> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<String> discussions) {
        this.discussions = discussions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Post fromDocumentSnapshot(DocumentSnapshot document) {
        Post post = new Post();
        post.setPostId(document.getId());
        post.setTitle(document.getString("title"));
        post.setContent(document.getString("content"));
        post.setAuthor(document.getString("author"));
        post.setAuthorId(document.getString("authorId"));
        post.setDate(document.getDate("date"));
        post.setComments((List<String>) document.get("comments"));
        post.setLikes((List<String>) document.get("likes"));
        post.setDislikes((List<String>) document.get("dislikes"));
        post.setDiscussions((List<String>) document.get("discussions"));
        post.setDescription(document.getString("description")); // Atribuir o valor do campo "description"
        return post;
    }
}
