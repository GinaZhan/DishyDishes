package com.example.backend.Services;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.backend.Models.Recipe;
import com.example.backend.Repositories.RecipeRepo;
import com.example.backend.DTOs.RecipeDTO;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RecipeService {

    private final GridFsFileStorageService gridFsFileStorageService;

    @Autowired
    private RecipeRepo recipeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public RecipeService(RecipeRepo recipeRepo, GridFsFileStorageService gridFsFileStorageService) { // Modify constructor
        this.recipeRepo = recipeRepo;
        this.gridFsFileStorageService = gridFsFileStorageService; // Initialize in constructor
    }

    public RecipeDTO getRecipeById(String id) {
        Recipe recipe = recipeRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        RecipeDTO recipeDTO = modelMapper.map(recipe, RecipeDTO.class); // Convert Recipe to RecipeDTO
        return recipeDTO;
    }

    public void deleteRecipeById(String id) {
        if (!recipeRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found with id: " + id);
        }
        recipeRepo.deleteById(id);
    }

//    public RecipeDTO updateRecipeById(String id, RecipeDTO recipeDTO) {
//        Recipe recipe = recipeRepo.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found with id: " + id));
//
//        String originalUploaderId = recipe.getUploaderId();
//        modelMapper.map(recipeDTO, recipe);
//        recipe.setId(id);
//        recipe.setUploaderId(originalUploaderId);
//
//        Recipe updatedRecipe = recipeRepo.save(recipe);
//        return modelMapper.map(updatedRecipe, RecipeDTO.class);
//    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepo.save(recipe);
    }

    public List<Recipe> searchRecipesByName(String name) {
        return recipeRepo.findByNameContainingIgnoreCase(name);
    }

    public List<RecipeDTO> searchRecipesByTag(String tag) {
        List<Recipe> recipes = recipeRepo.findByTagsContainingIgnoreCase(tag);
        return recipes.stream().map(recipe -> modelMapper.map(recipe, RecipeDTO.class)).collect(Collectors.toList());
    }

    public List<RecipeDTO> searchRecipesByIngredient(String ingredientName) {
        List<Recipe> recipes = recipeRepo.findByIngredientsNameContainingIgnoreCase(ingredientName);
        return recipes.stream().map(recipe -> modelMapper.map(recipe, RecipeDTO.class)).collect(Collectors.toList());
    }


    public Recipe bookmarkRecipe(String recipeId, String userId) {
        Recipe recipe = recipeRepo.findById(recipeId).orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        recipe.addBookmarkedUser(userId);
        return recipeRepo.save(recipe);
    }

    public Recipe unbookmarkRecipe(String recipeId, String userId) {
        Recipe recipe = recipeRepo.findById(recipeId).orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        recipe.removeBookmarkedUser(userId);
        return recipeRepo.save(recipe);
    }
}
