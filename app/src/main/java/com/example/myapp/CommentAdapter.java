package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CommentAdapter extends FirestoreAdapter<CommentAdapter.CommentViewHolder, Comment> {

    public CommentAdapter(Query query) {
        super(new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build());
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment comment) {
        String commentContent = comment.getContent();

        holder.textViewCommentContent.setText(commentContent);
        holder.textViewCommentAuthor.setText("Author: Loading...");

        // Recuperar informações do autor do comentário do Firestore
        String authorId = comment.getAuthorId();
        FirebaseFirestore.getInstance().collection("users").document(authorId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String authorName = documentSnapshot.getString("name");
                        holder.textViewCommentAuthor.setText("Author: " + authorName);
                    } else {
                        holder.textViewCommentAuthor.setText("Author: Unknown");
                    }
                });
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCommentContent;
        TextView textViewCommentAuthor;
        Button buttonDelete;

        CommentViewHolder(View itemView) {
            super(itemView);
            textViewCommentContent = itemView.findViewById(R.id.textViewCommentContent);
            textViewCommentAuthor = itemView.findViewById(R.id.textViewCommentAuthor);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
