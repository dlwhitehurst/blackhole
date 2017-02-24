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
 * A GenJournal Object.
 */
@Entity
@Table(name = "gen_journal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "genjournal")
public class GenJournal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "entrydate")
    private ZonedDateTime entrydate;

    @Column(name = "transaction")
    private String transaction;

    @Column(name = "dacctno")
    private String dacctno;

    @Column(name = "cacctno")
    private String cacctno;

    @Column(name = "debitaccountname")
    private String debitaccountname;

    @Column(name = "creditaccountname")
    private String creditaccountname;

    @Column(name = "dadebit")
    private String dadebit;

    @Column(name = "dacredit")
    private String dacredit;

    @Column(name = "cadebit")
    private String cadebit;

    @Column(name = "cacredit")
    private String cacredit;

    @Column(name = "notes")
    private String notes;

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

    public String getDacctno() {
        return dacctno;
    }

    public void setDacctno(String dacctno) {
        this.dacctno = dacctno;
    }

    public String getCacctno() {
        return cacctno;
    }

    public void setCacctno(String cacctno) {
        this.cacctno = cacctno;
    }

    public String getDadebit() {
        return dadebit;
    }

    public void setDadebit(String dadebit) {
        this.dadebit = dadebit;
    }

    public String getDacredit() {
        return dacredit;
    }

    public void setDacredit(String dacredit) {
        this.dacredit = dacredit;
    }

    public String getCadebit() {
        return cadebit;
    }

    public void setCadebit(String cadebit) {
        this.cadebit = cadebit;
    }

    public String getCacredit() {
        return cacredit;
    }

    public void setCacredit(String cacredit) {
        this.cacredit = cacredit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GenJournal genJournal = (GenJournal) o;
        if(genJournal.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, genJournal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GenJournal{" +
            "id=" + id +
            ", entrydate='" + entrydate + "'" +
            ", transaction='" + transaction + "'" +
            ", dacctno='" + dacctno + "'" +
            ", cacctno='" + cacctno + "'" +
            ", debitaccountname='" + debitaccountname + "'" +
            ", creditaccountname='" + creditaccountname + "'" +
            ", dadebit='" + dadebit + "'" +
            ", dacredit='" + dacredit + "'" +
            ", cadebit='" + cadebit + "'" +
            ", cacredit='" + cacredit + "'" +
            ", notes='" + notes + "'" +
            '}';
    }

    public String getDebitaccountname() {
        return debitaccountname;
    }

    public void setDebitaccountname(String debitaccountname) {
        this.debitaccountname = debitaccountname;
    }

    public String getCreditaccountname() {
        return creditaccountname;
    }

    public void setCreditaccountname(String creditaccountname) {
        this.creditaccountname = creditaccountname;
    }

}
