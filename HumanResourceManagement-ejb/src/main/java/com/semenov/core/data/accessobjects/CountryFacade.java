/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.accessobjects;

import com.semenov.core.data.entities.Country;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * The class <code>CountryFacade</code> represents COUNTRY table CRUD
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@Stateless (name="countryOnline")
@LocalBean
public class CountryFacade {
    
    @PersistenceContext
    private EntityManager entityManager;
   
    /**
     * Gets list view for countries
     * @return countries list
     */
    public List<Country> list()
    {
        return entityManager.createNamedQuery("Country.findAll", Country.class).getResultList();
    }
    
    /**
     * Gets country by ID number
     * @param id - country ID number
     * @return country entity
     */
    public Country find(BigDecimal id) {
        return entityManager.find(Country.class, id);
    }
    
    /**
     * Adds country
     * @param country - country to add
     */
    public void add(Country country) {
        entityManager.persist(country);
    }
    
    /**
     * Updates country entity
     * @param country - country to update 
     */
    public void update(Country country) {
        entityManager.merge(country);
    }
    
    /**
     * Deletes country entity
     * @param country - country to delete
     */
    public void delete(Country country) {
        entityManager.remove(entityManager.contains(country) ? country : entityManager.merge(country));
    }
}
