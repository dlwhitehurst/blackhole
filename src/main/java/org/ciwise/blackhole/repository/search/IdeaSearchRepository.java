package org.ciwise.blackhole.repository.search;

import org.ciwise.blackhole.domain.Idea;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Idea entity.
 */
public interface IdeaSearchRepository extends ElasticsearchRepository<Idea, Long> {
}
