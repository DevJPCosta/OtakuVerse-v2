package com.example.myapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedActivity extends ListActivity {

    private List<Post> postList;
    private PostAdapter adapter;

    private EditText editTextPost;
    private Button buttonPost;

    private DatabaseReference postsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        editTextPost = findViewById(R.id.editTextPost);
        buttonPost = findViewById(R.id.buttonPost);

        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);
        setListAdapter(adapter);

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
            String authorId = "authorId"; // Substitua pelo valor correto do authorId
            Date currentDate = new Date();
            String postTitle = postContent;

            Post newPost = new Post(postId, postTitle, postContent, author, authorId, currentDate);

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

    private void openPostActivity(String postId) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    private static class ViewHolder {
        TextView textViewTitle;
        TextView textViewContent;
        TextView textViewAuthor;
        TextView textViewDate;
        TextView textViewLikes;
        TextView textViewDislikes;
        Button buttonComment;
        ListView listViewComments;
        Button buttonDelete;
        Button buttonViewPost;
    }

    public class PostAdapter extends ArrayAdapter<Post> {

        public PostAdapter(@NonNull Context context, List<Post> postList) {
            super(context, 0, postList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
                holder = new ViewHolder();
                holder.textViewTitle = convertView.findViewById(R.id.textViewTitle);
                holder.textViewContent = convertView.findViewById(R.id.textViewContent);
                holder.textViewAuthor = convertView.findViewById(R.id.textViewAuthor);
                holder.textViewDate = convertView.findViewById(R.id.textViewDate);
                holder.textViewLikes = convertView.findViewById(R.id.textViewLikes);
                holder.textViewDislikes = convertView.findViewById(R.id.textViewDislikes);
                holder.buttonComment = convertView.findViewById(R.id.buttonComment);
                holder.listViewComments = convertView.findViewById(R.id.listViewComments);
                holder.buttonDelete = convertView.findViewById(R.id.buttonDelete);
                holder.buttonViewPost = convertView.findViewById(R.id.buttonViewPost);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Preenche os dados do post nos elementos de interface do usuário
            Post post = getItem(position);
            if (post != null) {
                holder.textViewTitle.setText(post.getTitle());
                holder.textViewContent.setText(post.getContent());
                holder.textViewAuthor.setText(post.getAuthor());
                holder.textViewDate.setText(post.getDate().toString());
                // Configure os outros elementos conforme necessário
            }

            // Configura o listener de clique para o botão de visualizar postagem
            holder.buttonViewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtém o post atual
                    Post currentPost = getItem(position);

                    // Chama o método para abrir a tela de exibição do post
                    openPostActivity(currentPost.getPostId());
                }
            });

            return convertView;
        }
    }
}