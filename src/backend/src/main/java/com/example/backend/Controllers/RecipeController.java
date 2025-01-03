package com.example.backend.Controllers;

import com.example.backend.DTOs.RecipeDTO;
import com.example.backend.Models.Recipe;
import com.example.backend.Services.GridFsFileStorageService;
import com.example.backend.Services.RecipeService;
import com.example.backend.Services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Base64;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;


@RestController
@RequestMapping("/recipe")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private GridFsFileStorageService gridFsFileStorageService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipesByName(@RequestParam String name) {
        List<Recipe> recipes = recipeService.searchRecipesByName(name);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/search/byTag")
    public ResponseEntity<List<RecipeDTO>> searchByTag(@RequestParam String tag) {
        List<RecipeDTO> recipes = recipeService.searchRecipesByTag(tag);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search/byIngredient")
    public ResponseEntity<List<RecipeDTO>> searchByIngredient(@RequestParam String ingredientName) {
        List<RecipeDTO> recipes = recipeService.searchRecipesByIngredient(ingredientName);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable String id) {
        try {
            RecipeDTO recipeDTO = recipeService.getRecipeById(id);
            return ResponseEntity.ok(recipeDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadRecipe(@RequestBody RecipeDTO recipeDTO, HttpServletRequest request) {
        try {
            String uid = (String) request.getAttribute("uid");
            if (uid == null || uid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No valid token provided.");
            }

            String coverPhotoUrl = null;
            if (StringUtils.hasText(recipeDTO.getCoverPhoto())) {
                byte[] coverPhotoBytes = Base64.getDecoder().decode(recipeDTO.getCoverPhoto());
                coverPhotoUrl = gridFsFileStorageService.store(coverPhotoBytes, "cover.jpg", "image/jpeg");
            }

            // Setting uploaderId from the token
            recipeDTO.setUploaderId(uid);

            List<Recipe.Step> steps = recipeDTO.getSteps().stream().map(stepDto -> {
                String imageUrl = null;
                if (StringUtils.hasText(stepDto.getImageBase64())) {
                    byte[] stepImageBytes = Base64.getDecoder().decode(stepDto.getImageBase64());
                    imageUrl = gridFsFileStorageService.store(stepImageBytes, "step.jpg", "image/jpeg");
                }
                return new Recipe.Step(stepDto.getText(), imageUrl);
            }).collect(Collectors.toList());

            List<Recipe.Ingredient> ingredients = recipeDTO.getIngredients().stream()
                    .map(ingr -> new Recipe.Ingredient(ingr.getName(), ingr.getAmount()))
                    .collect(Collectors.toList());

            Recipe recipe = new Recipe();
            recipe.setName(recipeDTO.getName());
            recipe.setDescription(recipeDTO.getDescription());
            recipe.setCoverPhoto(coverPhotoUrl);
            recipe.setUploaderId(uid);
            recipe.setSteps(steps);
            recipe.setIngredients(ingredients);
            recipe.setTags(recipeDTO.getTags());

            Recipe savedRecipe = recipeService.saveRecipe(recipe);

            return ResponseEntity.ok("Recipe uploaded successfully with ID: " + savedRecipe.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload the recipe: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable String id, HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null || uid.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No valid token provided.");
        }
        try {
            userService.delBookmark(id);
            recipeService.deleteRecipeById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateRecipe(@PathVariable String id, @RequestBody RecipeDTO recipeDTO, HttpServletRequest request) {
//        String uid = (String) request.getAttribute("uid");
//        if (uid == null || uid.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No valid token provided.");
//        }
//        RecipeDTO updatedRecipeDTO = recipeService.updateRecipeById(id, recipeDTO);
//        return ResponseEntity.ok(updatedRecipeDTO);
//    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> serveImage(@PathVariable String id) {
        try {
            GridFsResource resource = gridFsFileStorageService.getFile(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(resource.getContentType()))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
