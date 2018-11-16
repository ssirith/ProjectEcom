/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author SSirith
 */
@Entity
@Table(name = "PROFILE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profile.findAll", query = "SELECT p FROM Profile p")
    , @NamedQuery(name = "Profile.findByProfileid", query = "SELECT p FROM Profile p WHERE p.profileid = :profileid")
    , @NamedQuery(name = "Profile.findByAccountid", query = "SELECT p FROM Profile p WHERE p.accountid = :accountid")
    , @NamedQuery(name = "Profile.findByFname", query = "SELECT p FROM Profile p WHERE p.fname = :fname")
    , @NamedQuery(name = "Profile.findByLname", query = "SELECT p FROM Profile p WHERE p.lname = :lname")
    , @NamedQuery(name = "Profile.findByAddress", query = "SELECT p FROM Profile p WHERE p.address = :address")
    , @NamedQuery(name = "Profile.findByTel", query = "SELECT p FROM Profile p WHERE p.tel = :tel")})
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROFILEID")
    private Integer profileid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FNAME")
    private String fname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "LNAME")
    private String lname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 600)
    @Column(name = "ADDRESS")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TEL")
    private String tel;
    @JoinColumn(name = "ACCOUNTID", referencedColumnName = "ACCOUNTID")
    @ManyToOne(optional = false)
    private Account accountid;

    public Profile() {
    }

    public Profile(Integer profileid) {
        this.profileid = profileid;
    }

    public Profile(Integer profileid, String fname, String lname, String address, String tel) {
        this.profileid = profileid;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.tel = tel;
    }

    public Profile(String fname, String lname, String address, String tel, Account accountid) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.tel = tel;
        this.accountid = accountid;
    }

    public Profile(String fname, String lname, String address, String tel) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.tel = tel;
    }

    
    
    public Integer getProfileid() {
        return profileid;
    }

    public void setProfileid(Integer profileid) {
        this.profileid = profileid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Account getAccountid() {
        return accountid;
    }

    public void setAccountid(Account accountid) {
        this.accountid = accountid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (profileid != null ? profileid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profile)) {
            return false;
        }
        Profile other = (Profile) object;
        if ((this.profileid == null && other.profileid != null) || (this.profileid != null && !this.profileid.equals(other.profileid))) {
            return false;
        }
        return true;
    }

    
//    @Override
//    public String toString() {
//        return "project.model.Profile[ profileid=" + profileid + " ]";
//    }

    @Override
    public String toString() {
        return "Profile{" + "profileid=" + profileid + ", fname=" + fname + ", lname=" + lname + ", address=" + address + ", tel=" + tel + ", accountid=" + accountid + '}';
    }
    
}
