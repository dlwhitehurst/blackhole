package org.ciwise.blackhole.repository;

import org.ciwise.blackhole.domain.GenLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccountEntry entity.
 */
public interface GenLedgerRepository extends JpaRepository<GenLedger,Long> {

    /**
     * Method to find all by chart number
     * @param cno
     * @param pageable
     * @return
     */
    Page<GenLedger> findByCno (String cno, Pageable pageable);

    /**
     * Method to find all by chart number
     * @param cno
     * @return
     */
    List<GenLedger> findByCno (String cno);

}
