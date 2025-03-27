package model;

public class Park {
    private String name;
    private String state;
    private String description;
    private String imageUrl;

    public Park(String name, String state, String description, String imageUrl) {
        this.name = name;
        this.state = state;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() { return name; }
    public String getState() { return state; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }

    @Override
    public String toString() {
        return "Name: " + name + "\nState: " + state + "\nDescription: " + description;
    }
} 