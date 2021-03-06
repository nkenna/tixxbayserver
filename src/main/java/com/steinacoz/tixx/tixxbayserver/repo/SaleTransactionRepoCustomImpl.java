/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.SaleTransactionDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketSaleTransactionDao;
import com.steinacoz.tixx.tixxbayserver.model.EventTeam;
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
        System.out.println(month);
        LocalDate end = month.atEndOfMonth();
        LocalDate start = month.atDay(1);
        
        System.out.println(start);        
        System.out.println(end);
        
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        //MatchOperation match1 = Aggregation.match();
        MatchOperation match2 = Aggregation.match(
                Criteria.where("eventCode").is(eventCode)
                .andOperator(
                        Criteria.where("transDate").lt(end),
                        Criteria.where("transDate").gt(start)
                )
            );
	list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("tixxTag", "taguuid", "taguuid", "tag"));
        list.add(match2);
        //list.add(Aggregation.lookup("user", "eventCode", "eventCode", "event"));
        TypedAggregation<SaleTransaction> agg = Aggregation.newAggregation(SaleTransaction.class, list);
	return mongoTemplate.aggregate(agg, SaleTransaction.class, SaleTransactionDao.class).getMappedResults();
    }

    @Override
    public SaleTransactionDao getSaleTrans(String transRef) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("transRef").is(transRef));
	list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(match);
        TypedAggregation<SaleTransaction> agg = Aggregation.newAggregation(SaleTransaction.class, list);
	return mongoTemplate.aggregate(agg, SaleTransaction.class, SaleTransactionDao.class).getUniqueMappedResult();
    }

    @Override
    public List<SaleTransactionDao> getAllTicketSaleByEventCode(String eventCode) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match1 = Aggregation.match(Criteria.where("eventCode").is(eventCode));
        list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(match1);
        TypedAggregation<SaleTransaction> agg = Aggregation.newAggregation(SaleTransaction.class, list);
	return mongoTemplate.aggregate(agg, SaleTransaction.class, SaleTransactionDao.class).getMappedResults();
    }

    @Override
    public List<SaleTransaction> getSTCreatedBy3wks() {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.minusDays(21);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        MatchOperation match = Aggregation.match(Criteria.where("transDate").lt(now).andOperator(Criteria.where("transDate").gte(futureDate)));
        
        list.add(match); 
        TypedAggregation<SaleTransaction> agg = Aggregation.newAggregation(SaleTransaction.class, list);
	return mongoTemplate.aggregate(agg, SaleTransaction.class).getMappedResults();
    }
    
}










