package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostActivity extends Activity {

    private TextView textViewPostTitle;
    private TextView textViewPostContent;
    private TextView textViewPostAuthor;

    private DatabaseReference postRef;
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

        postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId);

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String content = dataSnapshot.child("content").getValue(String.class);
                    String author = dataSnapshot.child("author").getValue(String.class);

                    textViewPostTitle.setText(title);
                    textViewPostContent.setText(content);
                    textViewPostAuthor.setText(author);
                } else {
                    exibirMensagemDeErro();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                exibirMensagemDeErro();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
            }
        });

        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCommentActivity();
            }
        });
    }

    private void exibirMensagemDeErro() {
        Toast.makeText(this, "Falha ao obter os detalhes do post", Toast.LENGTH_SHORT).show();
    }

    private void deletePost() {
        postRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    showToast("Postagem excluída com sucesso");
                    finish();
                } else {
                    showToast("Falha ao excluir a postagem: " + databaseError.getMessage());
                }
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
