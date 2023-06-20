package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;

public class PostActivity extends Activity {

    private TextView textViewPostTitle;
    private TextView textViewPostContent;
    private TextView textViewPostAuthor;

    private FirebaseFirestore db;
    private ListenerRegistration postListener;
    private String postId;

    private Button buttonDelete;
    private Button buttonComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        textViewPostTitle = findViewById(R.id.textViewPostTitle);
        textViewPostContent = findViewById(R.id.textViewPostContent);
        textViewPostAuthor = findViewById(R.id.textViewPostAuthor);

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonComment = findViewById(R.id.buttonComment);

        postId = getIntent().getStringExtra("postId");

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        postListener = db.collection("posts").document(postId)
                .addSnapshotListener(this, (documentSnapshot, e) -> {
                    if (e != null) {
                        exibirMensagemDeErro();
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("title");
                        String content = documentSnapshot.getString("content");
                        String author = documentSnapshot.getString("author");

                        textViewPostTitle.setText(title);
                        textViewPostContent.setText(content);
                        textViewPostAuthor.setText(author);
                    } else {
                        exibirMensagemDeErro();
                    }
                });

        buttonDelete.setOnClickListener(v -> deletePost());

        buttonComment.setOnClickListener(v -> openCommentActivity());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (postListener != null) {
            postListener.remove();
        }
    }

    private void exibirMensagemDeErro() {
        Toast.makeText(this, "Falha ao obter os detalhes do post", Toast.LENGTH_SHORT).show();
    }

    private void deletePost() {
        db.collection("posts").document(postId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Postagem excluída com sucesso");
                        finish();
                    } else {
                        showToast("Falha ao excluir a postagem: " + task.getException().getMessage());
                    }
                });
    }

    private void openCommentActivity() {
        Intent intent = new Intent(PostActivity.this, CommenterActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
