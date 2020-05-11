package com.justifiers.foodchef.LoginAndSignUp;

import android.widget.ImageButton;

public class User {
    private String id;
    private String name;
    private String email;
    private Object favourites;


    public User(String name, String email, Object favourites) {
        this.name = name;
        this.email = email;
        this.favourites = favourites;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getFavourites() {
        return favourites;
    }

    public void setFavourites(Object favourites) {
        this.favourites = favourites;
    }
}
