package com.example.myapp;

import java.util.List;
import androidx.annotation.Keep;

@Keep
public class UserProfile {
    private String name;
    private String email;
    private int age;
    private String bio;
    private String location;
    private List<String> interests;

    // Construtor sem argumentos necessário para desserialização do Firebase Firestore
    public UserProfile() {
    }

    public UserProfile(String name, String email, int age, String bio, String location, List<String> interests) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.bio = bio;
        this.location = location;
        this.interests = interests;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getInterests() {
        return interests;
    }

    // Implemente os métodos e funcionalidades adicionais do perfil de usuário, como setters, validações, etc.

    // Exemplo de método para verificar se o usuário é maior de idade
    public boolean isAdult() {
        return age >= 18;
    }
}
