package com.example.mkarman.bloodbank;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;



public class Members {
    private String userID,age,blod_type,city,name,phoneno,zone;
    public Map<String, Boolean> stars = new HashMap<>();

    public Members() {
    }

    public Members(String age, String blod_type, String name, String phoneno, String zone) {
        this.age = age;
        this.blod_type = blod_type;
        //this.city = city;
        this.name = name;
        this.phoneno = phoneno;
        this.zone = zone;

    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Members(String userID , String age, String blod_type, String name, String phoneno, String zone, String city) {
         this.userID = userID ;
        this.age = age;
        this.blod_type = blod_type;
        this.city = city;
        this.name = name;
        this.phoneno = phoneno;
        this.zone = zone;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("age", age);
        result.put("blod_type", blod_type);
        result.put("city", city);
        result.put("name", name);
        result.put("phoneno", phoneno);
        result.put("zone", zone);

        return result;
    }


    public String getZone() {

        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPhoneno() {

        return phoneno;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlod_type() {

        return blod_type;
    }

    public void setBlod_type(String blod_type) {
        this.blod_type = blod_type;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

