package com.justifiers.foodchef.Instructions;

public class InstructionItem {

    String description;
    String imageURL;
    int order;

    public InstructionItem(String description, String imageURL, int order) {
        this.description = description;
        this.imageURL = imageURL;
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getOrder() {
        return order;
    }
}
