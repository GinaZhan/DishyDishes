package com.example.backend.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

@Document(collection = "comment")
public class Comment{
    @Id
    private String id;
    private String uid;
    private String rid;
    private String content;
    private List<String> liked;
    private transient Date timestamp;

    public Comment(String uid,String rid,String content){
        super();
        this.uid=uid;
        this.rid=rid;
        this.content=content;
        this.liked=new ArrayList<>();
    }

    public String getId(){
        return id;
    }
    public String getUid(){
        return uid;
    }
    public String getRid(){
        return rid;
    }
    public String getContent(){
        return content;
    }
    public List<String> getLiked(){
        return liked;
    }
    public Date getTimestamp(){
        if(this.id!=null&&ObjectId.isValid(this.id)){
            ObjectId objectId=new ObjectId(this.id);
            return new Date(objectId.getTimestamp()*1000L);
        }
        return null;
    }

    public void setUid(String uid){
        this.uid=uid;
    }
    public void setRid(String rid){
        this.rid=rid;
    }
    public void setContent(String content){
        this.content=content;
    }

    public void addLiked(String id){
        if(!this.liked.contains(id)){
            this.liked.add(id);
        }
    }
    public void delLiked(String id){
        this.liked.remove(id);
    }
}
