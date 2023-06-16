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
        final Post currentPost = postList.get(position);

        // Configure as views do item do feed com os dados do post
        TextView textViewContent = itemView.findViewById(R.id.textViewContent);
        TextView textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
        TextView textViewDate = itemView.findViewById(R.id.textViewDate);

        textViewContent.setText(currentPost.getContent());
        textViewAuthor.setText(currentPost.getAuthor());
        textViewDate.setText(formatDate(currentPost.getDate()));

        // Botão de comentar
        Button buttonComment = itemView.findViewById(R.id.buttonComment);
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para lidar com o clique no botão de comentar
                // Implemente a funcionalidade desejada, como abrir uma tela de comentários
            }
        });

        // Botão de curtir
        Button buttonLike = itemView.findViewById(R.id.buttonLike);
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para lidar com o clique no botão de curtir
                // Implemente a funcionalidade desejada, como adicionar/remover a curtida da postagem
            }
        });

        // Botão de discutir
        Button buttonDiscuss = itemView.findViewById(R.id.buttonDiscuss);
        buttonDiscuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para lidar com o clique no botão de discutir
                // Implemente a funcionalidade desejada, como abrir uma tela de discussões
            }
        });

        return itemView;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }
}
