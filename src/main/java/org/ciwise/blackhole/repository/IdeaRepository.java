package org.ciwise.blackhole.repository;

import org.ciwise.blackhole.domain.Idea;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Idea entity.
 */
@SuppressWarnings("unused")
public interface IdeaRepository extends JpaRepository<Idea,Long> {

}
