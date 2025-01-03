package com.example.backend.Services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.mindrot.jbcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.backend.Models.Recipe;
import com.example.backend.Models.User;
import com.example.backend.DTOs.UserDTO;
import com.example.backend.Repositories.RecipeRepo;
import com.example.backend.Repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RecipeRepo recipeRepo;


    public UserDTO createUser(String username,String rawPwd,String alias){
        User user=new User(username,BCrypt.hashpw(rawPwd,BCrypt.gensalt()),alias);
        user=userRepo.save(user);
        return new UserDTO(user);
    }

    public String login(User user,String password){
        if(BCrypt.checkpw(password,user.getCredential())){
            return createToken(user.getId(),user.getUsername());
        }else{
            return "";
        }
    }

    private static String createToken(String uid,String username){
        String token=JWT.create()
            .withIssuer("DishyDishes")
            .withAudience(username)
            .withSubject(uid)
            .withIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC))
            .withExpiresAt(LocalDateTime.now().plusWeeks(2).toInstant(ZoneOffset.UTC))
            .sign(Algorithm.HMAC256("DishyDishesJSONWebTokenEncodeAlgorithmSecret"));
        return token;
    }

    public void delBookmark(String rid){
        Optional<Recipe> recipe=recipeRepo.findById(rid);
        for(String i:recipe.get().getBookmarkedUsers()){
            Optional<User> user=userRepo.findById(i);
            user.get().delBookmark(rid);
            userRepo.save(user.get());
        }
    }
}
