package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PostAdapter extends ArrayAdapter<Post> {

    private final Context context;
    private final List<Post> postList;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    public PostAdapter(Context context, List<Post> postList) {
        super(context, 0, postList);
        this.context = context;
        this.postList = postList;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
            holder = new ViewHolder();
            holder.textViewTitle = convertView.findViewById(R.id.textViewTitle);
            holder.textViewContent = convertView.findViewById(R.id.textViewContent);
            holder.textViewAuthor = convertView.findViewById(R.id.textViewAuthor);
            holder.textViewDate = convertView.findViewById(R.id.textViewDate);
            holder.buttonComment = convertView.findViewById(R.id.buttonComment);
            holder.listViewComments = convertView.findViewById(R.id.listViewComments);
            holder.buttonDelete = convertView.findViewById(R.id.buttonDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Obtenha o post atual
        Post currentPost = postList.get(position);

        // Configure as views do item do feed com os dados do post
        if (holder.textViewTitle != null) {
            holder.textViewTitle.setText(currentPost.getTitle());
        }
        if (holder.textViewContent != null) {
            holder.textViewContent.setText(currentPost.getContent());
        }
        if (holder.textViewAuthor != null) {
            holder.textViewAuthor.setText(currentPost.getAuthor());
        }
        if (holder.textViewDate != null) {
            holder.textViewDate.setText(formatDate(currentPost.getDate()));
        }

        // Verifica se o autor do post é o mesmo que o usuário atualmente logado
        String currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        String postAuthorId = currentPost.getAuthorId();
        if (currentUserId.equals(postAuthorId)) {
            // O usuário atual é o autor do post, exibe o botão de exclusão
            holder.buttonDelete.setVisibility(View.VISIBLE);
        } else {
            // O usuário atual não é o autor do post, esconde o botão de exclusão
            holder.buttonDelete.setVisibility(View.GONE);
        }

        // Configura o listener de clique para o botão de exclusão
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chama o método para excluir o post
                deletePost(currentPost);
            }
        });

        // Obtém os comentários do post
        List<String> commentIds = currentPost.getCommentIds();
        if (commentIds != null && commentIds.size() > 0) {
            // Obtém os objetos Comment correspondentes aos IDs dos comentários
            getComments(commentIds, holder.listViewComments);
        }

        return convertView;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void deletePost(Post post) {
        // TODO: Implementar a exclusão do post no Firestore
    }

    private void getComments(List<String> commentIds, ListView listViewComments) {
        List<Comment> commentList = new ArrayList<>();
        for (String commentId : commentIds) {
            DocumentReference commentRef = firestore.collection("comments").document(commentId);
            commentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        // Converte o documento em um objeto Comment
                        Comment comment = documentSnapshot.toObject(Comment.class);
                        if (comment != null) {
                            commentList.add(comment);
                        }
                    }
                    // Atualiza o adaptador de comentários
                    CommentAdapter commentAdapter = new CommentAdapter(context, commentList);
                    listViewComments.setAdapter(commentAdapter);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Falha ao obter os comentários.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private static class ViewHolder {
        TextView textViewTitle;
        TextView textViewContent;
        TextView textViewAuthor;
        TextView textViewDate;
        Button buttonComment;
        ListView listViewComments;
        Button buttonDelete;
    }
}
