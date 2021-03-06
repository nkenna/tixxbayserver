/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.UserDao;
import com.steinacoz.tixx.tixxbayserver.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
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
    public List<UserDao> aggregateAllUsers() {
                    
                List<AggregationOperation> list = new ArrayList<AggregationOperation>();
                list.add(Aggregation.lookup("userPoint", "username", "username", "userPoint"));
		list.add(Aggregation.lookup("event", "username", "creatorUsername", "events"));
		list.add(Aggregation.lookup("wallet", "walletId", "walletid", "wallet"));
		TypedAggregation<User> agg = Aggregation.newAggregation(User.class, list);
		return mongoTemplate.aggregate(agg, User.class, UserDao.class).getMappedResults();
                
    }

    @Override
    public UserDao getUserByEmail(String email) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("email").is(email));
        list.add(Aggregation.lookup("userPoint", "username", "username", "userPoint"));
	list.add(Aggregation.lookup("event", "username", "creatorUsername", "events"));
        list.add(Aggregation.lookup("wallet", "walletId", "walletid", "wallet"));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<User> agg = Aggregation.newAggregation(User.class, list);
	return mongoTemplate.aggregate(agg, User.class, UserDao.class).getUniqueMappedResult();
    }

    @Override
    public UserDao getUserByPhoneNumber(String phoneNumber) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("mobileNumber").is(phoneNumber));
        list.add(Aggregation.lookup("userPoint", "username", "username", "userPoint"));
	list.add(Aggregation.lookup("event", "username", "creatorUsername", "events"));
        list.add(Aggregation.lookup("wallet", "walletId", "walletid", "wallet"));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<User> agg = Aggregation.newAggregation(User.class, list);
	return mongoTemplate.aggregate(agg, User.class, UserDao.class).getUniqueMappedResult();
    }

    @Override
    public List<User> getUsersLast30days() {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.minusDays(21);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        MatchOperation match = Aggregation.match(Criteria.where("created").lt(now).andOperator(Criteria.where("created").gte(futureDate)));
        list.add(match);       
	TypedAggregation<User> agg = Aggregation.newAggregation(User.class, list);
	return mongoTemplate.aggregate(agg, User.class).getMappedResults();
    }
    
}



































