package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends Activity {

    private FirebaseAuth mAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Obter o usuário atual do Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // O usuário está logado, exiba o e-mail na TextView
            textViewUserEmail.setText(currentUser.getEmail());
        } else {
            // O usuário não está logado, redirecione para a MainActivity ou realize alguma outra ação
            redirectToMainActivity();
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        redirectToMainActivity();
    }

    private void redirectToMainActivity() {
        // Redirecionar para a MainActivity
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish(); // Finalizar a HomeActivity para que o usuário não possa voltar pressionando o botão "Voltar"
    }
}
