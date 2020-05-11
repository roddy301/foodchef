package com.justifiers.foodchef;

import java.io.Serializable;

public class Ingredient implements Serializable {

    String rIng, rQuantity;
//    Double rPrice;

    public Ingredient() {}

    public Ingredient(String rIng, String rQuantity) {
        this.rIng = rIng;
        this.rQuantity = rQuantity;
    }
//    public Ingredient(Double rPrice, String rQuantity, String rIng) {
//        this.rPrice = rPrice;
//        this.rQuantity = rQuantity;
//        this.rIng = rIng;
//    }

    //    public Double getrPrice() {
//        return rPrice;
//    }
//
//    public void setrPrice(Double rPrice) {
//        this.rPrice = rPrice;
//    }
//
    public String getrQuantity() {
        return rQuantity;
    }

    public void setrQuantity(String rQuantity) {
        this.rQuantity = rQuantity;
    }

    public String getrIng() {
        return rIng;
    }

    public void setrIng(String rIng) {
        this.rIng = rIng;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "rIng='" + rIng + '\'' +
                ", rQuantity='" + rQuantity + '\'' +
                '}';
    }
}
