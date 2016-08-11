package org.ciwise.blackhole.repository;

import org.ciwise.blackhole.domain.Lead;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lead entity.
 */
@SuppressWarnings("unused")
public interface LeadRepository extends JpaRepository<Lead,Long> {

}
