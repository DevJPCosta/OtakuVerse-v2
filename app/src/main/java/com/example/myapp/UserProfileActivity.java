package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.myapp.R;
import java.util.Arrays;

public class UserProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Obter dados do perfil do usuário
        UserProfile userProfile = getUserProfile();

        // Exibir os dados do perfil na atividade
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewAge = findViewById(R.id.textViewAge);
        TextView textViewBio = findViewById(R.id.textViewBio);
        TextView textViewLocation = findViewById(R.id.textViewLocation);
        // ...

        textViewName.setText(userProfile.getName());
        textViewEmail.setText(userProfile.getEmail());
        textViewAge.setText(String.valueOf(userProfile.getAge()));
        textViewBio.setText(userProfile.getBio());
        textViewLocation.setText(userProfile.getLocation());
        // ...
    }

    private UserProfile getUserProfile() {
        // Aqui você pode implementar a lógica para obter os dados do perfil do usuário
        // Pode ser a partir de um banco de dados, serviço web, etc.
        // Por enquanto, retornaremos um UserProfile fictício para fins de exemplo
        return new UserProfile(
                "John Doe",
                "johndoe@example.com",
                30,
                "I'm a software developer",
                "New York",
                Arrays.asList("Programming", "Reading", "Traveling")
        );
    }
}
