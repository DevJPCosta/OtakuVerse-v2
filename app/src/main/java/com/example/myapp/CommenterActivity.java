package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommenterActivity extends Activity {

    private EditText editTextComment;
    private Button buttonAddComment;
    private ListView listViewComments;

    private FirebaseFirestore db;
    private CollectionReference commentsRef;
    private DocumentReference postRef;
    private String postId;
    private List<String> commentList;
    private CommentAdapter commentAdapter;
    private ListenerRegistration commentsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commenter);

        editTextComment = findViewById(R.id.editTextComment);
        buttonAddComment = findViewById(R.id.buttonAddComment);
        listViewComments = findViewById(R.id.listViewComments);

        postId = getIntent().getStringExtra("postId");

        db = FirebaseFirestore.getInstance();
        commentsRef = db.collection("posts").document(postId).collection("comments");
        postRef = db.collection("posts").document(postId);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        listViewComments.setAdapter(commentAdapter);

        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        listViewComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteComment(position);
            }
        });

        loadComments();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commentsListener != null) {
            commentsListener.remove();
        }
    }

    private void addComment() {
        String comment = editTextComment.getText().toString().trim();
        if (!comment.isEmpty()) {
            // Adicione o comentário à lista
            commentList.add(comment);

            // Notifique o adaptador de que os dados foram alterados
            commentAdapter.notifyDataSetChanged();

            // Limpe o campo de texto do comentário
            editTextComment.setText("");

            // Crie um objeto Map para representar os dados do comentário
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("comment", comment);

            // Salve o comentário no Cloud Firestore
            commentsRef.add(commentData)
                    .addOnSuccessListener(documentReference -> showToast("Comentário adicionado com sucesso"))
                    .addOnFailureListener(e -> showToast("Falha ao adicionar o comentário"));
        } else {
            showToast("Digite um comentário válido");
        }
    }

    private void deleteComment(int position) {
        if (position >= 0 && position < commentList.size()) {
            String comment = commentList.get(position);

            // Remova o comentário da lista
            commentList.remove(position);

            // Notifique o adaptador de que os dados foram alterados
            commentAdapter.notifyDataSetChanged();

            // Encontre o documento do comentário no Cloud Firestore
            Query query = commentsRef.whereEqualTo("comment", comment).limit(1);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String commentKey = documentSnapshot.getId();

                        // Remova o comentário do Cloud Firestore
                        commentsRef.document(commentKey)
                                .delete()
                                .addOnSuccessListener(aVoid -> showToast("Comentário excluído com sucesso"))
                                .addOnFailureListener(e -> showToast("Falha ao excluir o comentário"));
                    } else {
                        showToast("Falha ao excluir o comentário");
                    }
                } else {
                    showToast("Falha ao excluir o comentário");
                }
            }).addOnFailureListener(e -> showToast("Falha ao excluir o comentário"));
        }
    }

    private void loadComments() {
        commentsListener = commentsRef.addSnapshotListener(this, (querySnapshot, e) -> {
            if (e != null) {
                showToast("Falha ao carregar os comentários");
                return;
            }

            if (querySnapshot != null) {
                commentList.clear();
                for (DocumentSnapshot documentSnapshot : querySnapshot) {
                    String comment = documentSnapshot.getString("comment");
                    if (comment != null) {
                        commentList.add(comment);
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
