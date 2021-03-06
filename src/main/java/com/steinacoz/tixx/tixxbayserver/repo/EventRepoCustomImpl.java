/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;


import com.steinacoz.tixx.tixxbayserver.CustomProjectAggregationOperation;
import com.steinacoz.tixx.tixxbayserver.dao.EventDao;
import com.steinacoz.tixx.tixxbayserver.model.Event;
import com.steinacoz.tixx.tixxbayserver.model.Location;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
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
        LocalDateTime now = LocalDateTime.now();
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        //MatchOperation match = Aggregation.match(Criteria.where("endDate").nlt(now).andOperator(Criteria.where("status").is(true)));
	list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
       
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        //list.add(new CustomProjectAggregationOperation(query));
        list.add(sortByPopDesc);
        TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
        
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }    

    @Override
    public List<EventDao> aggregateAllEventsByCreator(String username) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(username); //childTicket
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        //MatchOperation match = Aggregation.match(Criteria.where("creatorUsername").is(username).andOperator(Criteria.where("endDate").gte(now), Criteria.where("status").is(true)));
        MatchOperation match = Aggregation.match(Criteria.where("creatorUsername").is(username).andOperator(Criteria.where("status").is(true)));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
        list.add(sortByPopDesc);
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }

    @Override
    public List<EventDao> aggregateAllEventsByState(String state) {
        LocalDateTime now = LocalDateTime.now();
      List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        //MatchOperation match = Aggregation.match(Criteria.where("state").is(state).andOperator(Criteria.where("endDate").gte(now), Criteria.where("status").is(true)));
        MatchOperation match = Aggregation.match(Criteria.where("state").is(state).andOperator(Criteria.where("status").is(true)));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
        list.add(sortByPopDesc);
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();  
    }

    @Override
    public List<EventDao> aggregateAllEventsByLga(String lga) {
        LocalDateTime now = LocalDateTime.now();
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        //MatchOperation match = Aggregation.match(Criteria.where("lga").is(lga).andOperator(Criteria.where("endDate").gte(now), Criteria.where("status").is(true)));
        MatchOperation match = Aggregation.match(Criteria.where("lga").is(lga).andOperator(Criteria.where("status").is(true)));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(match);
        list.add(sortByPopDesc);
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults(); 
    }

    @Override
    public List<EventDao> aggregateAllEventsByCountry(String country) {
        LocalDateTime now = LocalDateTime.now();
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        //MatchOperation match = Aggregation.match(Criteria.where("country").is(country).andOperator(Criteria.where("endDate").gte(now), Criteria.where("status").is(true)));
        MatchOperation match = Aggregation.match(Criteria.where("country").is(country).andOperator(Criteria.where("status").is(true)));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(match);
        list.add(sortByPopDesc);
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults(); 
    }

    @Override
    public List<EventDao> aggregateAllEventsByUserLocation(String country, String state, String lga) {
        LocalDateTime now = LocalDateTime.now();
       List<AggregationOperation> list = new ArrayList<AggregationOperation>();
       SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
       MatchOperation match = Aggregation.match(Criteria.where("country").is(country)
               .orOperator(Criteria.where("state").is(state), Criteria.where("lga").is(lga)));
        MatchOperation match2 = Aggregation.match((Criteria.where("status").is(true)));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(match);
        list.add(match2);
        list.add(sortByPopDesc);
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }

    @Override
    public List<EventDao> aggregateAllEventsByUserGPSLocation(Point point) {
        LocalDateTime now = LocalDateTime.now();
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
      List<AggregationOperation> list = new ArrayList<AggregationOperation>();
      NearQuery query = NearQuery.near(point).maxDistance(new Distance(10, Metrics.MILES));
      list.add(Aggregation.geoNear(query, "distance"));
      //MatchOperation match = Aggregation.match(Criteria.where("status").is(true).andOperator(Criteria.where("endDate").gte(now)));
      MatchOperation match = Aggregation.match(Criteria.where("status").is(true));
    
       //MatchOperation match = Aggregation.match((Criteria.where("status").is(true).andOperator(Criteria.where("endDate").gt(now))));
       
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);    
        list.add(sortByPopDesc);
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults(); 
    }

    @Override
    public List<EventDao> aggregateAllEventsByStatus() {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        MatchOperation match = Aggregation.match(Criteria.where("status").is(true));
	list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
        list.add(sortByPopDesc);
        TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }

    @Override
    public EventDao getEventByEventCode(String eventCode) {
       System.out.println(eventCode);
       LocalDateTime now = LocalDateTime.now();
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        MatchOperation match = Aggregation.match(Criteria.where("eventCode").is(eventCode).andOperator(Criteria.where("status").is(true)));
        //MatchOperation match = Aggregation.match(Criteria.where("eventCode").is(eventCode).andOperator(Criteria.where("endDate").gte(now), Criteria.where("status").is(true)));
        //MatchOperation match = Aggregation.match(Criteria.where("eventCode").is(eventCode).andOperator(Criteria.where("endDate").gt(now)).andOperator(Criteria.where("status").is(true)));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);  
        list.add(sortByPopDesc);
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getUniqueMappedResult();
    }

    @Override
    public List<EventDao> aggregateAllEventsBy3Weeks() {
        LocalDate now = LocalDate.now();
        LocalDate nowDate = now.minusDays(365);
        LocalDate futureDate = now.plusDays(200);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        MatchOperation match = Aggregation.match(Criteria.where("startDate").lt(futureDate).andOperator(Criteria.where("startDate").gte(nowDate), Criteria.where("status").is(true)));
        
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match); 
        list.add(sortByPopDesc);
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults(); 
    }

    @Override
    public List<EventDao> aggregateAllEventsByShuffle() {
        LocalDateTime now = LocalDateTime.now();        
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        //SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        //MatchOperation match = Aggregation.match(Criteria.where("endDate").gte(now).andOperator(Criteria.where("status").is(true)));
        //MatchOperation match = Aggregation.match(Criteria.where("endDate").gte(now));
        SampleOperation matchStage = Aggregation.sample(50);
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
       // list.add(match);       
        list.add(matchStage);
        //list.add(sortByPopDesc);
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults(); 
    }

    @Override
    public EventDao getEventByVendor(String eventCode) {
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        MatchOperation match = Aggregation.match(Criteria.where("eventCode").is(eventCode));
	list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(match);
        list.add(sortByPopDesc);
        TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getUniqueMappedResult();
    }

    @Override
    public List<EventDao> aggregateAllEventsByName(String title) {
        LocalDateTime now = LocalDateTime.now();
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        MatchOperation match = Aggregation.match(Criteria.where("title").regex(title, "i").orOperator(Criteria.where("lga").regex(title, "i"), Criteria.where("state").regex(title, "i"), Criteria.where("country").regex(title, "i")));
        list.add(Aggregation.lookup("childTicket", "eventCode", "eventCode", "childtickets"));
        list.add(Aggregation.lookup("user", "creatorUsername", "username", "createdBy"));
        list.add(Aggregation.lookup("eventTeam", "eventCode", "eventCode", "teams"));
        list.add(Aggregation.lookup("ticket", "eventCode", "eventCode", "tickets"));
        list.add(match);
      
       
	TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class, EventDao.class).getMappedResults();
    }

    @Override
    public List<Event> getEventsCreatedBy3wks() {
        LocalDate now = LocalDate.now();
        
        LocalDate futureDate = now.minusDays(400);
        List<AggregationOperation> list = new ArrayList<AggregationOperation>();//eventCode
        SortOperation sortByPopDesc = Aggregation.sort(Sort.by(Direction.ASC, "startDate"));
        MatchOperation match = Aggregation.match(Criteria.where("startDate").lt(now).andOperator(Criteria.where("startDate").gte(futureDate)));
        
        list.add(match); 
        TypedAggregation<Event> agg = Aggregation.newAggregation(Event.class, list);
	return mongoTemplate.aggregate(agg, Event.class).getMappedResults(); 
    }

    
    
    
    
}





















































































