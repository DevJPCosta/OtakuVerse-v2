package com.example.myapp;

import java.util.List;

public class UserProfile {
    private final String name;
    private final String email;
    private final int age;
    private final String bio;
    private final String location;
    private final List<String> interests;

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
