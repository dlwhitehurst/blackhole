package org.ciwise.blackhole.repository;

import org.ciwise.blackhole.domain.GenAccount;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GenAccount entity.
 */
@SuppressWarnings("unused")
public interface GenAccountRepository extends JpaRepository<GenAccount,Long> {

}
