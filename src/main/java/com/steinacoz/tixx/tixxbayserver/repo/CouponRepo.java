/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.model.Coupon;
import com.steinacoz.tixx.tixxbayserver.model.EventCategory;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author nkenn
 */
@RepositoryRestResource(collectionResourceRel = "coupon", path = "coupon")
public interface CouponRepo extends MongoRepository<Coupon, String> {
    List<Coupon> findByTicketCode(String ticketCode);
    List<Coupon> findByEventCode(String eventCode);
    Coupon findByCode(String code);
    List<Coupon> findByUsed(boolean used);
}



