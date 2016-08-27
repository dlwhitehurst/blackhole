package org.ciwise.blackhole.repository.search;

import org.ciwise.blackhole.domain.GenJournal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GenJournal entity.
 */
public interface GenJournalSearchRepository extends ElasticsearchRepository<GenJournal, Long> {
}
