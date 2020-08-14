/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.BankDetailDao;
import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.BankDetail;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 *
 * @author nkenn
 */
public class BankDetailRepoCustomImpl implements BankDetailRepoCustom{
    private final MongoTemplate mongoTemplate;
    
    public BankDetailRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<BankDetailDao> getAllBankDetails() {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        //MatchOperation match = Aggregation.match(Criteria.where("creatorUsername").is(username));
        list.add(Aggregation.lookup("user", "ownerUsername", "username", "user"));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        //list.add(match);
       
	TypedAggregation<BankDetail> agg = Aggregation.newAggregation(BankDetail.class, list);
	return mongoTemplate.aggregate(agg, BankDetail.class, BankDetailDao.class).getMappedResults();
    }

    @Override
    public List<BankDetailDao> getAllBankDetailsByOwnerId(String ownerid) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("ownerId").is(ownerid));
        list.add(Aggregation.lookup("user", "ownerUsername", "username", "user"));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<BankDetail> agg = Aggregation.newAggregation(BankDetail.class, list);
	return mongoTemplate.aggregate(agg, BankDetail.class, BankDetailDao.class).getMappedResults();
    }

    @Override
    public List<BankDetailDao> getAllBankDetailsByOwnerUsername(String username) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("ownerUsername").is(username));
        list.add(Aggregation.lookup("user", "ownerUsername", "username", "user"));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<BankDetail> agg = Aggregation.newAggregation(BankDetail.class, list);
	return mongoTemplate.aggregate(agg, BankDetail.class, BankDetailDao.class).getMappedResults();
    }

    @Override
    public List<BankDetailDao> getAllBankDetailsByAccountNumber(String number) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("accountNumber").is(number));
        list.add(Aggregation.lookup("user", "ownerUsername", "username", "user"));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<BankDetail> agg = Aggregation.newAggregation(BankDetail.class, list);
	return mongoTemplate.aggregate(agg, BankDetail.class, BankDetailDao.class).getMappedResults();
    }

    @Override
    public List<BankDetailDao> getAllBankDetailsByAccountName(String name) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("accountName").is(name));
        list.add(Aggregation.lookup("user", "ownerUsername", "username", "user"));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<BankDetail> agg = Aggregation.newAggregation(BankDetail.class, list);
	return mongoTemplate.aggregate(agg, BankDetail.class, BankDetailDao.class).getMappedResults();
    }
    
}




