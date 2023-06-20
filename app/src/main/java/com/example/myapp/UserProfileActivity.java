package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class UserProfileActivity extends Activity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ListenerRegistration userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Obter dados do perfil do usuário
        getUserProfile();
    }

    private void getUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            userListener = db.collection("users").document(userId)
                    .addSnapshotListener((snapshot, exception) -> {
                        if (exception != null) {
                            showToast("Falha ao obter perfil do usuário");
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            UserProfile userProfile = snapshot.toObject(UserProfile.class);
                            displayUserProfile(userProfile);
                        }
                    });
        }
    }

    private void displayUserProfile(UserProfile userProfile) {
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewAge = findViewById(R.id.textViewAge);
        TextView textViewBio = findViewById(R.id.textViewBio);
        TextView textViewLocation = findViewById(R.id.textViewLocation);
        // ...

        if (userProfile != null) {
            textViewName.setText(userProfile.getName());
            textViewEmail.setText(userProfile.getEmail());
            textViewAge.setText(String.valueOf(userProfile.getAge()));
            textViewBio.setText(userProfile.getBio());
            textViewLocation.setText(userProfile.getLocation());
            // ...
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userListener != null) {
            userListener.remove();
        }
    }
}
