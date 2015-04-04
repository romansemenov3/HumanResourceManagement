/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.accessobjects;

import com.semenov.core.data.entities.Office;
import com.semenov.core.data.entities.Staff;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * The class <code>StaffFacade</code> represents STAFF table CRUD
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@Stateless (name="staffOnline")
@LocalBean
public class StaffFacade implements CRUD<Staff> {
    
    @PersistenceContext
    private EntityManager entityManager;
   
    /**
     * Gets list view for staff
     * @return staff list
     */
    public List<Staff> list()
    {
        return entityManager.createNamedQuery("Staff.findAll", Staff.class).getResultList();
    }
    
    /**
     * Gets list view for staff in office
     * @param office - office with staff
     * @return staff in office list
     */
    public List<Staff> list(Office office)
    {
        return entityManager.createNamedQuery("Staff.findByOfficeId", Staff.class).
                setParameter("officeId", office).getResultList();
    }
    
    /**
     * Gets staff by ID number
     * @param id - staff ID number
     * @return staff entity
     */
    public Staff find(BigDecimal id) {
        return entityManager.find(Staff.class, id);
    }
    
    /**
     * Adds staff
     * @param staff - staff to add
     */
    public void add(Staff staff) {
        entityManager.persist(staff);
    }
    
    /**
     * Updates staff entity
     * @param staff - staff to update 
     */
    public void update(Staff staff) {
        entityManager.merge(staff);
    }
    
    /**
     * Deletes staff entity
     * @param staff - staff to delete
     */
    public void delete(Staff staff) {
        entityManager.remove(entityManager.contains(staff) ? staff : entityManager.merge(staff));
    }
}
