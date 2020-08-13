/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.mongodb.client.model.geojson.Point;
import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Location;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 *
 * @author nkenn
 */
public class EventRepoCustomImpl implements EventRepoCustom {
    private final MongoTemplate mongoTemplate;
    
    public EventRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<EventDao> aggregateAllEvents() {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
		//list.add(Aggregation. .adlookup(from, localField, foreignField, as));
		list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
                list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
                
		//list.add(Aggregation.match(Criteria.where("customerid").is(customerid)));
    	//list.add(Aggregation.sort(Sort.Direction.ASC, "created"));
		
		TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
		return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }    

    @Override
    public List<EventDao> aggregateAllEventsByCreator(String creatorId) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("creatorUsername").is(creatorId));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }

    @Override
    public List<EventDao> aggregateAllEventsByState(String state) {
      List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("state").is(state));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();  
    }

    @Override
    public List<EventDao> aggregateAllEventsByLga(String lga) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
       MatchOperation match = Aggregation.match(Criteria.where("lga").is(lga));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults(); 
    }

    @Override
    public List<EventDao> aggregateAllEventsByCountry(String country) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
       MatchOperation match = Aggregation.match(Criteria.where("country").is(country));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults(); 
    }

    @Override
    public List<EventDao> aggregateAllEventsByUserLocation(String country, String state, String lga) {
        
       List<AggregationOperation> list = new ArrayList<AggregationOperation>();
       MatchOperation match = Aggregation.match(Criteria.where("country").is(country)
               .orOperator(Criteria.where("country").is(country))
       .orOperator(Criteria.where("country").is(country)));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }

    @Override
    public List<EventDao> aggregateAllEventsByUserGPSLocation(Location location) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}


















