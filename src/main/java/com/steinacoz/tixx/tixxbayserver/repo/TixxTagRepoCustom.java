/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.repo;

import com.steinacoz.tixx.tixxbayserver.dao.TixxTagDao;
import com.steinacoz.tixx.tixxbayserver.model.TixxTag;
import java.util.List;

/**
 *
 * @author nkenn
 */
public interface TixxTagRepoCustom {
    List<TixxTagDao> aggregateAlltags();
    TixxTagDao aggrefindByTagUuid(String tagUuid);
}





