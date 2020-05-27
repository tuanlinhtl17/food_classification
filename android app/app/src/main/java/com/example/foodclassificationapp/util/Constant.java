package com.example.foodclassificationapp.util;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    private Constant() {
        throw  new IllegalStateException("Utility class");
    }
    public static final String CLASSIFY_URL = "http://july17.tk/predict";
    public static final int CAMERA_IMAGE_REQUEST_CODE = 100;
    public static final String USER_DB = "user";
    public static final String HASAGI_DB = "hasagi";
    public static final String FOOD_DB = "food";
    public static final String NAME = "name";
    public static final String CARBOHYDRATE = "cacbohydrat";
    public static final String CALORIES = "calories";
    public static final String FAT = "fat";
    public static final String IMAGE = "image";
    public static final String PROTEIN = "protein";
    public static final String TIME = "time";
    public static final String METS = "mets";
    public static final String TYPE = "type";
    public static final String FITNESS = "fitness";
    public static final String VIDEO = "video";
    public static final String DESCRIPTION = "description";
    public static final String CALORIE_BURN = "caloBurn";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String AGE = "age";
    public static final String VALUE = "value";
    public static final String LAST_DATE = "lastDate";
    public static final String GENDER = "gender";
    public static final String EMAIL = "email";
    public static final String DAILY_ACTIVITIES = "daily_activities";
    public static final String ACTIVITY_IMG = "ACTIVITY_IMG";
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    public static final String CALORIE_BURNED = "caloriesBurned";
    public static final String FOOD_CAMERA = "foodCamera";

    public static final Map<Integer, String> MONTH = new HashMap<>();
    static {
        MONTH.put(0, "January"); MONTH.put(1, "February"); MONTH.put(2, "March"); MONTH.put(3, "April");
        MONTH.put(4, "May"); MONTH.put(5, "June"); MONTH.put(6, "July"); MONTH.put(7, "August");
        MONTH.put(8, "September"); MONTH.put(9, "October"); MONTH.put(10, "November"); MONTH.put(11, "December");
    }
}
