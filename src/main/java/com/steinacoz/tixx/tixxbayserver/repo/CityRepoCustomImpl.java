/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.CityDao;
import com.steinacoz.tixx.tixxbayserver.dao.StateDao;
import com.steinacoz.tixx.tixxbayserver.model.City;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.State;
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
public class CityRepoCustomImpl implements CityRepoCustom {
    private final MongoTemplate mongoTemplate;
    
    public CityRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<CityDao> getAllCities() {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        //MatchOperation match3 = Aggregation.match(Criteria.where("endDate").is(now).andOperator(Criteria.where("status").is(true)));
	list.add(Aggregation.lookup("state", "state", "name", "stateData"));        
        TypedAggregation<City> agg = Aggregation.newAggregation(City.class, list);
	return mongoTemplate.aggregate(agg, City.class, CityDao.class).getMappedResults();
    }

    @Override
    public CityDao getCityByName(String name) {
       List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        MatchOperation match = Aggregation.match(Criteria.where("name").is(name));
	list.add(Aggregation.lookup("state", "state", "name", "stateData"));  
        list.add(match);
        TypedAggregation<City> agg = Aggregation.newAggregation(City.class, list);
	return mongoTemplate.aggregate(agg, City.class, CityDao.class).getUniqueMappedResult(); 
    }
    
}





