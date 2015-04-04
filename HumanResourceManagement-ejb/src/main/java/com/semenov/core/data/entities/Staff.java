/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roman
 */
@Entity
@Table(name = "STAFF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Staff.findAll", query = "SELECT s FROM Staff s"),
    @NamedQuery(name = "Staff.findById", query = "SELECT s FROM Staff s WHERE s.id = :id"),
    @NamedQuery(name = "Staff.findByFirstName", query = "SELECT s FROM Staff s WHERE s.firstName = :firstName"),
    @NamedQuery(name = "Staff.findBySecondName", query = "SELECT s FROM Staff s WHERE s.secondName = :secondName"),
    @NamedQuery(name = "Staff.findByOfficeId", query = "SELECT s FROM Staff s WHERE s.officeId = :officeId")})
public class Staff implements Serializable, com.semenov.core.data.entities.Entity {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "SECOND_NAME")
    private String secondName;
    @JoinColumn(name = "OFFICE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Office officeId;

    public Staff() {
    }

    public Staff(BigDecimal id) {
        this.id = id;
    }

    public Staff(BigDecimal id, String firstName, String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }
    
    public Staff(BigDecimal id, String firstName, String secondName, Office officeId) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.officeId = officeId;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Office getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Office officeId) {
        this.officeId = officeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Staff)) {
            return false;
        }
        Staff other = (Staff) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.semenov.core.data.entities.Staff[ id=" + id + " ]";
    }
    
}
