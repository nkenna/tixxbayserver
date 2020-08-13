/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.dao.TicketDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Ticket;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

/**
 *
 * @author nkenn
 */
public class TicketRepoCustomImpl implements TicketRepoCustom{
    private final MongoTemplate mongoTemplate;
    
    public TicketRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<TicketDao> aggregateAllTicketCategories() {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
	list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("childticket", "ticketCode", "parentTicketCode", "childTickets"));
        //list.add(Aggregation.match(Criteria.where("customerid").is(customerid)));
    	//list.add(Aggregation.sort(Sort.Direction.ASC, "created"));
	TypedAggregation<Ticket> agg = Aggregation.newAggregation(Ticket.class, list);
	return mongoTemplate.aggregate(agg, Ticket.class, TicketDao.class).getMappedResults();
    }
}






