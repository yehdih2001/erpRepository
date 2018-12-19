package io.erp.application.web.rest;

import io.erp.application.ErpApp;

import io.erp.application.domain.Product;
import io.erp.application.repository.ProductRepository;
import io.erp.application.repository.search.ProductSearchRepository;
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

import io.erp.application.domain.enumeration.ProductType;
/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApp.class)
public class ProductResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CAN_BE_SOLD = false;
    private static final Boolean UPDATED_CAN_BE_SOLD = true;

    private static final Boolean DEFAULT_CAN_BE_PURCHASED = false;
    private static final Boolean UPDATED_CAN_BE_PURCHASED = true;

    private static final ProductType DEFAULT_PRODUCT_TYPE = ProductType.Consumable;
    private static final ProductType UPDATED_PRODUCT_TYPE = ProductType.StorableProduct;

    private static final String DEFAULT_PRODUCT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_INTERNAL_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_REFERENCE = "BBBBBBBBBB";

    private static final Double DEFAULT_SALES_PRICE = 1D;
    private static final Double UPDATED_SALES_PRICE = 2D;

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;

    private static final byte[] DEFAULT_BAR_CODE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BAR_CODE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BAR_CODE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BAR_CODE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_INTERNAL_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ProductRepository productRepository;

    /**
     * This repository is mocked in the io.erp.application.repository.search test package.
     *
     * @see io.erp.application.repository.search.ProductSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductSearchRepository mockProductSearchRepository;

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

    private MockMvc restProductMockMvc;

    private Product product;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productRepository, mockProductSearchRepository);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .canBeSold(DEFAULT_CAN_BE_SOLD)
            .canBePurchased(DEFAULT_CAN_BE_PURCHASED)
            .productType(DEFAULT_PRODUCT_TYPE)
            .productCategory(DEFAULT_PRODUCT_CATEGORY)
            .internalReference(DEFAULT_INTERNAL_REFERENCE)
            .salesPrice(DEFAULT_SALES_PRICE)
            .cost(DEFAULT_COST)
            .barCode(DEFAULT_BAR_CODE)
            .barCodeContentType(DEFAULT_BAR_CODE_CONTENT_TYPE)
            .internalNotes(DEFAULT_INTERNAL_NOTES)
            .active(DEFAULT_ACTIVE);
        return product;
    }

    @Before
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.isCanBeSold()).isEqualTo(DEFAULT_CAN_BE_SOLD);
        assertThat(testProduct.isCanBePurchased()).isEqualTo(DEFAULT_CAN_BE_PURCHASED);
        assertThat(testProduct.getProductType()).isEqualTo(DEFAULT_PRODUCT_TYPE);
        assertThat(testProduct.getProductCategory()).isEqualTo(DEFAULT_PRODUCT_CATEGORY);
        assertThat(testProduct.getInternalReference()).isEqualTo(DEFAULT_INTERNAL_REFERENCE);
        assertThat(testProduct.getSalesPrice()).isEqualTo(DEFAULT_SALES_PRICE);
        assertThat(testProduct.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testProduct.getBarCode()).isEqualTo(DEFAULT_BAR_CODE);
        assertThat(testProduct.getBarCodeContentType()).isEqualTo(DEFAULT_BAR_CODE_CONTENT_TYPE);
        assertThat(testProduct.getInternalNotes()).isEqualTo(DEFAULT_INTERNAL_NOTES);
        assertThat(testProduct.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].canBeSold").value(hasItem(DEFAULT_CAN_BE_SOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].canBePurchased").value(hasItem(DEFAULT_CAN_BE_PURCHASED.booleanValue())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productCategory").value(hasItem(DEFAULT_PRODUCT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].internalReference").value(hasItem(DEFAULT_INTERNAL_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].salesPrice").value(hasItem(DEFAULT_SALES_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].barCodeContentType").value(hasItem(DEFAULT_BAR_CODE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].barCode").value(hasItem(Base64Utils.encodeToString(DEFAULT_BAR_CODE))))
            .andExpect(jsonPath("$.[*].internalNotes").value(hasItem(DEFAULT_INTERNAL_NOTES.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.canBeSold").value(DEFAULT_CAN_BE_SOLD.booleanValue()))
            .andExpect(jsonPath("$.canBePurchased").value(DEFAULT_CAN_BE_PURCHASED.booleanValue()))
            .andExpect(jsonPath("$.productType").value(DEFAULT_PRODUCT_TYPE.toString()))
            .andExpect(jsonPath("$.productCategory").value(DEFAULT_PRODUCT_CATEGORY.toString()))
            .andExpect(jsonPath("$.internalReference").value(DEFAULT_INTERNAL_REFERENCE.toString()))
            .andExpect(jsonPath("$.salesPrice").value(DEFAULT_SALES_PRICE.doubleValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.barCodeContentType").value(DEFAULT_BAR_CODE_CONTENT_TYPE))
            .andExpect(jsonPath("$.barCode").value(Base64Utils.encodeToString(DEFAULT_BAR_CODE)))
            .andExpect(jsonPath("$.internalNotes").value(DEFAULT_INTERNAL_NOTES.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .canBeSold(UPDATED_CAN_BE_SOLD)
            .canBePurchased(UPDATED_CAN_BE_PURCHASED)
            .productType(UPDATED_PRODUCT_TYPE)
            .productCategory(UPDATED_PRODUCT_CATEGORY)
            .internalReference(UPDATED_INTERNAL_REFERENCE)
            .salesPrice(UPDATED_SALES_PRICE)
            .cost(UPDATED_COST)
            .barCode(UPDATED_BAR_CODE)
            .barCodeContentType(UPDATED_BAR_CODE_CONTENT_TYPE)
            .internalNotes(UPDATED_INTERNAL_NOTES)
            .active(UPDATED_ACTIVE);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduct)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.isCanBeSold()).isEqualTo(UPDATED_CAN_BE_SOLD);
        assertThat(testProduct.isCanBePurchased()).isEqualTo(UPDATED_CAN_BE_PURCHASED);
        assertThat(testProduct.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testProduct.getProductCategory()).isEqualTo(UPDATED_PRODUCT_CATEGORY);
        assertThat(testProduct.getInternalReference()).isEqualTo(UPDATED_INTERNAL_REFERENCE);
        assertThat(testProduct.getSalesPrice()).isEqualTo(UPDATED_SALES_PRICE);
        assertThat(testProduct.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testProduct.getBarCode()).isEqualTo(UPDATED_BAR_CODE);
        assertThat(testProduct.getBarCodeContentType()).isEqualTo(UPDATED_BAR_CODE_CONTENT_TYPE);
        assertThat(testProduct.getInternalNotes()).isEqualTo(UPDATED_INTERNAL_NOTES);
        assertThat(testProduct.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Get the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).deleteById(product.getId());
    }

    @Test
    @Transactional
    public void searchProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        when(mockProductSearchRepository.search(queryStringQuery("id:" + product.getId())))
            .thenReturn(Collections.singletonList(product));
        // Search the product
        restProductMockMvc.perform(get("/api/_search/products?query=id:" + product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].canBeSold").value(hasItem(DEFAULT_CAN_BE_SOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].canBePurchased").value(hasItem(DEFAULT_CAN_BE_PURCHASED.booleanValue())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productCategory").value(hasItem(DEFAULT_PRODUCT_CATEGORY)))
            .andExpect(jsonPath("$.[*].internalReference").value(hasItem(DEFAULT_INTERNAL_REFERENCE)))
            .andExpect(jsonPath("$.[*].salesPrice").value(hasItem(DEFAULT_SALES_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].barCodeContentType").value(hasItem(DEFAULT_BAR_CODE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].barCode").value(hasItem(Base64Utils.encodeToString(DEFAULT_BAR_CODE))))
            .andExpect(jsonPath("$.[*].internalNotes").value(hasItem(DEFAULT_INTERNAL_NOTES.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }
}
