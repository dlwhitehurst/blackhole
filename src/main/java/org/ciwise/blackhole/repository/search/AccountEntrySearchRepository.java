package org.ciwise.blackhole.repository.search;

import org.ciwise.blackhole.domain.AccountEntry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AccountEntry entity.
 */
public interface AccountEntrySearchRepository extends ElasticsearchRepository<AccountEntry, Long> {
}
