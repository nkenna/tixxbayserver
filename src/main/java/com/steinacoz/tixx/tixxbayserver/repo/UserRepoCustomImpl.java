/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import com.steinacoz.tixx.tixxbayserver.model.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 *
 * @author nkenn
 */
public class UserRepoCustomImpl implements UserRepoCustom{
    
    private final MongoTemplate mongoTemplate;
	

    public UserRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<User> aggregateAllUsers() {
      /**  LookupOperation lookup1 = Aggregation.lookup("tixxtag",// Join Table
	            "taguuid",// Query table fields
	            "taguuid",// Join fields in tables
	            "tags");
        
		
		LookupOperation lookup2 = Aggregation.lookup("wallet",// Join Table
	            "_id",// Query table fields
	            "ownerid",// Join fields in tables
	            "wallet");
		
		LookupOperation lookup3 = Aggregation.lookup("transaction",// Join Table
	            "id",// Query table fields
	            "originId",// Join fields in tables
	            "donetrans");
                
                LookupOperation lookup4 = Aggregation.lookup("transaction",// Join Table
	            "id",// Query table fields
	            "toId",// Join fields in tables
	            "recvtrans");
                
                LookupOperation lookup5 = Aggregation.lookup("event",// Join Table
	            "id",// Query table fields
	            "creatorId",// Join fields in tables
	            "events"); **/
                
                List<AggregationOperation> list = new ArrayList<AggregationOperation>();
		//list.add(Aggregation. .adlookup(from, localField, foreignField, as));
		list.add(Aggregation.lookup("event", "id", "creatorId", "events"));
				
		//list.add(Aggregation.match(Criteria.where("originid").is(originid)));
    	//list.add(Aggregation.sort(Sort.Direction.ASC, "created"));
		
		TypedAggregation<User> agg = Aggregation.newAggregation(User.class, list);
		return mongoTemplate.aggregate(agg, User.class).getMappedResults();
                
               // LookupOperation lookup = LookupOperation.newLookup().from("event").localField("id").foreignField("creatorId")
		//		.as("events");
                
                //ProjectionOperation po = Aggregation.project("events", "wallet", "id");
		
		//Aggregation aggregation =
	       //     Aggregation.newAggregation(User.class, lookup1,lookup2, lookup3, lookup4,lookup);
		
		//AggregationResults<UserDao> noRepeatDataInfoVos2 = 
                //   return  mongoTemplate.aggregate(aggregation, User.class, UserDao.class).getMappedResults();
                //List<UserDao> noRepeatDataList2 = noRepeatDataInfoVos2.getMappedResults();
                //List<User> noRepeatDataList2 = noRepeatDataInfoVos2.getMappedResults();
               // return noRepeatDataList2;
                //return noRepeatDataList2;
    }
    
}






















