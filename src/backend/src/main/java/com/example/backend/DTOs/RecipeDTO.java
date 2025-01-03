package com.example.backend.DTOs;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class RecipeDTO {
    @Id
    private String id;
    private String name;
    private String description;
    private String coverPhoto; // Base64-encoded string
    private String uploaderId;
    private Date uploadTime;
    private List<String> bookmarkedUsers;
    private List<StepDto> steps;
    private List<IngredientDto> ingredients;
    private List<String> tags;

    // Default constructor
    public RecipeDTO() {}

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public List<String> getBookmarkedUsers() {
        return bookmarkedUsers;
    }

    public void setBookmarkedUsers(List<String> bookmarkedUsers) {
        this.bookmarkedUsers = bookmarkedUsers;
    }

    public List<StepDto> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDto> steps) {
        this.steps = steps;
    }

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDto> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    // StepDto inner class
    public static class StepDto {
        private String text;
        private String imageBase64; // Optional Base64-encoded image

        // Default constructor
        public StepDto() {}

        // Getters and setters
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImageBase64() {
            return imageBase64;
        }

        public void setImageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
        }
    }

    // IngredientDto inner class
    public static class IngredientDto {
        private String name;
        private String amount;

        // Default constructor
        public IngredientDto() {}

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}

