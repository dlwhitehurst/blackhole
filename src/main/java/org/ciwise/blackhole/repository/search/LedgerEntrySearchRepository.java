package org.ciwise.blackhole.repository.search;

import org.ciwise.blackhole.domain.LedgerEntry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LedgerEntry entity.
 */
public interface LedgerEntrySearchRepository extends ElasticsearchRepository<LedgerEntry, Long> {
}
