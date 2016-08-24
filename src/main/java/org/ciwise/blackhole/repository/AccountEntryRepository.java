package org.ciwise.blackhole.repository;

import org.ciwise.blackhole.domain.AccountEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccountEntry entity.
 */
@SuppressWarnings("unused")
public interface AccountEntryRepository extends JpaRepository<AccountEntry,Long> {

    /**
     * Method to find all by chart number
     * @param cno
     * @param pageable
     * @return
     */
    Page<AccountEntry> findByCno (String cno, Pageable pageable);

    /**
     * Method to find all by chart number
     * @param cno
     * @return
     */
    List<AccountEntry> findByCno (String cno);

}
