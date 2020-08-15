/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.BankDetailDao;
import com.steinacoz.tixx.tixxbayserver.dao.TransactionDao;
import com.steinacoz.tixx.tixxbayserver.model.BankDetail;
import com.steinacoz.tixx.tixxbayserver.model.Transaction;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 *
 * @author nkenn
 */
public class TransactionRepoCustomImpl implements TransactionRepoCustom  {
    private final MongoTemplate mongoTemplate;
	

    public TransactionRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<TransactionDao> aggregateAll() {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        //MatchOperation match = Aggregation.match(Criteria.where("creatorUsername").is(username));
        list.add(Aggregation.lookup("user", "usernameFrom", "username", "user"));
        list.add(Aggregation.lookup("user", "usernameTo", "username", "user"));
        list.add(Aggregation.lookup("user", "usernameFrom", "username", "user"));
        list.add(Aggregation.lookup("tixxtag", "taguuid", "taguuid", "tixxTags"));
        list.add(Aggregation.lookup("wallet", "walletid", "walletId", "wallet"));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        //list.add(match);
       
	TypedAggregation<Transaction> agg = Aggregation.newAggregation(Transaction.class, list);
	return mongoTemplate.aggregate(agg, Transaction.class, TransactionDao.class).getMappedResults();
        
    }

    @Override
    public List<TransactionDao> aggregateTransRef(String transRef) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("transRef").is(transRef));
        list.add(Aggregation.lookup("user", "usernameFrom", "username", "user"));
        list.add(Aggregation.lookup("user", "usernameTo", "username", "user"));
        list.add(Aggregation.lookup("user", "usernameFrom", "username", "user"));
        list.add(Aggregation.lookup("tixxtag", "taguuid", "taguuid", "tixxTags"));
        list.add(Aggregation.lookup("wallet", "walletid", "walletId", "wallet"));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<Transaction> agg = Aggregation.newAggregation(Transaction.class, list);
	return mongoTemplate.aggregate(agg, Transaction.class, TransactionDao.class).getMappedResults();
    }

    @Override
    public List<TransactionDao> getTransByMonth(String transRef) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("transRef").is(transRef));
        list.add(Aggregation.lookup("user", "usernameFrom", "username", "user"));
        list.add(Aggregation.lookup("user", "usernameTo", "username", "user"));
        list.add(Aggregation.lookup("user", "usernameFrom", "username", "user"));
        list.add(Aggregation.lookup("tixxtag", "taguuid", "taguuid", "tixxTags"));
        list.add(Aggregation.lookup("wallet", "walletid", "walletId", "wallet"));
        list.add(Aggregation.project("").and(DateOperators.Month.month("transDate")));
        //list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<Transaction> agg = Aggregation.newAggregation( Transaction.class, list);
	return mongoTemplate.aggregate(agg, Transaction.class, TransactionDao.class).getMappedResults();
    }
    
}








