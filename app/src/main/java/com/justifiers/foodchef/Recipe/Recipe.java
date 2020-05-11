package com.justifiers.foodchef.Recipe;


import com.justifiers.foodchef.Instructions.InstructionItem;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    private String Utensils;
    private Object ingredients;
    private String likes;
    private String rImage;
    private String rName;
    private String rNameFr;
    private String rNameUk;
    private String rNameHi;
    private String rTime;
    private String rType;
    private String rTypeFr;
    private String rTypeUa;
    private String rTypeHi;
    private String rVideo;
    private String rDescription;
    private List<InstructionItem> rInstructionSteps;

    public Recipe() {
    }

    public Recipe(String Utensils, Object ingredients, String likes, String rImage, String rName, String rNameFr, String rNameUk, String rNameHi, String rTime, String rType, String rTypeFr, String rTypeUa, String rTypeHi, String rVideo, String rDescription, List<InstructionItem> rInstructionSteps) {
        this.Utensils = Utensils;
        this.ingredients = ingredients;
        this.likes = likes;
        this.rImage = rImage;
        this.rName = rName;
        this.rNameFr = rNameFr;
        this.rNameUk = rNameUk;
        this.rNameHi = rNameHi;
        this.rTime = rTime;
        this.rType = rType;
        this.rTypeFr = rTypeFr;
        this.rTypeUa = rTypeUa;
        this.rTypeHi = rTypeHi;
        this.rVideo = rVideo;
        this.rDescription = rDescription;
        this.rInstructionSteps = rInstructionSteps;
    }

    public String getUtensils() {
        return Utensils;
    }

    public void setUtensils(String utensils) {
        this.Utensils = utensils;
    }

    public Object getIngredients() {
        return ingredients;
    }

    public void setIngredients(Object ingredients) {
        this.ingredients = ingredients;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getrImage() {
        return rImage;
    }

    public void setrImage(String rImage) {
        this.rImage = rImage;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrNameFr() {
        return rNameFr;
    }

    public void setrNameFr(String rNameFr) {
        this.rNameFr = rNameFr;
    }

    public String getrNameUk() {
        return rNameUk;
    }

    public void setrNameUk(String rNameUk) {
        this.rNameUk = rNameUk;
    }

    public String getrNameHi() {
        return rNameHi;
    }

    public void setrNameHi(String rNameHi) {
        this.rNameHi = rNameHi;
    }

    public String getrTime() {
        return rTime;
    }

    public void setrTime(String rTime) {
        this.rTime = rTime;
    }

    public String getrType() {
        return rType;
    }

    public void setrType(String rType) {
        this.rType = rType;
    }

    public String getrTypeFr() {
        return rTypeFr;
    }

    public void setrTypeFr(String rTypeFr) {
        this.rTypeFr = rTypeFr;
    }

    public String getrTypeUa() {
        return rTypeUa;
    }

    public void setrTypeUa(String rTypeUa) {
        this.rTypeUa = rTypeUa;
    }

    public String getrTypeHi() {
        return rTypeHi;
    }

    public void setrTypeHi(String rTypeHi) {
        this.rTypeHi = rTypeHi;
    }

    public String getrVideo() {
        return rVideo;
    }

    public void setrVideo(String rVideo) {
        this.rVideo = rVideo;
    }

    public String getrDescription() {
        return rDescription;
    }

    public void setrDescription(String rDescription) {
        this.rDescription = rDescription;
    }

    public List<InstructionItem> getrInstructionSteps() {
        return rInstructionSteps;
    }

    public void setrInstructionSteps(List<InstructionItem> rInstructionSteps) {
        this.rInstructionSteps = rInstructionSteps;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "ingredients=" + ingredients +
                ", likes='" + likes + '\'' +
                ", rImage='" + rImage + '\'' +
                ", rName='" + rName + '\'' +
                ", rNameFr='" + rNameFr + '\'' +
                ", rNameUk='" + rNameUk + '\'' +
                ", rNameHi='" + rNameHi + '\'' +
                ", rTime='" + rTime + '\'' +
                ", rType='" + rType + '\'' +
                ", rTypeFr='" + rTypeFr + '\'' +
                ", rTypeUa='" + rTypeUa + '\'' +
                ", rTypeHi='" + rTypeHi + '\'' +
                ", rVideo='" + rVideo + '\'' +
                ", rDescription='" + rDescription + '\'' +
                ", rInstructionSteps=" + rInstructionSteps +
                '}';
    }
}
