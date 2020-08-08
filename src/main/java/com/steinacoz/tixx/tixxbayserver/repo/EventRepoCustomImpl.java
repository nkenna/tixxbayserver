/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
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
        LookupOperation lookup1 = Aggregation.lookup("user",// Join Table
	            "creatorId",// Query table fields
	            "id",// Join fields in tables
	            "createdBy");
		
		LookupOperation lookup2 = Aggregation.lookup("ticket",// Join Table
	            "eventId",// Query table fields
	            "id",// Join fields in tables
	            "tickets");
		
		TypedAggregation<Event> noRepeatAggregation2 =
	            Aggregation.newAggregation(Event.class, lookup1,lookup2);
		
		AggregationResults<EventDao> noRepeatDataInfoVos2 = mongoTemplate.aggregate(noRepeatAggregation2, EventDao.class);
                List<EventDao> noRepeatDataList2 = noRepeatDataInfoVos2.getMappedResults();
                return noRepeatDataList2;
    }    

    @Override
    public List<EventDao> aggregateAllEventsByCreator(String creatorId) {
        MatchOperation match = Aggregation.match(Criteria.where("creatorId").is(creatorId));
        LookupOperation lookup1 = Aggregation.lookup("user",// Join Table
	            "creatorId",// Query table fields
	            "id",// Join fields in tables
	            "createdBy");
		
		LookupOperation lookup2 = Aggregation.lookup("ticket",// Join Table
	            "eventId",// Query table fields
	            "id",// Join fields in tables
	            "tickets");
		
		TypedAggregation<Event> noRepeatAggregation2 =
	            Aggregation.newAggregation(Event.class, match, lookup1,lookup2);
		
		AggregationResults<EventDao> noRepeatDataInfoVos2 = mongoTemplate.aggregate(noRepeatAggregation2, EventDao.class);
                List<EventDao> noRepeatDataList2 = noRepeatDataInfoVos2.getMappedResults();
                return noRepeatDataList2;
    }
    
}







