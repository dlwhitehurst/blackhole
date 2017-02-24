/**
 * Copyright (c) CI Wise Inc.  All rights reserved.  http://www.ciwise.com
 * The software in this package is published under the terms of the Apache
 * version 2.0 license, a copy of which has been included with this distribution
 * in the LICENSE.md file.
 *
 */

package org.ciwise.blackhole.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A GenLedger Object.
 */
@Entity
@Table(name = "gen_ledger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "genledger")
public class GenLedger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "entrydate")
    private ZonedDateTime entrydate;

    @Column(name = "transaction")
    private String transaction;

    @Column(name = "reconcile")
    private Boolean reconcile;

    @Column(name = "postingref")
    private Long postingref;

    @Column(name = "debit")
    private String debit;

    @Column(name = "credit")
    private String credit;

    @Column(name = "debitbalance")
    private String debitbalance;

    @Column(name = "creditbalance")
    private String creditbalance;

    @Column(name = "notes")
    private String notes;

    @Column(name = "cno")
    private String cno;

    @Column(name = "accountname")
    private String accountname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(ZonedDateTime entrydate) {
        this.entrydate = entrydate;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public Boolean isReconcile() {
        return reconcile;
    }

    public void setReconcile(Boolean reconcile) {
        this.reconcile = reconcile;
    }

    public Long getPostingref() {
        return postingref;
    }

    public void setPostingref(Long postingref) {
        this.postingref = postingref;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebitbalance() {
        return debitbalance;
    }

    public void setDebitbalance(String debitbalance) {
        this.debitbalance = debitbalance;
    }

    public String getCreditbalance() {
        return creditbalance;
    }

    public void setCreditbalance(String creditbalance) {
        this.creditbalance = creditbalance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GenLedger genLedger = (GenLedger) o;
        if(genLedger.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, genLedger.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GenLedger{" +
            "id=" + id +
            ", entrydate='" + entrydate + "'" +
            ", transaction='" + transaction + "'" +
            ", reconcile='" + reconcile + "'" +
            ", postingref='" + postingref + "'" +
            ", debit='" + debit + "'" +
            ", credit='" + credit + "'" +
            ", debitbalance='" + debitbalance + "'" +
            ", creditbalance='" + creditbalance + "'" +
            ", notes='" + notes + "'" +
            ", cno='" + cno + "'" +
            ", accountname='" + accountname + "'" +
            '}';
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }
}
