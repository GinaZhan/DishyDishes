package com.example.backend.Repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.backend.Models.Recipe;

public interface RecipeRepo extends MongoRepository<Recipe, String> {
    Optional<Recipe> findById(String id);
    List<Recipe> findByNameContainingIgnoreCase(String name);
    List<Recipe> findByUploaderId(String uid);
    List<Recipe> findByTagsContainingIgnoreCase(String tag);
    List<Recipe> findByIngredientsNameContainingIgnoreCase(String ingredientName);

}
