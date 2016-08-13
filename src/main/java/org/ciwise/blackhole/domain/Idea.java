package org.ciwise.blackhole.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Idea.
 */
@Entity
@Table(name = "idea")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "idea")
public class Idea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "entrydate")
    private LocalDate entrydate;

    @Column(name = "idea")
    private String idea;

    @Column(name = "inprocess")
    private Boolean inprocess;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(LocalDate entrydate) {
        this.entrydate = entrydate;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public Boolean isInprocess() {
        return inprocess;
    }

    public void setInprocess(Boolean inprocess) {
        this.inprocess = inprocess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Idea idea = (Idea) o;
        if(idea.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, idea.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Idea{" +
            "id=" + id +
            ", entrydate='" + entrydate + "'" +
            ", idea='" + idea + "'" +
            ", inprocess='" + inprocess + "'" +
            '}';
    }
}
