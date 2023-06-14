package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends Activity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // Verificar se o e-mail é válido
                if (isValidEmail(email)) {
                    // Realizar o registro do usuário
                    registerUser(name, email, password);
                } else {
                    // E-mail inválido
                    showToast("E-mail inválido");
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        // Verificar se o e-mail é válido
        // Você pode substituir essa lógica com sua própria validação de e-mail
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void registerUser(String name, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro bem-sucedido
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            showToast("Registro realizado com sucesso");
                            navigateToHomeScreen();
                        } else {
                            // Registro falhou
                            showToast("Falha no registro: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHomeScreen() {
        // Lógica para navegar para a próxima tela (por exemplo, a tela inicial do aplicativo)
        Intent intent = new Intent(RegisterActivity.this, UserProfile.class);
        startActivity(intent);
        finish(); // Opcionalmente, você pode finalizar a atividade atual se não precisar mais dela na pilha de atividades.
    }
}
