package com.example.backend.Models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user")
public class User{
    @Id
    private String id;
    private String username;
    private String credential;
    private String alias;
    private List<String> followers;
    private List<String> followed;
    private List<String> bookmarked;
    private List<String> preferences;
    private String avatar;

    public User(String username,String credential,String alias){
        super();
        this.username=username;
        this.credential=credential;
        this.alias=alias;
        this.followers=new ArrayList<>();
        this.followed=new ArrayList<>();
        this.bookmarked=new ArrayList<>();
        this.preferences=new ArrayList<>();
        this.avatar="";
    }

    public String getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getCredential(){
        return credential;
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

    public void setId(String id){
        this.id=id;
    }
    public void setCredential(String credential){
        this.credential=credential;
    }
    public void setAlias(String alias){
        this.alias=alias;
    }
    public void addFollowed(String id){
        if(!this.followed.contains(id)){
            this.followed.add(id);
        }
    }
    public void delFollowed(String id){
        this.followed.remove(id);
    }
    public void addFollowers(String id){
        if(!this.followers.contains(id)){
            this.followers.add(id);
        }
    }
    public void delFollowers(String id){
        this.followers.remove(id);
    }
    public void addBookmark(String rid){
        if(!this.bookmarked.contains(rid)){
            this.bookmarked.add(rid);
        }
    }
    public void delBookmark(String rid){
        this.bookmarked.remove(rid);
    }
    public void addPreferences(String pref){
        if(!this.preferences.contains(pref)){
            this.preferences.add(pref);
        }
    }
    public void delPreferences(String pref){
        this.preferences.remove(pref);
    }
    public void setAvatar(String avatar){
        this.avatar=avatar;
    }

}
