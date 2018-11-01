/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SSirith
 */
@Entity
@Table(name = "CARD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Card.findAll", query = "SELECT c FROM Card c")
    , @NamedQuery(name = "Card.findByCardid", query = "SELECT c FROM Card c WHERE c.cardid = :cardid")
    , @NamedQuery(name = "Card.findByCardnumber", query = "SELECT c FROM Card c WHERE c.cardnumber = :cardnumber")
    , @NamedQuery(name = "Card.findByExp", query = "SELECT c FROM Card c WHERE c.exp = :exp")
    , @NamedQuery(name = "Card.findByCvc", query = "SELECT c FROM Card c WHERE c.cvc = :cvc")})
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CARDID")
    private Integer cardid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CARDNUMBER")
    private int cardnumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXP")
    @Temporal(TemporalType.DATE)
    private Date exp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CVC")
    private String cvc;
    @JoinColumn(name = "ACCOUNTID", referencedColumnName = "ACCOUNTID")
    @ManyToOne(optional = false)
    private Account accountid;

    public Card() {
    }

    public Card(Integer cardid) {
        this.cardid = cardid;
    }

    public Card(Integer cardid, int cardnumber, Date exp, String cvc) {
        this.cardid = cardid;
        this.cardnumber = cardnumber;
        this.exp = exp;
        this.cvc = cvc;
    }

    public Integer getCardid() {
        return cardid;
    }

    public void setCardid(Integer cardid) {
        this.cardid = cardid;
    }

    public int getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(int cardnumber) {
        this.cardnumber = cardnumber;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
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
        hash += (cardid != null ? cardid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Card)) {
            return false;
        }
        Card other = (Card) object;
        if ((this.cardid == null && other.cardid != null) || (this.cardid != null && !this.cardid.equals(other.cardid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "project.model.Card[ cardid=" + cardid + " ]";
    }
    
}
