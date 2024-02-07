package com.example;

public class Resource {
    public enum ResourceType {
        WOOD, MEAT, CRYSTAL, GOLD
    };

    private ResourceType resourceType;
    private int quantity;

    public Resource(ResourceType resourceType, int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void printResource() {
        System.out.print("Resource: " + resourceType + ", quantity: " + quantity);
    }
}
