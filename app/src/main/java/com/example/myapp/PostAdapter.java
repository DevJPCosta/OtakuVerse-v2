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

import com.example.myapp.Post;
import com.example.myapp.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
            holder.textViewTitle = convertView.findViewById(R.id.textViewPostTitle);
            holder.textViewContent = convertView.findViewById(R.id.textViewPostContent);
            holder.textViewPostAuthor = convertView.findViewById(R.id.textViewPostAuthor);
            holder.textViewDate = convertView.findViewById(R.id.textViewPostDate);
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

    private void openPostActivity(String postId) {
        // Implemente este método de acordo com a abertura da tela de exibição do post
    }

    private void showToast(String message) {
        // Implemente este método para mostrar um Toast com a mensagem fornecida
    }

    private static class ViewHolder {
        TextView textViewTitle;
        TextView textViewContent;
        TextView textViewPostAuthor;
        TextView textViewDate;
        Button buttonViewPost;
    }
}
