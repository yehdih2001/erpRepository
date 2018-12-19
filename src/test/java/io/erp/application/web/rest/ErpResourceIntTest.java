package io.erp.application.web.rest;

import io.erp.application.ErpApp;

import io.erp.application.domain.Erp;
import io.erp.application.repository.ErpRepository;
import io.erp.application.repository.search.ErpSearchRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the ErpResource REST controller.
 *
 * @see ErpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApp.class)
public class ErpResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ErpRepository erpRepository;

    /**
     * This repository is mocked in the io.erp.application.repository.search test package.
     *
     * @see io.erp.application.repository.search.ErpSearchRepositoryMockConfiguration
     */
    @Autowired
    private ErpSearchRepository mockErpSearchRepository;

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

    private MockMvc restErpMockMvc;

    private Erp erp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ErpResource erpResource = new ErpResource(erpRepository, mockErpSearchRepository);
        this.restErpMockMvc = MockMvcBuilders.standaloneSetup(erpResource)
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
    public static Erp createEntity(EntityManager em) {
        Erp erp = new Erp()
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .active(DEFAULT_ACTIVE);
        return erp;
    }

    @Before
    public void initTest() {
        erp = createEntity(em);
    }

    @Test
    @Transactional
    public void createErp() throws Exception {
        int databaseSizeBeforeCreate = erpRepository.findAll().size();

        // Create the Erp
        restErpMockMvc.perform(post("/api/erps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(erp)))
            .andExpect(status().isCreated());

        // Validate the Erp in the database
        List<Erp> erpList = erpRepository.findAll();
        assertThat(erpList).hasSize(databaseSizeBeforeCreate + 1);
        Erp testErp = erpList.get(erpList.size() - 1);
        assertThat(testErp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testErp.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testErp.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Erp in Elasticsearch
        verify(mockErpSearchRepository, times(1)).save(testErp);
    }

    @Test
    @Transactional
    public void createErpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = erpRepository.findAll().size();

        // Create the Erp with an existing ID
        erp.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restErpMockMvc.perform(post("/api/erps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(erp)))
            .andExpect(status().isBadRequest());

        // Validate the Erp in the database
        List<Erp> erpList = erpRepository.findAll();
        assertThat(erpList).hasSize(databaseSizeBeforeCreate);

        // Validate the Erp in Elasticsearch
        verify(mockErpSearchRepository, times(0)).save(erp);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = erpRepository.findAll().size();
        // set the field null
        erp.setName(null);

        // Create the Erp, which fails.

        restErpMockMvc.perform(post("/api/erps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(erp)))
            .andExpect(status().isBadRequest());

        List<Erp> erpList = erpRepository.findAll();
        assertThat(erpList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllErps() throws Exception {
        // Initialize the database
        erpRepository.saveAndFlush(erp);

        // Get all the erpList
        restErpMockMvc.perform(get("/api/erps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(erp.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getErp() throws Exception {
        // Initialize the database
        erpRepository.saveAndFlush(erp);

        // Get the erp
        restErpMockMvc.perform(get("/api/erps/{id}", erp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(erp.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingErp() throws Exception {
        // Get the erp
        restErpMockMvc.perform(get("/api/erps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateErp() throws Exception {
        // Initialize the database
        erpRepository.saveAndFlush(erp);

        int databaseSizeBeforeUpdate = erpRepository.findAll().size();

        // Update the erp
        Erp updatedErp = erpRepository.findById(erp.getId()).get();
        // Disconnect from session so that the updates on updatedErp are not directly saved in db
        em.detach(updatedErp);
        updatedErp
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .active(UPDATED_ACTIVE);

        restErpMockMvc.perform(put("/api/erps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedErp)))
            .andExpect(status().isOk());

        // Validate the Erp in the database
        List<Erp> erpList = erpRepository.findAll();
        assertThat(erpList).hasSize(databaseSizeBeforeUpdate);
        Erp testErp = erpList.get(erpList.size() - 1);
        assertThat(testErp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testErp.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testErp.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Erp in Elasticsearch
        verify(mockErpSearchRepository, times(1)).save(testErp);
    }

    @Test
    @Transactional
    public void updateNonExistingErp() throws Exception {
        int databaseSizeBeforeUpdate = erpRepository.findAll().size();

        // Create the Erp

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restErpMockMvc.perform(put("/api/erps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(erp)))
            .andExpect(status().isBadRequest());

        // Validate the Erp in the database
        List<Erp> erpList = erpRepository.findAll();
        assertThat(erpList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Erp in Elasticsearch
        verify(mockErpSearchRepository, times(0)).save(erp);
    }

    @Test
    @Transactional
    public void deleteErp() throws Exception {
        // Initialize the database
        erpRepository.saveAndFlush(erp);

        int databaseSizeBeforeDelete = erpRepository.findAll().size();

        // Get the erp
        restErpMockMvc.perform(delete("/api/erps/{id}", erp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Erp> erpList = erpRepository.findAll();
        assertThat(erpList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Erp in Elasticsearch
        verify(mockErpSearchRepository, times(1)).deleteById(erp.getId());
    }

    @Test
    @Transactional
    public void searchErp() throws Exception {
        // Initialize the database
        erpRepository.saveAndFlush(erp);
        when(mockErpSearchRepository.search(queryStringQuery("id:" + erp.getId())))
            .thenReturn(Collections.singletonList(erp));
        // Search the erp
        restErpMockMvc.perform(get("/api/_search/erps?query=id:" + erp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(erp.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Erp.class);
        Erp erp1 = new Erp();
        erp1.setId(1L);
        Erp erp2 = new Erp();
        erp2.setId(erp1.getId());
        assertThat(erp1).isEqualTo(erp2);
        erp2.setId(2L);
        assertThat(erp1).isNotEqualTo(erp2);
        erp1.setId(null);
        assertThat(erp1).isNotEqualTo(erp2);
    }
}
