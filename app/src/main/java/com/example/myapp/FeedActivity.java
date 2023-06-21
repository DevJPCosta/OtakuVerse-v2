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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedActivity extends ListActivity {

    private List<Post> postList;
    private PostAdapter adapter;

    private EditText editTextPost;
    private Button buttonPost;

    private FirebaseFirestore db;
    private CollectionReference postsRef;
    private ListenerRegistration postsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        editTextPost = findViewById(R.id.editTextPost);
        buttonPost = findViewById(R.id.buttonPost);

        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);
        setListAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        postsRef = db.collection("posts");

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });

        // Carrega os posts do Cloud Firestore
        loadPosts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (postsListener != null) {
            postsListener.remove();
        }
    }

    private void createPost() {
        String postContent = editTextPost.getText().toString().trim();

        if (!postContent.isEmpty()) {
            String postId = postsRef.document().getId();
            String author = "Autor";
            String authorId = "authorId"; // Substitua pelo valor correto do authorId
            Date currentDate = new Date(System.currentTimeMillis()); // Atualizado para obter a data corretamente
            String postTitle = postContent;

            Post newPost = new Post(postId, postTitle, postContent, author, authorId, currentDate);

            postsRef.document(postId)
                    .set(newPost)
                    .addOnSuccessListener(aVoid -> {
                        showToast("Postagem realizada com sucesso");
                        editTextPost.setText("");
                    })
                    .addOnFailureListener(e -> showToast("Falha ao realizar a postagem"));
        } else {
            showToast("Digite o conteúdo da postagem");
        }
    }

    private void loadPosts() {
        postsListener = postsRef.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                showToast("Falha ao carregar os posts");
                return;
            }

            if (querySnapshot != null) {
                postList.clear();
                for (DocumentSnapshot documentSnapshot : querySnapshot) {
                    Post post = documentSnapshot.toObject(Post.class);
                    if (post != null) {
                        postList.add(post);
                    }
                }
                adapter.notifyDataSetChanged();
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
        TextView textViewPostAuthor;
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
                holder.textViewPostAuthor = convertView.findViewById(R.id.textViewPostAuthor);
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
                holder.textViewPostAuthor.setText(post.getAuthor());

                // Verifica se a data não é nula antes de chamá-la
                if (post.getDate() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(post.getDate());
                    holder.textViewDate.setText(formattedDate);
                } else {
                    holder.textViewDate.setText("");
                }

                // Configure os outros elementos conforme necessário
            }

            // Configura o listener de clique para o botão de visualizar postagem
            holder.buttonViewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtém o post atual
                    Post currentPost = getItem(position);

                    // Verifica se o currentPost não é nulo e se o postId não é nulo
                    if (currentPost != null && currentPost.getPostId() != null) {
                        // Chama o método para abrir a tela de exibição do post
                        openPostActivity(currentPost.getPostId());
                    } else {
                        showToast("Erro: postId nulo");
                    }
                }
            });

            return convertView;
        }
    }
}
