package com.justifiers.foodchef.LoginAndSignUp;

public class Favourites {
    private String rName;
    private String rImage;
    private String rLikes;
    private String rTime;

    public Favourites(String rName,String rImage, String rLikes, String rTime) {
        this.rName = rName;
        this.rImage = rImage;
        this.rLikes = rLikes;
        this.rTime = rTime;
    }

    public Favourites() {
    }


    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrImage() {
        return rImage;
    }

    public void setrImage(String rImage) {
        this.rImage = rImage;
    }

    public String getrLikes() {
        return rLikes;
    }

    public void setrLikes(String rLikes) {
        this.rLikes = rLikes;
    }

    public String getrTime() {
        return rTime;
    }

    public void setrTime(String rTime) {
        this.rTime = rTime;
    }
}
