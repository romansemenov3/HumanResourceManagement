/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.entities;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Roman
 */
@Entity
@Table(name = "OFFICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Office.findAll", query = "SELECT o FROM Office o"),
    @NamedQuery(name = "Office.findById", query = "SELECT o FROM Office o WHERE o.id = :id"),
    @NamedQuery(name = "Office.findByName", query = "SELECT o FROM Office o WHERE o.name = :name"),
    @NamedQuery(name = "Office.findByRegionId", query = "SELECT o FROM Office o WHERE o.regionId = :regionId")})
public class Office implements Serializable, com.semenov.core.data.entities.Entity {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @Expose
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    @Expose
    private String name;
    @JoinColumn(name = "REGION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Region regionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "officeId")
    private Collection<Staff> staffCollection;

    public Office() {
    }

    public Office(BigDecimal id) {
        this.id = id;
    }

    public Office(BigDecimal id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Office(BigDecimal id, String name, Region regionId) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegionId() {
        return regionId;
    }

    public void setRegionId(Region regionId) {
        this.regionId = regionId;
    }

    @XmlTransient
    public Collection<Staff> getStaffCollection() {
        return staffCollection;
    }

    public void setStaffCollection(Collection<Staff> staffCollection) {
        this.staffCollection = staffCollection;
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
        if (!(object instanceof Office)) {
            return false;
        }
        Office other = (Office) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.semenov.core.data.entities.Office[ id=" + id + " ]";
    }
    
}
