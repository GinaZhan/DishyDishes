package com.example.backend.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend.Models.User;

public interface UserRepo extends MongoRepository<User,String>{
    public Optional<User> findById(String id);
    public Optional<User> findByUsername(String username);
    public List<User> findByAlias(String alias);
    
}
