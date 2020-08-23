/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;


import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Location;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;


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
	list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }    

    @Override
    public List<EventDao> aggregateAllEventsByCreator(String username) {
        System.out.println(username);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        MatchOperation match = Aggregation.match(Criteria.where("creatorUsername").is(username));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
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
               .orOperator(Criteria.where("state").is(state))
       .orOperator(Criteria.where("lga").is(lga)));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }

    @Override
    public List<EventDao> aggregateAllEventsByUserGPSLocation(Point point) {
      List<AggregationOperation> list = new ArrayList<AggregationOperation>();
      NearQuery query = NearQuery.near(point).maxDistance(new Distance(10, Metrics.MILES));
      list.add(Aggregation.geoNear(query, "distance"));
       MatchOperation match = Aggregation.match((Criteria.where("status").is(true)));
       
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
        
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults(); 
    }

    @Override
    public List<EventDao> aggregateAllEventsByStatus() {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("status").is(true));
	list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
        TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }

    @Override
    public EventDao getEventByEventCode(String eventCode) {
       System.out.println(eventCode);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        MatchOperation match = Aggregation.match(Criteria.where("eventCode").is(eventCode));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getUniqueMappedResult();
    }
    
}






























