package com.example.foodclassificationapp.entity;

public class UserProfile {
    String image;
    String fullName;
    String email;
    int age;
    float height;
    float weight;
    String gender;

    public UserProfile(String image, String fullName, String email, int age, float height, float weight, String gender) {
        this.image = image;
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
