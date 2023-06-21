package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.Comment;
import com.example.myapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        super(context, 0, commentList);
        this.commentList = commentList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
        }

        Comment comment = commentList.get(position);

        TextView textViewCommentContent = convertView.findViewById(R.id.textViewCommentContent);
        TextView textViewCommentAuthor = convertView.findViewById(R.id.textViewCommentAuthor);
        Button buttonDelete = convertView.findViewById(R.id.buttonDelete);

        textViewCommentContent.setText(comment.getContent());
        textViewCommentAuthor.setText(comment.getAuthor());

        // Adicione um ouvinte de clique para o botão de exclusão
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteComment(position);
            }
        });

        return convertView;
    }

    private void deleteComment(int position) {
        commentList.remove(position);
        notifyDataSetChanged();
    }
}
