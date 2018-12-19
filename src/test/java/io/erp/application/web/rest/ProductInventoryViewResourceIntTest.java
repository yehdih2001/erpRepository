package io.erp.application.web.rest;

import io.erp.application.ErpApp;

import io.erp.application.domain.ProductInventoryView;
import io.erp.application.repository.ProductInventoryViewRepository;
import io.erp.application.repository.search.ProductInventoryViewSearchRepository;
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
import org.springframework.util.Base64Utils;
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
 * Test class for the ProductInventoryViewResource REST controller.
 *
 * @see ProductInventoryViewResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApp.class)
public class ProductInventoryViewResourceIntTest {

    private static final Double DEFAULT_WEIGHT_IN_KG = 1D;
    private static final Double UPDATED_WEIGHT_IN_KG = 2D;

    private static final Double DEFAULT_VOLUME_IN_METERCUBE = 1D;
    private static final Double UPDATED_VOLUME_IN_METERCUBE = 2D;

    private static final String DEFAULT_DESCRIPTION_FOR_DELIVERY_ORDERS = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_FOR_DELIVERY_ORDERS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_FOR_RECEIPTS = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_FOR_RECEIPTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_CUSTOMER_LEAD_TIME = 1;
    private static final Integer UPDATED_CUSTOMER_LEAD_TIME = 2;

    @Autowired
    private ProductInventoryViewRepository productInventoryViewRepository;

    /**
     * This repository is mocked in the io.erp.application.repository.search test package.
     *
     * @see io.erp.application.repository.search.ProductInventoryViewSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductInventoryViewSearchRepository mockProductInventoryViewSearchRepository;

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

    private MockMvc restProductInventoryViewMockMvc;

    private ProductInventoryView productInventoryView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductInventoryViewResource productInventoryViewResource = new ProductInventoryViewResource(productInventoryViewRepository, mockProductInventoryViewSearchRepository);
        this.restProductInventoryViewMockMvc = MockMvcBuilders.standaloneSetup(productInventoryViewResource)
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
    public static ProductInventoryView createEntity(EntityManager em) {
        ProductInventoryView productInventoryView = new ProductInventoryView()
            .weightInKg(DEFAULT_WEIGHT_IN_KG)
            .volumeInMetercube(DEFAULT_VOLUME_IN_METERCUBE)
            .descriptionForDeliveryOrders(DEFAULT_DESCRIPTION_FOR_DELIVERY_ORDERS)
            .descriptionForReceipts(DEFAULT_DESCRIPTION_FOR_RECEIPTS)
            .customerLeadTime(DEFAULT_CUSTOMER_LEAD_TIME);
        return productInventoryView;
    }

    @Before
    public void initTest() {
        productInventoryView = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductInventoryView() throws Exception {
        int databaseSizeBeforeCreate = productInventoryViewRepository.findAll().size();

        // Create the ProductInventoryView
        restProductInventoryViewMockMvc.perform(post("/api/product-inventory-views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productInventoryView)))
            .andExpect(status().isCreated());

        // Validate the ProductInventoryView in the database
        List<ProductInventoryView> productInventoryViewList = productInventoryViewRepository.findAll();
        assertThat(productInventoryViewList).hasSize(databaseSizeBeforeCreate + 1);
        ProductInventoryView testProductInventoryView = productInventoryViewList.get(productInventoryViewList.size() - 1);
        assertThat(testProductInventoryView.getWeightInKg()).isEqualTo(DEFAULT_WEIGHT_IN_KG);
        assertThat(testProductInventoryView.getVolumeInMetercube()).isEqualTo(DEFAULT_VOLUME_IN_METERCUBE);
        assertThat(testProductInventoryView.getDescriptionForDeliveryOrders()).isEqualTo(DEFAULT_DESCRIPTION_FOR_DELIVERY_ORDERS);
        assertThat(testProductInventoryView.getDescriptionForReceipts()).isEqualTo(DEFAULT_DESCRIPTION_FOR_RECEIPTS);
        assertThat(testProductInventoryView.getCustomerLeadTime()).isEqualTo(DEFAULT_CUSTOMER_LEAD_TIME);

        // Validate the ProductInventoryView in Elasticsearch
        verify(mockProductInventoryViewSearchRepository, times(1)).save(testProductInventoryView);
    }

    @Test
    @Transactional
    public void createProductInventoryViewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productInventoryViewRepository.findAll().size();

        // Create the ProductInventoryView with an existing ID
        productInventoryView.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductInventoryViewMockMvc.perform(post("/api/product-inventory-views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productInventoryView)))
            .andExpect(status().isBadRequest());

        // Validate the ProductInventoryView in the database
        List<ProductInventoryView> productInventoryViewList = productInventoryViewRepository.findAll();
        assertThat(productInventoryViewList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductInventoryView in Elasticsearch
        verify(mockProductInventoryViewSearchRepository, times(0)).save(productInventoryView);
    }

    @Test
    @Transactional
    public void getAllProductInventoryViews() throws Exception {
        // Initialize the database
        productInventoryViewRepository.saveAndFlush(productInventoryView);

        // Get all the productInventoryViewList
        restProductInventoryViewMockMvc.perform(get("/api/product-inventory-views?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInventoryView.getId().intValue())))
            .andExpect(jsonPath("$.[*].weightInKg").value(hasItem(DEFAULT_WEIGHT_IN_KG.doubleValue())))
            .andExpect(jsonPath("$.[*].volumeInMetercube").value(hasItem(DEFAULT_VOLUME_IN_METERCUBE.doubleValue())))
            .andExpect(jsonPath("$.[*].descriptionForDeliveryOrders").value(hasItem(DEFAULT_DESCRIPTION_FOR_DELIVERY_ORDERS.toString())))
            .andExpect(jsonPath("$.[*].descriptionForReceipts").value(hasItem(DEFAULT_DESCRIPTION_FOR_RECEIPTS.toString())))
            .andExpect(jsonPath("$.[*].customerLeadTime").value(hasItem(DEFAULT_CUSTOMER_LEAD_TIME)));
    }
    
    @Test
    @Transactional
    public void getProductInventoryView() throws Exception {
        // Initialize the database
        productInventoryViewRepository.saveAndFlush(productInventoryView);

        // Get the productInventoryView
        restProductInventoryViewMockMvc.perform(get("/api/product-inventory-views/{id}", productInventoryView.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productInventoryView.getId().intValue()))
            .andExpect(jsonPath("$.weightInKg").value(DEFAULT_WEIGHT_IN_KG.doubleValue()))
            .andExpect(jsonPath("$.volumeInMetercube").value(DEFAULT_VOLUME_IN_METERCUBE.doubleValue()))
            .andExpect(jsonPath("$.descriptionForDeliveryOrders").value(DEFAULT_DESCRIPTION_FOR_DELIVERY_ORDERS.toString()))
            .andExpect(jsonPath("$.descriptionForReceipts").value(DEFAULT_DESCRIPTION_FOR_RECEIPTS.toString()))
            .andExpect(jsonPath("$.customerLeadTime").value(DEFAULT_CUSTOMER_LEAD_TIME));
    }

    @Test
    @Transactional
    public void getNonExistingProductInventoryView() throws Exception {
        // Get the productInventoryView
        restProductInventoryViewMockMvc.perform(get("/api/product-inventory-views/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductInventoryView() throws Exception {
        // Initialize the database
        productInventoryViewRepository.saveAndFlush(productInventoryView);

        int databaseSizeBeforeUpdate = productInventoryViewRepository.findAll().size();

        // Update the productInventoryView
        ProductInventoryView updatedProductInventoryView = productInventoryViewRepository.findById(productInventoryView.getId()).get();
        // Disconnect from session so that the updates on updatedProductInventoryView are not directly saved in db
        em.detach(updatedProductInventoryView);
        updatedProductInventoryView
            .weightInKg(UPDATED_WEIGHT_IN_KG)
            .volumeInMetercube(UPDATED_VOLUME_IN_METERCUBE)
            .descriptionForDeliveryOrders(UPDATED_DESCRIPTION_FOR_DELIVERY_ORDERS)
            .descriptionForReceipts(UPDATED_DESCRIPTION_FOR_RECEIPTS)
            .customerLeadTime(UPDATED_CUSTOMER_LEAD_TIME);

        restProductInventoryViewMockMvc.perform(put("/api/product-inventory-views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductInventoryView)))
            .andExpect(status().isOk());

        // Validate the ProductInventoryView in the database
        List<ProductInventoryView> productInventoryViewList = productInventoryViewRepository.findAll();
        assertThat(productInventoryViewList).hasSize(databaseSizeBeforeUpdate);
        ProductInventoryView testProductInventoryView = productInventoryViewList.get(productInventoryViewList.size() - 1);
        assertThat(testProductInventoryView.getWeightInKg()).isEqualTo(UPDATED_WEIGHT_IN_KG);
        assertThat(testProductInventoryView.getVolumeInMetercube()).isEqualTo(UPDATED_VOLUME_IN_METERCUBE);
        assertThat(testProductInventoryView.getDescriptionForDeliveryOrders()).isEqualTo(UPDATED_DESCRIPTION_FOR_DELIVERY_ORDERS);
        assertThat(testProductInventoryView.getDescriptionForReceipts()).isEqualTo(UPDATED_DESCRIPTION_FOR_RECEIPTS);
        assertThat(testProductInventoryView.getCustomerLeadTime()).isEqualTo(UPDATED_CUSTOMER_LEAD_TIME);

        // Validate the ProductInventoryView in Elasticsearch
        verify(mockProductInventoryViewSearchRepository, times(1)).save(testProductInventoryView);
    }

    @Test
    @Transactional
    public void updateNonExistingProductInventoryView() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryViewRepository.findAll().size();

        // Create the ProductInventoryView

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInventoryViewMockMvc.perform(put("/api/product-inventory-views")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productInventoryView)))
            .andExpect(status().isBadRequest());

        // Validate the ProductInventoryView in the database
        List<ProductInventoryView> productInventoryViewList = productInventoryViewRepository.findAll();
        assertThat(productInventoryViewList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductInventoryView in Elasticsearch
        verify(mockProductInventoryViewSearchRepository, times(0)).save(productInventoryView);
    }

    @Test
    @Transactional
    public void deleteProductInventoryView() throws Exception {
        // Initialize the database
        productInventoryViewRepository.saveAndFlush(productInventoryView);

        int databaseSizeBeforeDelete = productInventoryViewRepository.findAll().size();

        // Get the productInventoryView
        restProductInventoryViewMockMvc.perform(delete("/api/product-inventory-views/{id}", productInventoryView.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductInventoryView> productInventoryViewList = productInventoryViewRepository.findAll();
        assertThat(productInventoryViewList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductInventoryView in Elasticsearch
        verify(mockProductInventoryViewSearchRepository, times(1)).deleteById(productInventoryView.getId());
    }

    @Test
    @Transactional
    public void searchProductInventoryView() throws Exception {
        // Initialize the database
        productInventoryViewRepository.saveAndFlush(productInventoryView);
        when(mockProductInventoryViewSearchRepository.search(queryStringQuery("id:" + productInventoryView.getId())))
            .thenReturn(Collections.singletonList(productInventoryView));
        // Search the productInventoryView
        restProductInventoryViewMockMvc.perform(get("/api/_search/product-inventory-views?query=id:" + productInventoryView.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInventoryView.getId().intValue())))
            .andExpect(jsonPath("$.[*].weightInKg").value(hasItem(DEFAULT_WEIGHT_IN_KG.doubleValue())))
            .andExpect(jsonPath("$.[*].volumeInMetercube").value(hasItem(DEFAULT_VOLUME_IN_METERCUBE.doubleValue())))
            .andExpect(jsonPath("$.[*].descriptionForDeliveryOrders").value(hasItem(DEFAULT_DESCRIPTION_FOR_DELIVERY_ORDERS.toString())))
            .andExpect(jsonPath("$.[*].descriptionForReceipts").value(hasItem(DEFAULT_DESCRIPTION_FOR_RECEIPTS.toString())))
            .andExpect(jsonPath("$.[*].customerLeadTime").value(hasItem(DEFAULT_CUSTOMER_LEAD_TIME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductInventoryView.class);
        ProductInventoryView productInventoryView1 = new ProductInventoryView();
        productInventoryView1.setId(1L);
        ProductInventoryView productInventoryView2 = new ProductInventoryView();
        productInventoryView2.setId(productInventoryView1.getId());
        assertThat(productInventoryView1).isEqualTo(productInventoryView2);
        productInventoryView2.setId(2L);
        assertThat(productInventoryView1).isNotEqualTo(productInventoryView2);
        productInventoryView1.setId(null);
        assertThat(productInventoryView1).isNotEqualTo(productInventoryView2);
    }
}
