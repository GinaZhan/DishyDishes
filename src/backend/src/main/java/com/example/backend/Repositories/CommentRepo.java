package com.example.backend.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend.Models.Comment;

public interface CommentRepo extends MongoRepository<Comment,String>{
    public Comment getCommentById(String id);
    List<Comment> findByRid(String rid);
}
