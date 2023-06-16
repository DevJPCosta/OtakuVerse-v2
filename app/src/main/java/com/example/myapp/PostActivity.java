package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PostActivity extends Activity {

    private TextView textViewPostTitle;
    private TextView textViewPostContent;
    private TextView textViewPostAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        textViewPostTitle = findViewById(R.id.textViewPostTitle);
        textViewPostContent = findViewById(R.id.textViewPostContent);
        textViewPostAuthor = findViewById(R.id.textViewPostAuthor);

        // Obtenha o ID do post da Intent
        String postId = getIntent().getStringExtra("postId");

        // Aqui você pode recuperar os detalhes do post usando o ID e atualizar as visualizações de acordo
        // Por exemplo, você pode consultar o Firebase ou qualquer outra fonte de dados para obter os detalhes do post
        // e definir os valores nas TextViews correspondentes

        // Exemplo de atualização das TextViews com os detalhes do post
        textViewPostTitle.setText("Título do Post");
        textViewPostContent.setText("Conteúdo do Post");
        textViewPostAuthor.setText("Autor do Post");
    }
}
