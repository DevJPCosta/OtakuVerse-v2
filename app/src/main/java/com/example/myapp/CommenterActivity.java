package com.example.myapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommenterActivity extends AppCompatActivity {
    private EditText editTextComment;
    private Button buttonAddComment;
    private ListView listViewComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    private FirebaseFirestore db;
    private CollectionReference commentsRef;

    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commenter);

        // Obtenha o ID do post da atividade anterior
        postId = getIntent().getStringExtra("postId");

        // Inicialize as instâncias do Firebase Firestore
        db = FirebaseFirestore.getInstance();
        commentsRef = db.collection("comments");

        // Inicialize as views
        editTextComment = findViewById(R.id.editTextComment);
        buttonAddComment = findViewById(R.id.buttonAddComment);
        listViewComments = findViewById(R.id.listViewComments);

        // Inicialize a lista de comentários
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        listViewComments.setAdapter(commentAdapter);

        // Carregue os comentários existentes
        loadComments();

        // Defina o clique do botão Adicionar Comentário
        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
    }

    private void loadComments() {
        // Crie a consulta para recuperar os comentários com base no ID do post
        Query query = commentsRef.whereEqualTo("postId", postId);

        // Execute a consulta
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Limpe a lista de comentários
                commentList.clear();

                // Adicione os comentários recuperados à lista
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Comment comment = document.toObject(Comment.class);
                    commentList.add(comment);
                }

                // Notifique o adaptador sobre as alterações na lista
                commentAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(CommenterActivity.this, "Erro ao carregar os comentários.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addComment() {
        String commentText = editTextComment.getText().toString().trim();

        // Verifique se o campo de comentário não está vazio
        if (TextUtils.isEmpty(commentText)) {
            Toast.makeText(this, "Digite um comentário.", Toast.LENGTH_SHORT).show();
            return;
        }

        String author = "Nome do Autor"; // Substitua pelo nome do autor real
        Date datetime = new Date(); // Substitua pelo objeto de data e hora real

// Crie um novo objeto de comentário
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(commentText);
        comment.setAuthor(author);

// Adicione o comentário ao Firebase Firestore
        commentsRef.add(comment)
                .addOnSuccessListener(documentReference -> {
                    // Limpe o campo de comentário após adicionar o comentário com sucesso
                    editTextComment.setText("");

                    // Atualize a lista de comentários
                    loadComments();

                    Toast.makeText(CommenterActivity.this, "Comentário adicionado com sucesso.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CommenterActivity.this, "Erro ao adicionar o comentário.", Toast.LENGTH_SHORT).show();
                });
    }
}
