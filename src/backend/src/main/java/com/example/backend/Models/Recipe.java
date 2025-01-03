package com.example.backend.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "recipe")
public class Recipe{
    @Id
    private String id;
    private String name;
    private String description;
    private String coverPhoto;
    private String uploaderId;
    private Date uploadTime;
    private List<String> bookmarkedUsers = new ArrayList<>();
    private List<Step> steps;
    private List<Ingredient> ingredients;
    private List<String> tags;

    //the storage service returns a URL or path as a String after saving the file,
    // which is then stored in the Recipe object.

    public Recipe() {
        this.uploadTime = new Date(); // Initialize upload time upon creation
    }

    public Recipe(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public static class Step {
        private String text;
        private String imageUrl; // Image URL is optional

        public Step(String text, String imageUrl) {
            this.text = text;
            this.imageUrl = imageUrl;
        }

        // Getters and setters
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public static class Ingredient {
        private String name;
        private String amount; // Amount is mandatory

        public Ingredient(String name, String amount) {
            this.name = name;
            this.amount = amount;
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getUploaderId() {return this.uploaderId; }

    public void setUploaderId(String uploaderId) {this.uploaderId = uploaderId; }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void addBookmarkedUser(String userId) {
        if (!this.bookmarkedUsers.contains(userId)) {
            this.bookmarkedUsers.add(userId);
        }
    }

    public void removeBookmarkedUser(String userId) {
        this.bookmarkedUsers.remove(userId);
    }

    public List<String> getBookmarkedUsers() {
        return this.bookmarkedUsers;
    }

    public int getBookmarkNumber() {
        return this.bookmarkedUsers.size();
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
