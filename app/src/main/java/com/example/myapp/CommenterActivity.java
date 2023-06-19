package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommenterActivity extends Activity {

    private EditText editTextComment;
    private Button buttonAddComment;
    private ListView listViewComments;

    private DatabaseReference postRef;
    private String postId;
    private List<String> commentList;
    private CommentAdapter commentAdapter;

    // Interface CommentKeyCallback
    private interface CommentKeyCallback {
        void onCommentKeyFound(String commentKey);
        void onCommentKeyNotFound();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commenter);

        editTextComment = findViewById(R.id.editTextComment);
        buttonAddComment = findViewById(R.id.buttonAddComment);
        listViewComments = findViewById(R.id.listViewComments);

        postId = getIntent().getStringExtra("postId");

        postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments");

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

    private void addComment() {
        String comment = editTextComment.getText().toString().trim();
        if (!comment.isEmpty()) {
            // Adicione o comentário à lista
            commentList.add(comment);

            // Notifique o adaptador de que os dados foram alterados
            commentAdapter.notifyDataSetChanged();

            // Limpe o campo de texto do comentário
            editTextComment.setText("");

            // Salve o comentário no Firebase Database
            postRef.push().setValue(comment, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        showToast("Comentário adicionado com sucesso");
                    } else {
                        showToast("Falha ao adicionar o comentário");
                    }
                }
            });
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

            // Encontre a chave do comentário no Firebase Database
            findCommentKey(comment, new CommentKeyCallback() {
                @Override
                public void onCommentKeyFound(String commentKey) {
                    // Remova o comentário do Firebase Database
                    postRef.child(commentKey).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                showToast("Comentário excluído com sucesso");
                            } else {
                                showToast("Falha ao excluir o comentário");
                            }
                        }
                    });
                }

                @Override
                public void onCommentKeyNotFound() {
                    showToast("Falha ao excluir o comentário");
                }
            });
        }
    }

    private void findCommentKey(String comment, CommentKeyCallback callback) {
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    String key = commentSnapshot.getKey();
                    String value = commentSnapshot.getValue(String.class);
                    if (value != null && value.equals(comment)) {
                        // A chave do comentário foi encontrada, chame o método onCommentKeyFound
                        callback.onCommentKeyFound(key);
                        return;
                    }
                }
                // A chave do comentário não foi encontrada, chame o método onCommentKeyNotFound
                callback.onCommentKeyNotFound();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast("Falha ao excluir o comentário");
            }
        });
    }

    private void loadComments() {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    String comment = commentSnapshot.getValue(String.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast("Falha ao carregar os comentários");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
