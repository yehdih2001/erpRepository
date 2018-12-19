package io.erp.application.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CompagnySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CompagnySearchRepositoryMockConfiguration {

    @MockBean
    private CompagnySearchRepository mockCompagnySearchRepository;

}
