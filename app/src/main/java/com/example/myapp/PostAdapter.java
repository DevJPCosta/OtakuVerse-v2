package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends ArrayAdapter<Post> {

    private final Context context;
    private final List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        super(context, 0, postList);
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        }

        // Obtenha o post atual
        Post currentPost = postList.get(position);

        // Configure as views do item do feed com os dados do post
        TextView textViewContent = (TextView) itemView.findViewById(R.id.textViewContent);
        TextView textViewAuthor = (TextView) itemView.findViewById(R.id.textViewAuthor);
        TextView textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);


        textViewContent.setText(currentPost.getContent());
        textViewAuthor.setText(currentPost.getAuthor());
        textViewDate.setText(formatDate(currentPost.getDate()));

        return itemView;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }
}
