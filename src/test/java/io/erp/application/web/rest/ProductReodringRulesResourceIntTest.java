package io.erp.application.web.rest;

import io.erp.application.ErpApp;

import io.erp.application.domain.ProductReodringRules;
import io.erp.application.repository.ProductReodringRulesRepository;
import io.erp.application.repository.search.ProductReodringRulesSearchRepository;
import io.erp.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static io.erp.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductReodringRulesResource REST controller.
 *
 * @see ProductReodringRulesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApp.class)
public class ProductReodringRulesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MINIMUM_QUANTITY = 1;
    private static final Integer UPDATED_MINIMUM_QUANTITY = 2;

    private static final Integer DEFAULT_MAXIMUM_QUANTITY = 1;
    private static final Integer UPDATED_MAXIMUM_QUANTITY = 2;

    private static final Integer DEFAULT_QUANTITY_MULTIPLE = 1;
    private static final Integer UPDATED_QUANTITY_MULTIPLE = 2;

    private static final Integer DEFAULT_LEAD_TIME = 1;
    private static final Integer UPDATED_LEAD_TIME = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ProductReodringRulesRepository productReodringRulesRepository;

    /**
     * This repository is mocked in the io.erp.application.repository.search test package.
     *
     * @see io.erp.application.repository.search.ProductReodringRulesSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductReodringRulesSearchRepository mockProductReodringRulesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProductReodringRulesMockMvc;

    private ProductReodringRules productReodringRules;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductReodringRulesResource productReodringRulesResource = new ProductReodringRulesResource(productReodringRulesRepository, mockProductReodringRulesSearchRepository);
        this.restProductReodringRulesMockMvc = MockMvcBuilders.standaloneSetup(productReodringRulesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReodringRules createEntity(EntityManager em) {
        ProductReodringRules productReodringRules = new ProductReodringRules()
            .name(DEFAULT_NAME)
            .minimumQuantity(DEFAULT_MINIMUM_QUANTITY)
            .maximumQuantity(DEFAULT_MAXIMUM_QUANTITY)
            .quantityMultiple(DEFAULT_QUANTITY_MULTIPLE)
            .leadTime(DEFAULT_LEAD_TIME)
            .active(DEFAULT_ACTIVE);
        return productReodringRules;
    }

    @Before
    public void initTest() {
        productReodringRules = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductReodringRules() throws Exception {
        int databaseSizeBeforeCreate = productReodringRulesRepository.findAll().size();

        // Create the ProductReodringRules
        restProductReodringRulesMockMvc.perform(post("/api/product-reodring-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productReodringRules)))
            .andExpect(status().isCreated());

        // Validate the ProductReodringRules in the database
        List<ProductReodringRules> productReodringRulesList = productReodringRulesRepository.findAll();
        assertThat(productReodringRulesList).hasSize(databaseSizeBeforeCreate + 1);
        ProductReodringRules testProductReodringRules = productReodringRulesList.get(productReodringRulesList.size() - 1);
        assertThat(testProductReodringRules.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductReodringRules.getMinimumQuantity()).isEqualTo(DEFAULT_MINIMUM_QUANTITY);
        assertThat(testProductReodringRules.getMaximumQuantity()).isEqualTo(DEFAULT_MAXIMUM_QUANTITY);
        assertThat(testProductReodringRules.getQuantityMultiple()).isEqualTo(DEFAULT_QUANTITY_MULTIPLE);
        assertThat(testProductReodringRules.getLeadTime()).isEqualTo(DEFAULT_LEAD_TIME);
        assertThat(testProductReodringRules.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the ProductReodringRules in Elasticsearch
        verify(mockProductReodringRulesSearchRepository, times(1)).save(testProductReodringRules);
    }

    @Test
    @Transactional
    public void createProductReodringRulesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productReodringRulesRepository.findAll().size();

        // Create the ProductReodringRules with an existing ID
        productReodringRules.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductReodringRulesMockMvc.perform(post("/api/product-reodring-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productReodringRules)))
            .andExpect(status().isBadRequest());

        // Validate the ProductReodringRules in the database
        List<ProductReodringRules> productReodringRulesList = productReodringRulesRepository.findAll();
        assertThat(productReodringRulesList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductReodringRules in Elasticsearch
        verify(mockProductReodringRulesSearchRepository, times(0)).save(productReodringRules);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productReodringRulesRepository.findAll().size();
        // set the field null
        productReodringRules.setName(null);

        // Create the ProductReodringRules, which fails.

        restProductReodringRulesMockMvc.perform(post("/api/product-reodring-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productReodringRules)))
            .andExpect(status().isBadRequest());

        List<ProductReodringRules> productReodringRulesList = productReodringRulesRepository.findAll();
        assertThat(productReodringRulesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductReodringRules() throws Exception {
        // Initialize the database
        productReodringRulesRepository.saveAndFlush(productReodringRules);

        // Get all the productReodringRulesList
        restProductReodringRulesMockMvc.perform(get("/api/product-reodring-rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReodringRules.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].minimumQuantity").value(hasItem(DEFAULT_MINIMUM_QUANTITY)))
            .andExpect(jsonPath("$.[*].maximumQuantity").value(hasItem(DEFAULT_MAXIMUM_QUANTITY)))
            .andExpect(jsonPath("$.[*].quantityMultiple").value(hasItem(DEFAULT_QUANTITY_MULTIPLE)))
            .andExpect(jsonPath("$.[*].leadTime").value(hasItem(DEFAULT_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getProductReodringRules() throws Exception {
        // Initialize the database
        productReodringRulesRepository.saveAndFlush(productReodringRules);

        // Get the productReodringRules
        restProductReodringRulesMockMvc.perform(get("/api/product-reodring-rules/{id}", productReodringRules.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productReodringRules.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.minimumQuantity").value(DEFAULT_MINIMUM_QUANTITY))
            .andExpect(jsonPath("$.maximumQuantity").value(DEFAULT_MAXIMUM_QUANTITY))
            .andExpect(jsonPath("$.quantityMultiple").value(DEFAULT_QUANTITY_MULTIPLE))
            .andExpect(jsonPath("$.leadTime").value(DEFAULT_LEAD_TIME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProductReodringRules() throws Exception {
        // Get the productReodringRules
        restProductReodringRulesMockMvc.perform(get("/api/product-reodring-rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductReodringRules() throws Exception {
        // Initialize the database
        productReodringRulesRepository.saveAndFlush(productReodringRules);

        int databaseSizeBeforeUpdate = productReodringRulesRepository.findAll().size();

        // Update the productReodringRules
        ProductReodringRules updatedProductReodringRules = productReodringRulesRepository.findById(productReodringRules.getId()).get();
        // Disconnect from session so that the updates on updatedProductReodringRules are not directly saved in db
        em.detach(updatedProductReodringRules);
        updatedProductReodringRules
            .name(UPDATED_NAME)
            .minimumQuantity(UPDATED_MINIMUM_QUANTITY)
            .maximumQuantity(UPDATED_MAXIMUM_QUANTITY)
            .quantityMultiple(UPDATED_QUANTITY_MULTIPLE)
            .leadTime(UPDATED_LEAD_TIME)
            .active(UPDATED_ACTIVE);

        restProductReodringRulesMockMvc.perform(put("/api/product-reodring-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductReodringRules)))
            .andExpect(status().isOk());

        // Validate the ProductReodringRules in the database
        List<ProductReodringRules> productReodringRulesList = productReodringRulesRepository.findAll();
        assertThat(productReodringRulesList).hasSize(databaseSizeBeforeUpdate);
        ProductReodringRules testProductReodringRules = productReodringRulesList.get(productReodringRulesList.size() - 1);
        assertThat(testProductReodringRules.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductReodringRules.getMinimumQuantity()).isEqualTo(UPDATED_MINIMUM_QUANTITY);
        assertThat(testProductReodringRules.getMaximumQuantity()).isEqualTo(UPDATED_MAXIMUM_QUANTITY);
        assertThat(testProductReodringRules.getQuantityMultiple()).isEqualTo(UPDATED_QUANTITY_MULTIPLE);
        assertThat(testProductReodringRules.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testProductReodringRules.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the ProductReodringRules in Elasticsearch
        verify(mockProductReodringRulesSearchRepository, times(1)).save(testProductReodringRules);
    }

    @Test
    @Transactional
    public void updateNonExistingProductReodringRules() throws Exception {
        int databaseSizeBeforeUpdate = productReodringRulesRepository.findAll().size();

        // Create the ProductReodringRules

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReodringRulesMockMvc.perform(put("/api/product-reodring-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productReodringRules)))
            .andExpect(status().isBadRequest());

        // Validate the ProductReodringRules in the database
        List<ProductReodringRules> productReodringRulesList = productReodringRulesRepository.findAll();
        assertThat(productReodringRulesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductReodringRules in Elasticsearch
        verify(mockProductReodringRulesSearchRepository, times(0)).save(productReodringRules);
    }

    @Test
    @Transactional
    public void deleteProductReodringRules() throws Exception {
        // Initialize the database
        productReodringRulesRepository.saveAndFlush(productReodringRules);

        int databaseSizeBeforeDelete = productReodringRulesRepository.findAll().size();

        // Get the productReodringRules
        restProductReodringRulesMockMvc.perform(delete("/api/product-reodring-rules/{id}", productReodringRules.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductReodringRules> productReodringRulesList = productReodringRulesRepository.findAll();
        assertThat(productReodringRulesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductReodringRules in Elasticsearch
        verify(mockProductReodringRulesSearchRepository, times(1)).deleteById(productReodringRules.getId());
    }

    @Test
    @Transactional
    public void searchProductReodringRules() throws Exception {
        // Initialize the database
        productReodringRulesRepository.saveAndFlush(productReodringRules);
        when(mockProductReodringRulesSearchRepository.search(queryStringQuery("id:" + productReodringRules.getId())))
            .thenReturn(Collections.singletonList(productReodringRules));
        // Search the productReodringRules
        restProductReodringRulesMockMvc.perform(get("/api/_search/product-reodring-rules?query=id:" + productReodringRules.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReodringRules.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].minimumQuantity").value(hasItem(DEFAULT_MINIMUM_QUANTITY)))
            .andExpect(jsonPath("$.[*].maximumQuantity").value(hasItem(DEFAULT_MAXIMUM_QUANTITY)))
            .andExpect(jsonPath("$.[*].quantityMultiple").value(hasItem(DEFAULT_QUANTITY_MULTIPLE)))
            .andExpect(jsonPath("$.[*].leadTime").value(hasItem(DEFAULT_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductReodringRules.class);
        ProductReodringRules productReodringRules1 = new ProductReodringRules();
        productReodringRules1.setId(1L);
        ProductReodringRules productReodringRules2 = new ProductReodringRules();
        productReodringRules2.setId(productReodringRules1.getId());
        assertThat(productReodringRules1).isEqualTo(productReodringRules2);
        productReodringRules2.setId(2L);
        assertThat(productReodringRules1).isNotEqualTo(productReodringRules2);
        productReodringRules1.setId(null);
        assertThat(productReodringRules1).isNotEqualTo(productReodringRules2);
    }
}
