/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.SaleTransactionDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketSaleTransactionDao;
import com.steinacoz.tixx.tixxbayserver.model.SaleTransaction;
import com.steinacoz.tixx.tixxbayserver.model.TicketSaleTransaction;
import java.time.LocalDate;
import java.time.YearMonth;
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
public class SaleTransactionRepoCustomImpl implements SaleTransactionRepoCustom{
    private final MongoTemplate mongoTemplate;
    
    public SaleTransactionRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<SaleTransactionDao> getAllSaleTrans() {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
	list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        TypedAggregation<SaleTransaction> agg = Aggregation.newAggregation(SaleTransaction.class, list);
	return mongoTemplate.aggregate(agg, SaleTransaction.class, SaleTransactionDao.class).getMappedResults();
    }

    @Override
    public List<SaleTransactionDao> getAllSaleTransByMonth(LocalDate date, String eventCode) {
        YearMonth month = YearMonth.from(date);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match1 = Aggregation.match(Criteria.where("eventCode").is(eventCode));
        MatchOperation match2 = Aggregation.match(Criteria.where("transDate").lt(month.atEndOfMonth()).andOperator(Criteria.where("transDate").gt(month.atDay(1))));
	list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        TypedAggregation<SaleTransaction> agg = Aggregation.newAggregation(SaleTransaction.class, list);
	return mongoTemplate.aggregate(agg, SaleTransaction.class, SaleTransactionDao.class).getMappedResults();
    }

    @Override
    public SaleTransactionDao getSaleTrans(String transRef) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("transRef").is(transRef));
	list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        TypedAggregation<SaleTransaction> agg = Aggregation.newAggregation(SaleTransaction.class, list);
	return mongoTemplate.aggregate(agg, SaleTransaction.class, SaleTransactionDao.class).getUniqueMappedResult();
    }
    
}




