package org.ciwise.blackhole.repository.search;

import org.ciwise.blackhole.domain.GenLedger;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GenLedger entity.
 */
public interface GenLedgerSearchRepository extends ElasticsearchRepository<GenLedger, Long> {
}
