package com.example.backend.Configuration;

import com.example.backend.DTOs.RecipeDTO;
import com.example.backend.Models.Recipe;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(RecipeDTO.class, Recipe.class).addMappings(mapper -> {
            mapper.skip(Recipe::setId); // Skip setting the ID when mapping from DTO to Entity
            mapper.skip(Recipe::setUploaderId); // Skip setting the uploaderId when mapping from DTO to Entity
        });
        modelMapper.typeMap(Recipe.class, RecipeDTO.class).addMappings(mapper -> {
            mapper.map(Recipe::getUploaderId, RecipeDTO::setUploaderId);
            mapper.map(Recipe::getUploadTime, RecipeDTO::setUploadTime);
            mapper.map(Recipe::getBookmarkedUsers, RecipeDTO::setBookmarkedUsers);
            mapper.map(Recipe::getId, RecipeDTO::setId);
        });
        return modelMapper;
    }
}
