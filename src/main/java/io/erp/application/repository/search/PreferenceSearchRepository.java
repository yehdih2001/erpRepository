package io.erp.application.repository.search;

import io.erp.application.domain.Preference;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Preference entity.
 */
public interface PreferenceSearchRepository extends ElasticsearchRepository<Preference, Long> {
}
