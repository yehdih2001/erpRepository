package io.erp.application.repository.search;

import io.erp.application.domain.AppUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AppUser entity.
 */
public interface AppUserSearchRepository extends ElasticsearchRepository<AppUser, Long> {
}
