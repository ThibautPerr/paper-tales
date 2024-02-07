package com.example;

public class Resource {
    public enum ResourceType {
        WOOD, MEAT, CRYSTAL, GOLD
    };

    private ResourceType type;
    private int quantity;

    public Resource(ResourceType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public ResourceType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void printResource() {
        System.out.print("Resource: " + type + ", quantity: " + quantity);
    }
}
