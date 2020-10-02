/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.TicketSaleTransaction;
import com.steinacoz.tixx.tixxbayserver.model.WalletTransaction;
import java.time.LocalDate;
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
public class WalletTransactionRepoCustomImpl implements WalletTransactionRepoCustom {
     private final MongoTemplate mongoTemplate;
	

    public WalletTransactionRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<WalletTransaction> getWTCreatedBy3wks() {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.minusDays(21);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        MatchOperation match = Aggregation.match(Criteria.where("transDate").lt(now).andOperator(Criteria.where("transDate").gte(futureDate)));
        list.add(match); 
        TypedAggregation<WalletTransaction> agg = Aggregation.newAggregation(WalletTransaction.class, list);
	return mongoTemplate.aggregate(agg, WalletTransaction.class).getMappedResults();
    }
}



