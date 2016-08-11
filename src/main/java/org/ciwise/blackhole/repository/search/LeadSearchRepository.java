package org.ciwise.blackhole.repository.search;

import org.ciwise.blackhole.domain.Lead;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Lead entity.
 */
public interface LeadSearchRepository extends ElasticsearchRepository<Lead, Long> {
}
