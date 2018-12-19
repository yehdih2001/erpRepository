package io.erp.application.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ProductReodringRulesSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProductReodringRulesSearchRepositoryMockConfiguration {

    @MockBean
    private ProductReodringRulesSearchRepository mockProductReodringRulesSearchRepository;

}
