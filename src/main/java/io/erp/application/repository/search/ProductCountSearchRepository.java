package io.erp.application.repository.search;

import io.erp.application.domain.ProductCount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductCount entity.
 */
public interface ProductCountSearchRepository extends ElasticsearchRepository<ProductCount, Long> {
}
