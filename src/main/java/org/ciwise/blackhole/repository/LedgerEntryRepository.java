package org.ciwise.blackhole.repository;

import org.ciwise.blackhole.domain.LedgerEntry;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LedgerEntry entity.
 */
@SuppressWarnings("unused")
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry,Long> {

}
