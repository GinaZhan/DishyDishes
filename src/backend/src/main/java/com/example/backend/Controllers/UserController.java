package com.example.backend.Controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.Models.Recipe;
import com.example.backend.Models.User;
import com.example.backend.DTOs.UserDTO;
import com.example.backend.Repositories.RecipeRepo;
import com.example.backend.Repositories.UserRepo;
import com.example.backend.Services.GridFsFileStorageService;
import com.example.backend.Services.RecipeService;
import com.example.backend.Services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RecipeRepo recipeRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private GridFsFileStorageService gridFsFileStorageService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestParam String username,@RequestParam String password,@RequestParam String alias){
        Optional<User> user=userRepo.findByUsername(username);
        if(user.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exist: "+username);
        }else{
            UserDTO u=userService.createUser(username,password,alias);
            return ResponseEntity.ok(u.getId());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,@RequestParam String password){
        Optional<User> user=userRepo.findByUsername(username);
        if(user.isPresent()){
            String token=userService.login(user.get(),password);
            if(token.isEmpty()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }else{
                return ResponseEntity.ok(token);
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for username: "+username);
        }
    }

    @PostMapping("/login/check")
    public ResponseEntity<?> checkLogin(HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            return ResponseEntity.ok(uid);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id){
        Optional<User> user=userRepo.findById(id);
        if(user.isPresent()){
            return ResponseEntity.ok(new UserDTO(user.get()));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for id: "+id);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        Optional<User> user=userRepo.findByUsername(username);
        if(user.isPresent()){
            return ResponseEntity.ok(new UserDTO(user.get()));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for username: "+username);
        }
    }

    @GetMapping("/search")
    public List<UserDTO> getUserByAlias(@RequestParam String alias){
        return userRepo.findByAlias(alias).stream().map(UserDTO::new).collect(Collectors.toList());
    }

    // for test purposes
    // @GetMapping("/all")
    // public List<User> getAll(){
    //     return userRepo.findAll();
    // }

    @PostMapping("/follow/add")
    public ResponseEntity<?> follow(@RequestParam String followId,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid.equals(followId)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot follow the user themselves");
        }
        if(uid!=null&&!uid.isEmpty()){
            Optional<User> user=userRepo.findById(uid);
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
            }
            Optional<User> followUser=userRepo.findById(followId);
            if(followUser.isPresent()){
                user.get().addFollowed(followId);
                followUser.get().addFollowers(uid);
                userRepo.save(user.get());
                userRepo.save(followUser.get());
                return ResponseEntity.ok(true);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Followed user do not exist, id: "+followId);
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/follow/del")
    public ResponseEntity<?> unfollow(@RequestParam String followId,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Optional<User> user=userRepo.findById(uid);
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
            }
            Optional<User> followUser=userRepo.findById(followId);
            if(followUser.isPresent()){
                user.get().delFollowed(followId);
                followUser.get().delFollowers(uid);
                userRepo.save(user.get());
                userRepo.save(followUser.get());
                return ResponseEntity.ok(true);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Followed user do not exist, id: "+followId);
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/bookmark/add")
    public ResponseEntity<?> bookmark(@RequestParam String rid,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Optional<User> user=userRepo.findById(uid);
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
            }
            Optional<Recipe> recipe=recipeRepo.findById(rid);
            if(recipe.isPresent()){
                user.get().addBookmark(rid);
                userRepo.save(user.get());
                recipeService.bookmarkRecipe(rid,uid);
                return ResponseEntity.ok(true);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe do not exist, id: "+rid);
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/bookmark/del")
    public ResponseEntity<?> unbookmark(@RequestParam String rid,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Optional<User> user=userRepo.findById(uid);
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
            }
            user.get().delBookmark(rid);
            userRepo.save(user.get());
            recipeService.unbookmarkRecipe(rid,uid);
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
    @PostMapping("/preference/add")
    public ResponseEntity<?> addPref(@RequestParam String pref,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Optional<User> user=userRepo.findById(uid);
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
            }
            user.get().addPreferences(pref);
            userRepo.save(user.get());
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/preference/del")
    public ResponseEntity<?> delPref(@RequestParam String pref,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Optional<User> user=userRepo.findById(uid);
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
            }
            user.get().delPreferences(pref);
            userRepo.save(user.get());
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/avatar/set")
    public ResponseEntity<?> setAvatar(@RequestParam("avatar") MultipartFile avatar,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Optional<User> user=userRepo.findById(uid);
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
            }
            if(!avatar.isEmpty()){
                try{
                    String avatarUrl;
                    byte[] bytes = avatar.getBytes();
                    
                    avatarUrl=gridFsFileStorageService.store(bytes,"avatar.jpg","image/jpeg");
                    log.info(avatarUrl);
                    user.get().setAvatar(avatarUrl);
                    userRepo.save(user.get());
                    
                    return ResponseEntity.ok().body(true);
                }catch(Exception e){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating avatar");
                }
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty avatar file");
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @GetMapping("/recipe")
    public List<Recipe> getUserRecipe(@RequestParam String uid){
        return recipeRepo.findByUploaderId(uid);
    }
}
