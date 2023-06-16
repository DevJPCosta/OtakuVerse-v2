package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> commentList;

    public CommentAdapter(Context context, List<String> commentList) {
        super(context, 0, commentList);
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        }

        // Obtenha o comentário atual
        String currentComment = commentList.get(position);

        // Configure a view do item de comentário com os dados do comentário
        TextView textViewCommentContent = itemView.findViewById(R.id.textViewCommentContent);
        textViewCommentContent.setText(currentComment);

        return itemView;
    }
}
