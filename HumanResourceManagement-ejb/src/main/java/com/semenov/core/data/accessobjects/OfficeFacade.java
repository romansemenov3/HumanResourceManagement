/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.accessobjects;

import com.semenov.core.data.entities.Country;
import com.semenov.core.data.entities.Office;
import com.semenov.core.data.entities.Region;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


/**
 * The class <code>OfficeFacade</code> represents OFFICE table CRUD
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@Stateless (name="officeOnline")
@LocalBean
public class OfficeFacade implements CRUD<Office> {
    
    @PersistenceContext
    private EntityManager entityManager;
   
    /**
     * Gets list view for offices
     * @return offices list
     */
    public List<Office> list()
    {
        return entityManager.createNamedQuery("Office.findAll", Office.class).getResultList();
    }
    
    /**
     * Gets list view for offices in region
     * @param region - region with offices
     * @return offices in region list
     */
    public List<Office> list(Region region)
    {
        return entityManager.createNamedQuery("Office.findByRegionId", Office.class).
                setParameter("regionId", region).getResultList();
    }
    
	/**
	 * Gets page view of offices
	 * @param pageLength - page length
	 * @param pageNumber - page number
	 * @return offices page
	 */
	public List<Office> page(Region region, int pageLength, int pageNumber) {
		TypedQuery<Office> query = entityManager.createNamedQuery("Office.findByRegionId", Office.class).setParameter("regionId", region);
		query.setFirstResult(pageLength*pageNumber);
		query.setMaxResults(pageLength);		
		return query.getResultList();
	}
    
    /**
     * Gets country by ID number
     * @param id - country ID number
     * @return country entity
     */
    public Office find(BigDecimal id) {
        return entityManager.find(Office.class, id);
    }
    
    /**
     * Adds country
     * @param office - office to add
     */
    public void add(Office office) {
        entityManager.persist(office);
    }
    
    /**
     * Updates office entity
     * @param office - office to update 
     */
    public void update(Office office) {
        entityManager.merge(office);
    }
    
    /**
     * Deletes office entity
     * @param office - office to delete
     */
    public void delete(Office office) {
        entityManager.remove(entityManager.contains(office) ? office : entityManager.merge(office));
    }
    
    /**
     * Gets total row amount
     * @param region - regions with offices
     * @return total row amount
     */
	public long getRowCount(Region region) {
		return entityManager.createQuery("SELECT COUNT(o) FROM Office o WHERE o.regionId = :regionId", Long.class).
				setParameter("regionId", region).getSingleResult();
	}
}
