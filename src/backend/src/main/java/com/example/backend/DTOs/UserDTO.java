package com.example.backend.DTOs;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.example.backend.Models.User;

public class UserDTO{
    @Id
    private String id;
    private String username;
    private String alias;
    private List<String> followers;
    private List<String> followed;
    private List<String> bookmarked;
    private List<String> preferences;
    private String avatar;

    public UserDTO(User user){
        super();
        this.id=user.getId();
        this.username=user.getUsername();
        this.alias=user.getAlias();
        this.followers=user.getFollowers();
        this.followed=user.getFollowed();
        this.bookmarked=user.getBookmarked();
        this.preferences=user.getPreferences();
        this.avatar=user.getAvatar();
    }

    public String getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getAlias(){
        return alias;
    }
    public List<String> getFollowers(){
        return followers;
    }
    public List<String> getFollowed(){
        return followed;
    }
    public List<String> getBookmarked(){
        return bookmarked;
    }
    public List<String> getPreferences(){
        return preferences;
    }
    public String getAvatar(){
        return avatar;
    }
}
