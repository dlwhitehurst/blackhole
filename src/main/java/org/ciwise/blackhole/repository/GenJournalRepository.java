package org.ciwise.blackhole.repository;

import org.ciwise.blackhole.domain.GenJournal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LedgerEntry entity.
 */
@SuppressWarnings("unused")
public interface GenJournalRepository extends JpaRepository<GenJournal,Long> {

}
