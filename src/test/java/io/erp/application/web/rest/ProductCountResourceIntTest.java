package io.erp.application.web.rest;

import io.erp.application.ErpApp;

import io.erp.application.domain.ProductCount;
import io.erp.application.repository.ProductCountRepository;
import io.erp.application.repository.search.ProductCountSearchRepository;
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
 * Test class for the ProductCountResource REST controller.
 *
 * @see ProductCountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApp.class)
public class ProductCountResourceIntTest {

    private static final Integer DEFAULT_ON_HAND = 1;
    private static final Integer UPDATED_ON_HAND = 2;

    private static final Integer DEFAULT_PURCHASED = 1;
    private static final Integer UPDATED_PURCHASED = 2;

    private static final Integer DEFAULT_FORECASTED = 1;
    private static final Integer UPDATED_FORECASTED = 2;

    private static final Integer DEFAULT_SOLD = 1;
    private static final Integer UPDATED_SOLD = 2;

    @Autowired
    private ProductCountRepository productCountRepository;

    /**
     * This repository is mocked in the io.erp.application.repository.search test package.
     *
     * @see io.erp.application.repository.search.ProductCountSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductCountSearchRepository mockProductCountSearchRepository;

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

    private MockMvc restProductCountMockMvc;

    private ProductCount productCount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductCountResource productCountResource = new ProductCountResource(productCountRepository, mockProductCountSearchRepository);
        this.restProductCountMockMvc = MockMvcBuilders.standaloneSetup(productCountResource)
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
    public static ProductCount createEntity(EntityManager em) {
        ProductCount productCount = new ProductCount()
            .onHand(DEFAULT_ON_HAND)
            .purchased(DEFAULT_PURCHASED)
            .forecasted(DEFAULT_FORECASTED)
            .sold(DEFAULT_SOLD);
        return productCount;
    }

    @Before
    public void initTest() {
        productCount = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductCount() throws Exception {
        int databaseSizeBeforeCreate = productCountRepository.findAll().size();

        // Create the ProductCount
        restProductCountMockMvc.perform(post("/api/product-counts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCount)))
            .andExpect(status().isCreated());

        // Validate the ProductCount in the database
        List<ProductCount> productCountList = productCountRepository.findAll();
        assertThat(productCountList).hasSize(databaseSizeBeforeCreate + 1);
        ProductCount testProductCount = productCountList.get(productCountList.size() - 1);
        assertThat(testProductCount.getOnHand()).isEqualTo(DEFAULT_ON_HAND);
        assertThat(testProductCount.getPurchased()).isEqualTo(DEFAULT_PURCHASED);
        assertThat(testProductCount.getForecasted()).isEqualTo(DEFAULT_FORECASTED);
        assertThat(testProductCount.getSold()).isEqualTo(DEFAULT_SOLD);

        // Validate the ProductCount in Elasticsearch
        verify(mockProductCountSearchRepository, times(1)).save(testProductCount);
    }

    @Test
    @Transactional
    public void createProductCountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productCountRepository.findAll().size();

        // Create the ProductCount with an existing ID
        productCount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCountMockMvc.perform(post("/api/product-counts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCount)))
            .andExpect(status().isBadRequest());

        // Validate the ProductCount in the database
        List<ProductCount> productCountList = productCountRepository.findAll();
        assertThat(productCountList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductCount in Elasticsearch
        verify(mockProductCountSearchRepository, times(0)).save(productCount);
    }

    @Test
    @Transactional
    public void getAllProductCounts() throws Exception {
        // Initialize the database
        productCountRepository.saveAndFlush(productCount);

        // Get all the productCountList
        restProductCountMockMvc.perform(get("/api/product-counts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCount.getId().intValue())))
            .andExpect(jsonPath("$.[*].onHand").value(hasItem(DEFAULT_ON_HAND)))
            .andExpect(jsonPath("$.[*].purchased").value(hasItem(DEFAULT_PURCHASED)))
            .andExpect(jsonPath("$.[*].forecasted").value(hasItem(DEFAULT_FORECASTED)))
            .andExpect(jsonPath("$.[*].sold").value(hasItem(DEFAULT_SOLD)));
    }
    
    @Test
    @Transactional
    public void getProductCount() throws Exception {
        // Initialize the database
        productCountRepository.saveAndFlush(productCount);

        // Get the productCount
        restProductCountMockMvc.perform(get("/api/product-counts/{id}", productCount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productCount.getId().intValue()))
            .andExpect(jsonPath("$.onHand").value(DEFAULT_ON_HAND))
            .andExpect(jsonPath("$.purchased").value(DEFAULT_PURCHASED))
            .andExpect(jsonPath("$.forecasted").value(DEFAULT_FORECASTED))
            .andExpect(jsonPath("$.sold").value(DEFAULT_SOLD));
    }

    @Test
    @Transactional
    public void getNonExistingProductCount() throws Exception {
        // Get the productCount
        restProductCountMockMvc.perform(get("/api/product-counts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductCount() throws Exception {
        // Initialize the database
        productCountRepository.saveAndFlush(productCount);

        int databaseSizeBeforeUpdate = productCountRepository.findAll().size();

        // Update the productCount
        ProductCount updatedProductCount = productCountRepository.findById(productCount.getId()).get();
        // Disconnect from session so that the updates on updatedProductCount are not directly saved in db
        em.detach(updatedProductCount);
        updatedProductCount
            .onHand(UPDATED_ON_HAND)
            .purchased(UPDATED_PURCHASED)
            .forecasted(UPDATED_FORECASTED)
            .sold(UPDATED_SOLD);

        restProductCountMockMvc.perform(put("/api/product-counts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductCount)))
            .andExpect(status().isOk());

        // Validate the ProductCount in the database
        List<ProductCount> productCountList = productCountRepository.findAll();
        assertThat(productCountList).hasSize(databaseSizeBeforeUpdate);
        ProductCount testProductCount = productCountList.get(productCountList.size() - 1);
        assertThat(testProductCount.getOnHand()).isEqualTo(UPDATED_ON_HAND);
        assertThat(testProductCount.getPurchased()).isEqualTo(UPDATED_PURCHASED);
        assertThat(testProductCount.getForecasted()).isEqualTo(UPDATED_FORECASTED);
        assertThat(testProductCount.getSold()).isEqualTo(UPDATED_SOLD);

        // Validate the ProductCount in Elasticsearch
        verify(mockProductCountSearchRepository, times(1)).save(testProductCount);
    }

    @Test
    @Transactional
    public void updateNonExistingProductCount() throws Exception {
        int databaseSizeBeforeUpdate = productCountRepository.findAll().size();

        // Create the ProductCount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCountMockMvc.perform(put("/api/product-counts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCount)))
            .andExpect(status().isBadRequest());

        // Validate the ProductCount in the database
        List<ProductCount> productCountList = productCountRepository.findAll();
        assertThat(productCountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductCount in Elasticsearch
        verify(mockProductCountSearchRepository, times(0)).save(productCount);
    }

    @Test
    @Transactional
    public void deleteProductCount() throws Exception {
        // Initialize the database
        productCountRepository.saveAndFlush(productCount);

        int databaseSizeBeforeDelete = productCountRepository.findAll().size();

        // Get the productCount
        restProductCountMockMvc.perform(delete("/api/product-counts/{id}", productCount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductCount> productCountList = productCountRepository.findAll();
        assertThat(productCountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductCount in Elasticsearch
        verify(mockProductCountSearchRepository, times(1)).deleteById(productCount.getId());
    }

    @Test
    @Transactional
    public void searchProductCount() throws Exception {
        // Initialize the database
        productCountRepository.saveAndFlush(productCount);
        when(mockProductCountSearchRepository.search(queryStringQuery("id:" + productCount.getId())))
            .thenReturn(Collections.singletonList(productCount));
        // Search the productCount
        restProductCountMockMvc.perform(get("/api/_search/product-counts?query=id:" + productCount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCount.getId().intValue())))
            .andExpect(jsonPath("$.[*].onHand").value(hasItem(DEFAULT_ON_HAND)))
            .andExpect(jsonPath("$.[*].purchased").value(hasItem(DEFAULT_PURCHASED)))
            .andExpect(jsonPath("$.[*].forecasted").value(hasItem(DEFAULT_FORECASTED)))
            .andExpect(jsonPath("$.[*].sold").value(hasItem(DEFAULT_SOLD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCount.class);
        ProductCount productCount1 = new ProductCount();
        productCount1.setId(1L);
        ProductCount productCount2 = new ProductCount();
        productCount2.setId(productCount1.getId());
        assertThat(productCount1).isEqualTo(productCount2);
        productCount2.setId(2L);
        assertThat(productCount1).isNotEqualTo(productCount2);
        productCount1.setId(null);
        assertThat(productCount1).isNotEqualTo(productCount2);
    }
}
