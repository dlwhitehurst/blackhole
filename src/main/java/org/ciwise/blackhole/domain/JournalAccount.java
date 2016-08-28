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
import java.util.Objects;

/**
 * A JournalAccount.
 */
@Entity
@Table(name = "journal_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "journalaccount")
public class JournalAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "accountname")
    private String accountname;

    @Column(name = "cno")
    private String cno;

    @Column(name = "dc")
    private String dc;

    @Column(name = "debit")
    private String debit;

    @Column(name = "credit")
    private String credit;

    @ManyToOne
    private GenJournal genJournal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
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

    public GenJournal getGenJournal() {
        return genJournal;
    }

    public void setGenJournal(GenJournal genJournal) {
        this.genJournal = genJournal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JournalAccount journalAccount = (JournalAccount) o;
        if(journalAccount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, journalAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JournalAccount{" +
            "id=" + id +
            ", accountname='" + accountname + "'" +
            ", cno='" + cno + "'" +
            ", dc='" + dc + "'" +
            ", debit='" + debit + "'" +
            ", credit='" + credit + "'" +
            '}';
    }

}
