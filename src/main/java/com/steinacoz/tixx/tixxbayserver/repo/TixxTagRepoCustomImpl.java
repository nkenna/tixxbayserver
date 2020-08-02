/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.TixxTagDao;
import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
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
public class TixxTagRepoCustomImpl implements TixxTagRepoCustom {
    
    private final MongoTemplate mongoTemplate;
	

    public TixxTagRepoCustomImpl(MongoTemplate mongoTemplate) {
	super();
	this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<TixxTagDao> aggregateAlltags() {
        LookupOperation lookup1 = Aggregation.lookup("user",// Join Table
	            "addedById",// Query table fields
	            "id",// Join fields in tables
	            "addedBy");
		
		LookupOperation lookup2 = Aggregation.lookup("user",// Join Table
	            "updatedById",// Query table fields
	            "id",// Join fields in tables
	            "updatedBy");
		
		LookupOperation lookup3 = Aggregation.lookup("user",// Join Table
	            "wornedById",// Query table fields
	            "id",// Join fields in tables
	            "wornBy");
		
		TypedAggregation<TixxTag> noRepeatAggregation2 =
	            Aggregation.newAggregation(TixxTag.class, lookup1,lookup2, lookup3);
		
		AggregationResults<TixxTagDao> noRepeatDataInfoVos2 = mongoTemplate.aggregate(noRepeatAggregation2, TixxTagDao.class);
        List<TixxTagDao> noRepeatDataList2 = noRepeatDataInfoVos2.getMappedResults();
        return noRepeatDataList2;
    }

    @Override
    public TixxTagDao aggrefindByTagUuid(String tagUuid) {
        LookupOperation lookup1 = Aggregation.lookup("user",// Join Table
	            "addedById",// Query table fields
	            "id",// Join fields in tables
	            "addedBy");
		
		LookupOperation lookup2 = Aggregation.lookup("user",// Join Table
	            "updatedById",// Query table fields
	            "id",// Join fields in tables
	            "updatedBy");
		
		LookupOperation lookup3 = Aggregation.lookup("user",// Join Table
	            "wornedById",// Query table fields
	            "id",// Join fields in tables
	            "wornBy");
                
                MatchOperation match = Aggregation.match(Criteria.where("taguuid").is(tagUuid));
		
		TypedAggregation<TixxTag> noRepeatAggregation2 =
	            Aggregation.newAggregation(TixxTag.class, match, lookup1,lookup2, lookup3);
                
                AggregationResults<TixxTagDao> noRepeatDataInfoVos2 = mongoTemplate.aggregate(noRepeatAggregation2, TixxTagDao.class);
                TixxTagDao noRepeatDataList2 = noRepeatDataInfoVos2.getUniqueMappedResult();
                return noRepeatDataList2;
    }
    
}





