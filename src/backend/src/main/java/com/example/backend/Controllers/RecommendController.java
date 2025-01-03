package com.example.backend.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Models.User;
import com.example.backend.Repositories.UserRepo;
import com.example.backend.Services.RecommendService;

@RestController
@RequestMapping("/recommend")
public class RecommendController{
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private UserRepo userRepo;
    
    @GetMapping("/hot")
    public ResponseEntity<?> hot(){
        return ResponseEntity.ok(recommendService.hot());
    }    
    
    @GetMapping("/random")
    public ResponseEntity<?> random(){
        return ResponseEntity.ok(recommendService.random());
    }

    @GetMapping("/followed")
    public ResponseEntity<?> followed(@RequestParam String uid){
        Optional<User> user=userRepo.findById(uid);
        if(!user.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
        }else{
            return ResponseEntity.ok(recommendService.followed(user.get()));
        }
    }
}
