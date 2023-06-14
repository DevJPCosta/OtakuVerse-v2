package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedActivity extends Activity {

    private ListView listView;
    private List<Post> postList;
    private PostAdapter adapter;

    private EditText editTextPost;
    private Button buttonPost;

    // Referência ao nó "posts" no banco de dados do Firebase
    private DatabaseReference postsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        listView = (ListView) findViewById(R.id.listView);
        editTextPost = (EditText) findViewById(R.id.editTextPost);
        buttonPost = (Button) findViewById(R.id.buttonPost);

        postList = new ArrayList<>(); // Inicialmente, a lista de posts está vazia

        adapter = new PostAdapter(this, postList);
        listView.setAdapter(adapter);

        // Obtém a referência ao nó "posts" no banco de dados do Firebase
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postContent = editTextPost.getText().toString().trim();

                if (!postContent.isEmpty()) {
                    // Cria um ID único para o novo post
                    String postId = postsRef.push().getKey();

                    // Obtém a data e hora atual
                    Date currentDate = new Date();

                    // Cria um novo objeto Post com o conteúdo, autor, data e ID
                    Post newPost = new Post(postId, postContent, "Autor", currentDate);

                    // Salva o novo post no banco de dados do Firebase
                    postsRef.child(postId).setValue(newPost);

                    // Limpa o campo de texto da postagem
                    editTextPost.setText("");

                    showToast("Postagem realizada com sucesso");
                } else {
                    showToast("Digite o conteúdo da postagem");
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
