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
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        list.add(Aggregation.lookup("childTicket", "ticketCode", "parentTicketCode", "childTickets"));
        //list.add(Aggregation.match(Criteria.where("customerid").is(customerid)));
    	//list.add(Aggregation.sort(Sort.Direction.ASC, "created"));
	TypedAggregation<Ticket> agg = Aggregation.newAggregation(Ticket.class, list);
	return mongoTemplate.aggregate(agg, Ticket.class, TicketDao.class).getMappedResults();
    }

    @Override
    public List<TicketDao> aggregateAllTicketCategoriesByEvent(String eventCode) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
	list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("childTicket", "ticketCode", "parentTicketCode", "childTickets"));
        list.add(Aggregation.match(Criteria.where("eventCode").is(eventCode)));
    	//list.add(Aggregation.sort(Sort.Direction.ASC, "created"));
	TypedAggregation<Ticket> agg = Aggregation.newAggregation(Ticket.class, list);
	return mongoTemplate.aggregate(agg, Ticket.class, TicketDao.class).getMappedResults();
    }

    @Override
    public TicketDao getTicketCategory(String ticketCode) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
	list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("childTicket", "ticketCode", "parentTicketCode", "childTickets"));
        list.add(Aggregation.match(Criteria.where("ticketCode").is(ticketCode)));
    	//list.add(Aggregation.sort(Sort.Direction.ASC, "created"));
	TypedAggregation<Ticket> agg = Aggregation.newAggregation(Ticket.class, list);
	return mongoTemplate.aggregate(agg, Ticket.class, TicketDao.class).getUniqueMappedResult();
    }

    @Override
    public TicketDao getTicketCategoryByChildTicket(String ticketCode) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
	list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("childTicket", "ticketCode", "parentTicketCode", "childTickets"));
        list.add(Aggregation.match(Criteria.where("ticketCode").is(ticketCode)));
    	//list.add(Aggregation.sort(Sort.Direction.ASC, "created"));
	TypedAggregation<Ticket> agg = Aggregation.newAggregation(Ticket.class, list);
	return mongoTemplate.aggregate(agg, Ticket.class, TicketDao.class).getUniqueMappedResult();
    }

    @Override
    public List<TicketDao> findAllTicketsABoutToStart() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(5);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        MatchOperation match = Aggregation.match(Criteria.where("saleStartDay").lt(futureDate).andOperator(Criteria.where("saleStartDay").gte(now), Criteria.where("haveOrdered").gte(false) ,Criteria.where("status").is(true)));
        list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("childTicket", "ticketCode", "parentTicketCode", "childTickets"));
        list.add(match);       
	TypedAggregation<Ticket> agg = Aggregation.newAggregation(Ticket.class, list);
	return mongoTemplate.aggregate(agg, Ticket.class, TicketDao.class).getMappedResults(); 
    }

    @Override
    public List<Ticket> findAllExpiredTickets() {
        LocalDateTime now = LocalDateTime.now();
        //LocalDateTime futureDate = now.plusDays(5);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        MatchOperation match = Aggregation.match(Criteria.where("saleEndDay").gt(now));
        //list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        //list.add(Aggregation.lookup("childTicket", "ticketCode", "parentTicketCode", "childTickets"));
        list.add(match);       
	TypedAggregation<Ticket> agg = Aggregation.newAggregation(Ticket.class, list);
	return mongoTemplate.aggregate(agg, Ticket.class).getMappedResults(); 
    }

    @Override
    public List<TicketDao> getTicketsByEventCreatorNFC(String eventCode) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        MatchOperation match = Aggregation.match(Criteria.where("eventCode").is(eventCode).andOperator(Criteria.where("ticketType").is("NFC")));
        //list.add(Aggregation.lookup("event", "eventCode", "eventCode", "event"));
        list.add(Aggregation.lookup("childTicket", "ticketCode", "parentTicketCode", "childTickets"));
        list.add(match);       
	TypedAggregation<Ticket> agg = Aggregation.newAggregation(Ticket.class, list);
	return mongoTemplate.aggregate(agg, Ticket.class, TicketDao.class).getMappedResults(); 
    }
}


















