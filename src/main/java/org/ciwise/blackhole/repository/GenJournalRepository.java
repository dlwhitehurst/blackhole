package org.ciwise.blackhole.repository;

import org.ciwise.blackhole.domain.GenJournal;
import org.ciwise.blackhole.domain.GenLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the GenJournal entity.
 */
@SuppressWarnings("unused")
public interface GenJournalRepository extends JpaRepository<GenJournal,Long> {
	@Query(nativeQuery=true, value="select coalesce(max(u.id), 0) from gen_journal u") 
	Long getMaxId();
}
