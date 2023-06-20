package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Objects;

public class HomeActivity extends Activity {

    private FirebaseAuth mAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private EditText editTextPost;
    private Button buttonCreatePost;
    private Button buttonViewFeed; // Botão para visualizar o feed

    private FirebaseFirestore db;
    private CollectionReference postsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        buttonLogout = findViewById(R.id.buttonLogout);
        editTextPost = findViewById(R.id.editTextPost);
        buttonCreatePost = findViewById(R.id.buttonCreatePost);
        buttonViewFeed = findViewById(R.id.buttonViewFeed); // Referência ao botão "Ver Feed" no layout

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        buttonCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });

        // Configurar o clique do botão "Ver Feed" para abrir a FeedActivity
        buttonViewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFeedActivity();
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            textViewUserEmail.setText(currentUser.getEmail());
        } else {
            redirectToMainActivity();
        }

        db = FirebaseFirestore.getInstance();
        postsRef = db.collection("posts");
    }

    private void logoutUser() {
        mAuth.signOut();
        redirectToMainActivity();
    }

    private void redirectToMainActivity() {
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish();
    }

    private void redirectToFeedActivity() {
        startActivity(new Intent(HomeActivity.this, FeedActivity.class));
    }

    private void createPost() {
        String postContent = editTextPost.getText().toString().trim();

        if (!postContent.isEmpty()) {
            String postId = postsRef.document().getId();
            String author = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            String authorId = mAuth.getCurrentUser().getUid();
            Date currentDate = new Date(System.currentTimeMillis());
            String postTitle = postContent;

            Post newPost = new Post(postId, postTitle, postContent, author, authorId, currentDate);

            postsRef.document(postId)
                    .set(newPost)
                    .addOnSuccessListener(aVoid -> {
                        showToast("Post criado com sucesso");
                        editTextPost.setText("");
                    })
                    .addOnFailureListener(e -> showToast("Falha ao criar o post"));
        } else {
            showToast("Digite o conteúdo do post");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
