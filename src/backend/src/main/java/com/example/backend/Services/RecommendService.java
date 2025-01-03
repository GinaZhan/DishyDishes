package com.example.backend.Services;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.example.backend.Models.Recipe;
import com.example.backend.Models.User;


@Service
public class RecommendService{
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<Recipe> hot(){
        MatchOperation matchOperation=Aggregation.match(
            Criteria.where("_id").gt(new ObjectId(new Date(System.currentTimeMillis()-7*24*60*60*1000)))
        );
        Aggregation aggregation=Aggregation.newAggregation(
            matchOperation,
            Aggregation.sort(Sort.Direction.DESC,"bookmarkedUsers"),
            Aggregation.limit(6)
        );
        AggregationResults<Recipe> results=mongoTemplate.aggregate(aggregation,"recipe",Recipe.class);

        return results.getMappedResults();
    }

    public List<Recipe> random(){
        MatchOperation matchOperation=Aggregation.match(
            Criteria.where("_id").gt(new ObjectId(new Date(System.currentTimeMillis()-(24*60*60*1000))))
        );
        Aggregation aggregation=Aggregation.newAggregation(
            matchOperation,
            Aggregation.sample(6)
        );
        AggregationResults<Recipe> results=mongoTemplate.aggregate(aggregation,"recipe",Recipe.class);

        return results.getMappedResults();
    }

    public List<Recipe> followed(User user){
        List<String> followedUsers=user.getFollowed();
        Query query=new Query(Criteria.where("uploaderId").in(followedUsers))
            .with(Sort.by(Sort.Direction.DESC,"uploadTime"))
            .limit(5);
        return mongoTemplate.find(query,Recipe.class);
    }

}
