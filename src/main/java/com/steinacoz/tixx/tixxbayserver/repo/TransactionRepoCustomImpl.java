/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.TransactionDao;
import com.steinacoz.tixx.tixxbayserver.model.Transaction;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

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
        LookupOperation lookup1 = Aggregation.lookup("tixxtag",// Join Table
	            "taguuid",// Query table fields
	            "taguuid",// Join fields in tables
	            "bands");
		
		LookupOperation lookup2 = Aggregation.lookup("user",// Join Table
	            "originId",// Query table fields
	            "_id",// Join fields in tables
	            "origin");
		
		LookupOperation lookup3 = Aggregation.lookup("user",// Join Table
	            "toId",// Query table fields
	            "_id",// Join fields in tables
	            "to");
		
		TypedAggregation<Transaction> noRepeatAggregation2 =
	            Aggregation.newAggregation(Transaction.class, lookup1,lookup2, lookup3);
		
		AggregationResults<TransactionDao> noRepeatDataInfoVos2 = mongoTemplate.aggregate(noRepeatAggregation2, TransactionDao.class);
        List<TransactionDao> noRepeatDataList2 = noRepeatDataInfoVos2.getMappedResults();
        return noRepeatDataList2;
    }

    @Override
    public List<TransactionDao> aggregateTransRef(String transRef) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}



