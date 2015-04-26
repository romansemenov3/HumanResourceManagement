/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.accessobjects;

import com.semenov.core.data.entities.Country;
import com.semenov.core.data.entities.Region;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


/**
 * The class <code>RegionFacade</code> represents REGION table CRUD
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@Stateless (name="regionOnline")
@LocalBean
public class RegionFacade implements CRUD<Region> {
    
    @PersistenceContext
    private EntityManager entityManager;
   
    /**
     * Gets list view for regions
     * @return regions list
     */
    public List<Region> list()
    {
        return entityManager.createNamedQuery("Region.findAll", Region.class).getResultList();
    }
    
    /**
     * Gets list view for regions in country
     * @param country - country with regions
     * @return regions in country list
     */
    public List<Region> list(Country country)
    {
        return entityManager.createNamedQuery("Region.findByCountryId", Region.class).
                setParameter("countryId", country).getResultList();
    }
    
	/**
	 * Gets page view of regions
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @return regions page
	 */
	public List<Region> page(Country country, int pageLength, int pageNumber) {
		TypedQuery<Region> query = entityManager.createNamedQuery("Region.findByCountryId", Region.class).setParameter("countryId", country);
		query.setFirstResult(pageLength*pageNumber);
		query.setMaxResults(pageLength);		
		return query.getResultList();
	}
    
    /**
     * Gets region by ID number
     * @param id - region ID number
     * @return region entity
     */
    public Region find(BigDecimal id) {
        return entityManager.find(Region.class, id);
    }
    
    /**
     * Adds region
     * @param region - region to add
     */
    public void add(Region region) {
        entityManager.persist(region);
    }
    
    /**
     * Updates region entity
     * @param region - region to update 
     */
    public void update(Region region) {
        entityManager.merge(region);
    }
    
    /**
     * Deletes region entity
     * @param region - region to delete
     */
    public void delete(Region region) {
        entityManager.remove(entityManager.contains(region) ? region : entityManager.merge(region));
    }
    
    /**
     * Gets total row amount
     * @param country - country with regions
     * @return total row amount
     */
	public long getRowCount(Country country) {
		return entityManager.createQuery("SELECT COUNT(r) FROM Region r WHERE r.countryId = :countryId", Long.class).
				setParameter("countryId", country).getSingleResult();
	}
}
