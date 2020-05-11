package com.justifiers.foodchef.LoginAndSignUp;

public class Favourites {
    private String rName;
    private String rImage;
    private String rTime;
    private String rVideo;

    public Favourites(String rName,String rImage, String rTime, String rVideo) {
        this.rName = rName;
        this.rImage = rImage;
        this.rTime = rTime;
        this.rVideo = rVideo;
    }

    public Favourites() {
    }

    public String getrVideo() {
        return rVideo;
    }

    public void setrVideo(String rVideo) {
        this.rVideo = rVideo;
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

    public String getrTime() {
        return rTime;
    }

    public void setrTime(String rTime) {
        this.rTime = rTime;
    }
}
