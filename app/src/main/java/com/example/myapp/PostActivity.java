package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        textViewPostTitle = findViewById(R.id.textViewPostTitle);
        textViewPostContent = findViewById(R.id.textViewPostContent);
        textViewPostAuthor = findViewById(R.id.textViewPostAuthor);

        // Obtenha o ID do post da Intent
        String postId = getIntent().getStringExtra("postId");

        postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId);

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // O documento do post existe, obtenha os dados
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String content = dataSnapshot.child("content").getValue(String.class);
                    String author = dataSnapshot.child("author").getValue(String.class);

                    // Atualize as TextViews com os detalhes do post
                    textViewPostTitle.setText(title);
                    textViewPostContent.setText(content);
                    textViewPostAuthor.setText(author);
                } else {
                    // O documento do post não existe
                    exibirMensagemDeErro();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Ocorreu um erro ao obter o documento do post
                exibirMensagemDeErro();
            }
        });
    }

    private void exibirMensagemDeErro() {
        Toast.makeText(this, "Falha ao obter os detalhes do post", Toast.LENGTH_SHORT).show();
    }
}
