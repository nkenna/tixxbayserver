/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.EventCategoryDao;
import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.EventCategory;
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
public class EventCategoryRepoCustomImpl implements EventCategoryRepoCustom {
    
    private final MongoTemplate mongoTemplate;
    
    public EventCategoryRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<EventCategoryDao> getAllCategories() {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();        
        list.add(Aggregation.lookup("event", "id", "categoryId", "events"));               
	TypedAggregation<EventCategory> agg = Aggregation.newAggregation(EventCategory.class, list);
	return mongoTemplate.aggregate(agg, EventCategory.class, EventCategoryDao.class).getMappedResults();
    }
    
}


