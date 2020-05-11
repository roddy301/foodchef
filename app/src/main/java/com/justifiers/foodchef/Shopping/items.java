package com.justifiers.foodchef.Shopping;

public class items {

    private String sname;
    private String ssize;
    private String price;
    private boolean isSelected;

    public items() {
    }

    public items(String sname, String ssize, String price) {
        this.sname = sname;
        this.ssize = ssize;
        this.price = price;
    }

    public items(String sname, String ssize, String price, boolean isSelected) {
        this.sname = sname;
        this.ssize = ssize;
        this.price = price;
        this.isSelected = isSelected;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSsize() {
        return ssize;
    }

    public void setSsize(String ssize) {
        this.ssize = ssize;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
