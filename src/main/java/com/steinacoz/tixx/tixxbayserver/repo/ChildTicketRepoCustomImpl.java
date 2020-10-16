/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.ChildTicketDao;
import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.ChildTicket;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 *
 * @author nkenn
 */
public class ChildTicketRepoCustomImpl implements ChildTicketRepoCustom{
    private final MongoTemplate mongoTemplate;
    
    public ChildTicketRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ChildTicketDao getChildTicketByTicketCode(String ticketCode) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("ticketCode").is(ticketCode));
        list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("ticket", "parentTicketCode", "ticketCode", "parentTicketData"));
        list.add(match);
       
	TypedAggregation<ChildTicket> agg = Aggregation.newAggregation(ChildTicket.class, list);
	return mongoTemplate.aggregate(agg, ChildTicket.class, ChildTicketDao.class).getUniqueMappedResult();
    }

    @Override
    public ChildTicketDao getChildTicketByEventCode(String eventCode) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("eventCode").is(eventCode));
        list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("ticket", "parentTicketCode", "ticketCode", "parentTicketData"));
        list.add(match);
       
	TypedAggregation<ChildTicket> agg = Aggregation.newAggregation(ChildTicket.class, list);
	return mongoTemplate.aggregate(agg, ChildTicket.class, ChildTicketDao.class).getUniqueMappedResult();
    }

   @Override
   public List<ChildTicketDao> getChildTicketsByUsername(String username) {
       List<AggregationOperation> list = new ArrayList<AggregationOperation>();
       SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "created"));
        MatchOperation match = Aggregation.match(Criteria.where("boughtByUsername").is(username));
        list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("ticket", "parentTicketCode", "ticketCode", "parentTicketData"));
        //list.add(Aggregation.lookup("user", "boughtByUsername", "username", "user"));
        //list.add(Aggregation..unwind("user"));
        //list.add(Aggregation.project("username"));
        list.add(match);
        list.add(sortByPopDesc);
       
	TypedAggregation<ChildTicket> agg = Aggregation.newAggregation(ChildTicket.class, list);
	return mongoTemplate.aggregate(agg, ChildTicket.class, ChildTicketDao.class).getMappedResults();
    }

    @Override
    public List<ChildTicketDao> getChildTicketsByParentCode(String ticketCode) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("parentTicketCode").is(ticketCode));
        list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("ticket", "parentTicketCode", "ticketCode", "parentTicketData"));
       
        list.add(match);
       
	TypedAggregation<ChildTicket> agg = Aggregation.newAggregation(ChildTicket.class, list);
	return mongoTemplate.aggregate(agg, ChildTicket.class, ChildTicketDao.class).getMappedResults();
    }
    
    
}















