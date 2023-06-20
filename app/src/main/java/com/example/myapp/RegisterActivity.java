package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.Collections;
import java.util.List;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.myapp.UserProfileActivity;

public class RegisterActivity extends Activity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;

    private EditText editTextAge;
    private  EditText editTextBio;
    private EditText editTextLocation;
    private EditText editTextInterests;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mUsersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUsersCollection = mFirestore.collection("users");

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextAge = findViewById(R.id.editTextAge);
        editTextBio = findViewById(R.id.editTextBio);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextInterests = findViewById(R.id.editTextInterests);
        buttonRegister = findViewById(R.id.buttonRegister);



        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String age = editTextAge.getText().toString();
                String bio = editTextBio.getText().toString();
                String location = editTextLocation.getText().toString();
                String interests = editTextInterests.getText().toString();


                registerUser(name, email, password, age, bio, location, interests);
            }
        });
    }

    private void registerUser(final String name, String email, String password, String age, String bio, String location, String interests) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro do usuário bem-sucedido
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Criação do documento do usuário no Firestore
                                DocumentReference userRef = mUsersCollection.document(user.getUid());

                                // Criação do objeto UserProfile com os campos adicionais
                                UserProfile userProfile = new UserProfile(
                                        name,
                                        email,
                                        Integer.parseInt(age),
                                        bio,
                                        location,
                                        Collections.singletonList(interests)
                                );

                                // Salvando o UserProfile no Firestore
                                userRef.set(userProfile)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Registro completo, redirecionar para a tela de login
                                                showToast("Usuário registrado com sucesso");
                                                navigateToLoginActivity();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                showToast("Falha ao registrar o usuário");
                                            }
                                        });
                            }
                        } else {
                            // Falha no registro do usuário
                            showToast("Falha ao registrar o usuário");
                        }
                    }
                });
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
