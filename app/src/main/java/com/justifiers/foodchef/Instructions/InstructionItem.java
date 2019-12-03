package com.justifiers.foodchef.Instructions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.justifiers.foodchef.Utilities.LanguageManager;

import java.io.Serializable;

public class InstructionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    String rDescription;
    String rDescriptionFr;
    String rDescriptionHi;
    String rDescriptionUa;
    String videoUrl;

    public InstructionItem() {
    }

    public InstructionItem(String rDescription, String rDescriptionFr, String rDescriptionHi, String rDescriptionUa, String videoUrl) {
        this.rDescription = rDescription;
        this.rDescriptionFr = rDescriptionFr;
        this.rDescriptionHi = rDescriptionHi;
        this.rDescriptionUa = rDescriptionUa;
        this.videoUrl = videoUrl;
    }

    public String getrDescription() {
        return rDescription;
    }

    public void setrDescription(String rDescription) {
        this.rDescription = rDescription;
    }

    public String getrDescriptionFr() {
        return rDescriptionFr;
    }

    public void setrDescriptionFr(String rDescriptionFr) {
        this.rDescriptionFr = rDescriptionFr;
    }

    public String getrDescriptionHi() {
        return rDescriptionHi;
    }

    public void setrDescriptionHi(String rDescriptionHi) {
        this.rDescriptionHi = rDescriptionHi;
    }

    public String getrDescriptionUa() {
        return rDescriptionUa;
    }

    public void setrDescriptionUa(String rDescriptionUa) {
        this.rDescriptionUa = rDescriptionUa;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription(Context context) {
        return new LanguageManager(context, rDescription, rDescriptionFr, rDescriptionHi, rDescriptionUa).getTranslatedText();
    }

    @Override
    public String toString() {
        return "InstructionItem{" +
                "rDescription='" + rDescription + '\'' +
                ", rDescriptionFr='" + rDescriptionFr + '\'' +
                ", rDescriptionHi='" + rDescriptionHi + '\'' +
                ", rDescriptionUa='" + rDescriptionUa + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
