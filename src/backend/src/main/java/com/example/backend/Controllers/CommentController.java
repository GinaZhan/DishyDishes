package com.example.backend.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Models.Comment;
import com.example.backend.Models.User;
import com.example.backend.Repositories.CommentRepo;
import com.example.backend.Repositories.UserRepo;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comment")
public class CommentController{
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/id/{id}")
    public Comment getCommentContentById(@PathVariable String id){
        return commentRepo.getCommentById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam String rid,@RequestParam String content,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Comment comment=new Comment(uid,rid,content);
            commentRepo.save(comment);
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
    
    @PostMapping("/del")
    public ResponseEntity<?> del(@RequestParam String id,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Comment comment=commentRepo.getCommentById(id);
            if(comment!=null&&comment.getUid().equals(uid)){
                commentRepo.delete(comment);
                return ResponseEntity.ok(true);
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("uid doesnt match");
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam String rid,HttpServletRequest request){
        return ResponseEntity.ok(commentRepo.findByRid(rid));
    }

    @PostMapping("/liked/add")
    public ResponseEntity<?> like(@RequestParam String cid,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Optional<User> user=userRepo.findById(uid);
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
            }
            Optional<Comment> comment=commentRepo.findById(cid);
            if(comment.isPresent()){
                comment.get().addLiked(uid);
                commentRepo.save(comment.get());
                return ResponseEntity.ok(true);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment do not exist, id: "+cid);
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/liked/del")
    public ResponseEntity<?> unlike(@RequestParam String cid,HttpServletRequest request){
        String uid=(String) request.getAttribute("uid");
        if(uid!=null&&!uid.isEmpty()){
            Optional<User> user=userRepo.findById(uid);
            if(!user.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current user do not exist, id: "+uid);
            }
            Optional<Comment> comment=commentRepo.findById(cid);
            if(comment.isPresent()){
                comment.get().delLiked(uid);
                commentRepo.save(comment.get());
                return ResponseEntity.ok(true);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment do not exist, id: "+cid);
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
