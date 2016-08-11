package org.ciwise.blackhole.repository.search;

import org.ciwise.blackhole.domain.GenAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GenAccount entity.
 */
public interface GenAccountSearchRepository extends ElasticsearchRepository<GenAccount, Long> {
}
