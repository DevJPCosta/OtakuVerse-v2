package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    private final Context context;
    private final List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
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
        Comment currentComment = commentList.get(position);

        // Configure a view do item de comentário com os dados do comentário
        TextView textViewCommentContent = itemView.findViewById(R.id.textViewCommentContent);
        TextView textViewCommentAuthor = itemView.findViewById(R.id.textViewCommentAuthor);
        TextView textViewCommentDatetime = itemView.findViewById(R.id.textViewCommentDatetime);

        textViewCommentContent.setText(currentComment.getContent());
        textViewCommentAuthor.setText(currentComment.getAuthor());

        String formattedDatetime = currentComment.getDatetime() != null
                ? DateFormat.getDateTimeInstance().format(currentComment.getDatetime())
                : "Data desconhecida";
        textViewCommentDatetime.setText(formattedDatetime);

        return itemView;
    }
}
