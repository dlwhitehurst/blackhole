package org.ciwise.blackhole.repository;

import org.ciwise.blackhole.domain.AccountEntry;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccountEntry entity.
 */
@SuppressWarnings("unused")
public interface AccountEntryRepository extends JpaRepository<AccountEntry,Long> {

}
