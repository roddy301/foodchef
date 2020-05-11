package com.justifiers.foodchef.Downloads;

public class recipie {

    private int rimagesource;
    private String rname;
    private String rsize;

    public recipie( String name, String size, int imagesource){
        rname=name;
        rsize=size;
        rimagesource=imagesource;

    }


    public int getImageSource() {
        return rimagesource;
    }

    public String getName() {
        return rname;
    }

    public String getSize(){
        return rsize;
    }

}