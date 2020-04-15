package com.example.foodclassificationapp.entity;

public class FitnessExercise {
    private String name;
    private String time;
    private String caloriesBurned;
    private String image;
    private String video;
    private String type;
    private String description;

    public FitnessExercise(String name, String time, String type, String image, String video, String description, String caloriesBurned) {
        this.name = name;
        this.time = time;
        this.image = image;
        this.video = video;
        this.type = type;
        this.description = description;
        this.caloriesBurned = caloriesBurned;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(String caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getType() {
        return type;
    }

    public void setType(String level) {
        this.type = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
