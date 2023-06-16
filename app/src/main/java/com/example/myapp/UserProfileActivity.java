package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Obter dados do perfil do usuário
        getUserProfile();
    }

    private void getUserProfile() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                        displayUserProfile(userProfile);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Tratar erros de leitura do banco de dados, se necessário
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

        textViewName.setText(userProfile.getName());
        textViewEmail.setText(userProfile.getEmail());
        textViewAge.setText(String.valueOf(userProfile.getAge()));
        textViewBio.setText(userProfile.getBio());
        textViewLocation.setText(userProfile.getLocation());
        // ...
    }
}
