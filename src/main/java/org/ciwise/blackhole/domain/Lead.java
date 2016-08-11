package org.ciwise.blackhole.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Lead.
 */
@Entity
@Table(name = "lead")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lead")
public class Lead implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "referringco")
    private String referringco;

    @Column(name = "referringname")
    private String referringname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "opp")
    private String opp;

    @Column(name = "oppwhere")
    private String oppwhere;

    @Column(name = "oppwho")
    private String oppwho;

    @Column(name = "notes")
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferringco() {
        return referringco;
    }

    public void setReferringco(String referringco) {
        this.referringco = referringco;
    }

    public String getReferringname() {
        return referringname;
    }

    public void setReferringname(String referringname) {
        this.referringname = referringname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpp() {
        return opp;
    }

    public void setOpp(String opp) {
        this.opp = opp;
    }

    public String getOppwhere() {
        return oppwhere;
    }

    public void setOppwhere(String oppwhere) {
        this.oppwhere = oppwhere;
    }

    public String getOppwho() {
        return oppwho;
    }

    public void setOppwho(String oppwho) {
        this.oppwho = oppwho;
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
        Lead lead = (Lead) o;
        if(lead.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lead.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lead{" +
            "id=" + id +
            ", referringco='" + referringco + "'" +
            ", referringname='" + referringname + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            ", opp='" + opp + "'" +
            ", oppwhere='" + oppwhere + "'" +
            ", oppwho='" + oppwho + "'" +
            ", notes='" + notes + "'" +
            '}';
    }
}
