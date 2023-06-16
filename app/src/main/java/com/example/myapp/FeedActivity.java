package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedActivity extends Activity {

    private ListView listView;
    private List<Post> postList;
    private PostAdapter adapter;

    private EditText editTextPost;
    private Button buttonPost;

    private DatabaseReference postsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        listView = findViewById(R.id.listView);
        editTextPost = findViewById(R.id.editTextPost);
        buttonPost = findViewById(R.id.buttonPost);

        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);
        listView.setAdapter(adapter);

        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });

        // Carrega os posts do Firebase Database
        loadPosts();
    }

    private void createPost() {
        String postContent = editTextPost.getText().toString().trim();

        if (!postContent.isEmpty()) {
            String postId = postsRef.push().getKey();
            String author = "Autor";
            Date currentDate = new Date();

            Post newPost = new Post(postId, postContent, author, currentDate);

            assert postId != null;
            postsRef.child(postId).setValue(newPost);

            showToast("Postagem realizada com sucesso");
            editTextPost.setText("");
        } else {
            showToast("Digite o conteúdo da postagem");
        }
    }

    private void loadPosts() {
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    postList.add(post);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Falha ao carregar os posts: " + databaseError.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
