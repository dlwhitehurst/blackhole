package org.ciwise.blackhole.service;

import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;

import org.ciwise.blackhole.domain.AccountEntry;
import org.ciwise.blackhole.domain.GenAccount;
import org.ciwise.blackhole.domain.LedgerEntry;
import org.ciwise.blackhole.domain.User;
import org.ciwise.blackhole.repository.AccountEntryRepository;
import org.ciwise.blackhole.repository.GenAccountRepository;
import org.ciwise.blackhole.repository.LedgerEntryRepository;
import org.ciwise.blackhole.repository.UserRepository;
import org.ciwise.blackhole.repository.search.AccountEntrySearchRepository;
import org.ciwise.blackhole.repository.search.GenAccountSearchRepository;
import org.ciwise.blackhole.repository.search.LedgerEntrySearchRepository;
import org.ciwise.blackhole.repository.search.UserSearchRepository;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;

@Service
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    @Inject
    private AccountEntryRepository accountEntryRepository;

    @Inject
    private AccountEntrySearchRepository accountEntrySearchRepository;

    @Inject
    private GenAccountRepository genAccountRepository;

    @Inject
    private GenAccountSearchRepository genAccountSearchRepository;

    @Inject
    private LedgerEntryRepository ledgerEntryRepository;

    @Inject
    private LedgerEntrySearchRepository ledgerEntrySearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    @Timed
    public void reindexAll() {
        reindexForClass(AccountEntry.class, accountEntryRepository, accountEntrySearchRepository);
        reindexForClass(GenAccount.class, genAccountRepository, genAccountSearchRepository);
        reindexForClass(LedgerEntry.class, ledgerEntryRepository, ledgerEntrySearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional
    @SuppressWarnings("unchecked")
    private <T> void reindexForClass(Class<T> entityClass, JpaRepository<T, Long> jpaRepository,
                                                          ElasticsearchRepository<T, Long> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
