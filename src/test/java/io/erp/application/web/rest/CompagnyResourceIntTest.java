package io.erp.application.web.rest;

import io.erp.application.ErpApp;

import io.erp.application.domain.Compagny;
import io.erp.application.repository.CompagnyRepository;
import io.erp.application.repository.search.CompagnySearchRepository;
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

import io.erp.application.domain.enumeration.Incoterm;
/**
 * Test class for the CompagnyResource REST controller.
 *
 * @see CompagnyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApp.class)
public class CompagnyResourceIntTest {

    private static final String DEFAULT_COMPAGNY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPAGNY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_2 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_V_AT = "AAAAAAAAAA";
    private static final String UPDATED_V_AT = "BBBBBBBBBB";

    private static final String DEFAULT_COMPAGNY_REGISTRY = "AAAAAAAAAA";
    private static final String UPDATED_COMPAGNY_REGISTRY = "BBBBBBBBBB";

    private static final String DEFAULT_SIRET = "AAAAAAAAAA";
    private static final String UPDATED_SIRET = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final Incoterm DEFAULT_DEFAULT_INCOTERM = Incoterm.EX_WORKS;
    private static final Incoterm UPDATED_DEFAULT_INCOTERM = Incoterm.FREE_CARRIER;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private CompagnyRepository compagnyRepository;

    /**
     * This repository is mocked in the io.erp.application.repository.search test package.
     *
     * @see io.erp.application.repository.search.CompagnySearchRepositoryMockConfiguration
     */
    @Autowired
    private CompagnySearchRepository mockCompagnySearchRepository;

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

    private MockMvc restCompagnyMockMvc;

    private Compagny compagny;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompagnyResource compagnyResource = new CompagnyResource(compagnyRepository, mockCompagnySearchRepository);
        this.restCompagnyMockMvc = MockMvcBuilders.standaloneSetup(compagnyResource)
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
    public static Compagny createEntity(EntityManager em) {
        Compagny compagny = new Compagny()
            .compagnyName(DEFAULT_COMPAGNY_NAME)
            .website(DEFAULT_WEBSITE)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .street(DEFAULT_STREET)
            .street2(DEFAULT_STREET_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .vAT(DEFAULT_V_AT)
            .compagnyRegistry(DEFAULT_COMPAGNY_REGISTRY)
            .siret(DEFAULT_SIRET)
            .currency(DEFAULT_CURRENCY)
            .defaultIncoterm(DEFAULT_DEFAULT_INCOTERM)
            .active(DEFAULT_ACTIVE);
        return compagny;
    }

    @Before
    public void initTest() {
        compagny = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompagny() throws Exception {
        int databaseSizeBeforeCreate = compagnyRepository.findAll().size();

        // Create the Compagny
        restCompagnyMockMvc.perform(post("/api/compagnies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compagny)))
            .andExpect(status().isCreated());

        // Validate the Compagny in the database
        List<Compagny> compagnyList = compagnyRepository.findAll();
        assertThat(compagnyList).hasSize(databaseSizeBeforeCreate + 1);
        Compagny testCompagny = compagnyList.get(compagnyList.size() - 1);
        assertThat(testCompagny.getCompagnyName()).isEqualTo(DEFAULT_COMPAGNY_NAME);
        assertThat(testCompagny.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testCompagny.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCompagny.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testCompagny.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testCompagny.getStreet2()).isEqualTo(DEFAULT_STREET_2);
        assertThat(testCompagny.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCompagny.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCompagny.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testCompagny.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCompagny.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCompagny.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompagny.getvAT()).isEqualTo(DEFAULT_V_AT);
        assertThat(testCompagny.getCompagnyRegistry()).isEqualTo(DEFAULT_COMPAGNY_REGISTRY);
        assertThat(testCompagny.getSiret()).isEqualTo(DEFAULT_SIRET);
        assertThat(testCompagny.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testCompagny.getDefaultIncoterm()).isEqualTo(DEFAULT_DEFAULT_INCOTERM);
        assertThat(testCompagny.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Compagny in Elasticsearch
        verify(mockCompagnySearchRepository, times(1)).save(testCompagny);
    }

    @Test
    @Transactional
    public void createCompagnyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = compagnyRepository.findAll().size();

        // Create the Compagny with an existing ID
        compagny.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompagnyMockMvc.perform(post("/api/compagnies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compagny)))
            .andExpect(status().isBadRequest());

        // Validate the Compagny in the database
        List<Compagny> compagnyList = compagnyRepository.findAll();
        assertThat(compagnyList).hasSize(databaseSizeBeforeCreate);

        // Validate the Compagny in Elasticsearch
        verify(mockCompagnySearchRepository, times(0)).save(compagny);
    }

    @Test
    @Transactional
    public void checkCompagnyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = compagnyRepository.findAll().size();
        // set the field null
        compagny.setCompagnyName(null);

        // Create the Compagny, which fails.

        restCompagnyMockMvc.perform(post("/api/compagnies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compagny)))
            .andExpect(status().isBadRequest());

        List<Compagny> compagnyList = compagnyRepository.findAll();
        assertThat(compagnyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = compagnyRepository.findAll().size();
        // set the field null
        compagny.setCity(null);

        // Create the Compagny, which fails.

        restCompagnyMockMvc.perform(post("/api/compagnies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compagny)))
            .andExpect(status().isBadRequest());

        List<Compagny> compagnyList = compagnyRepository.findAll();
        assertThat(compagnyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = compagnyRepository.findAll().size();
        // set the field null
        compagny.setZipCode(null);

        // Create the Compagny, which fails.

        restCompagnyMockMvc.perform(post("/api/compagnies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compagny)))
            .andExpect(status().isBadRequest());

        List<Compagny> compagnyList = compagnyRepository.findAll();
        assertThat(compagnyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = compagnyRepository.findAll().size();
        // set the field null
        compagny.setCountry(null);

        // Create the Compagny, which fails.

        restCompagnyMockMvc.perform(post("/api/compagnies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compagny)))
            .andExpect(status().isBadRequest());

        List<Compagny> compagnyList = compagnyRepository.findAll();
        assertThat(compagnyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompagnies() throws Exception {
        // Initialize the database
        compagnyRepository.saveAndFlush(compagny);

        // Get all the compagnyList
        restCompagnyMockMvc.perform(get("/api/compagnies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compagny.getId().intValue())))
            .andExpect(jsonPath("$.[*].compagnyName").value(hasItem(DEFAULT_COMPAGNY_NAME.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].vAT").value(hasItem(DEFAULT_V_AT.toString())))
            .andExpect(jsonPath("$.[*].compagnyRegistry").value(hasItem(DEFAULT_COMPAGNY_REGISTRY.toString())))
            .andExpect(jsonPath("$.[*].siret").value(hasItem(DEFAULT_SIRET.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].defaultIncoterm").value(hasItem(DEFAULT_DEFAULT_INCOTERM.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCompagny() throws Exception {
        // Initialize the database
        compagnyRepository.saveAndFlush(compagny);

        // Get the compagny
        restCompagnyMockMvc.perform(get("/api/compagnies/{id}", compagny.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(compagny.getId().intValue()))
            .andExpect(jsonPath("$.compagnyName").value(DEFAULT_COMPAGNY_NAME.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.street2").value(DEFAULT_STREET_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.vAT").value(DEFAULT_V_AT.toString()))
            .andExpect(jsonPath("$.compagnyRegistry").value(DEFAULT_COMPAGNY_REGISTRY.toString()))
            .andExpect(jsonPath("$.siret").value(DEFAULT_SIRET.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.defaultIncoterm").value(DEFAULT_DEFAULT_INCOTERM.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCompagny() throws Exception {
        // Get the compagny
        restCompagnyMockMvc.perform(get("/api/compagnies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompagny() throws Exception {
        // Initialize the database
        compagnyRepository.saveAndFlush(compagny);

        int databaseSizeBeforeUpdate = compagnyRepository.findAll().size();

        // Update the compagny
        Compagny updatedCompagny = compagnyRepository.findById(compagny.getId()).get();
        // Disconnect from session so that the updates on updatedCompagny are not directly saved in db
        em.detach(updatedCompagny);
        updatedCompagny
            .compagnyName(UPDATED_COMPAGNY_NAME)
            .website(UPDATED_WEBSITE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .street(UPDATED_STREET)
            .street2(UPDATED_STREET_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .vAT(UPDATED_V_AT)
            .compagnyRegistry(UPDATED_COMPAGNY_REGISTRY)
            .siret(UPDATED_SIRET)
            .currency(UPDATED_CURRENCY)
            .defaultIncoterm(UPDATED_DEFAULT_INCOTERM)
            .active(UPDATED_ACTIVE);

        restCompagnyMockMvc.perform(put("/api/compagnies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompagny)))
            .andExpect(status().isOk());

        // Validate the Compagny in the database
        List<Compagny> compagnyList = compagnyRepository.findAll();
        assertThat(compagnyList).hasSize(databaseSizeBeforeUpdate);
        Compagny testCompagny = compagnyList.get(compagnyList.size() - 1);
        assertThat(testCompagny.getCompagnyName()).isEqualTo(UPDATED_COMPAGNY_NAME);
        assertThat(testCompagny.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCompagny.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCompagny.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testCompagny.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testCompagny.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testCompagny.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCompagny.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCompagny.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testCompagny.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCompagny.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCompagny.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompagny.getvAT()).isEqualTo(UPDATED_V_AT);
        assertThat(testCompagny.getCompagnyRegistry()).isEqualTo(UPDATED_COMPAGNY_REGISTRY);
        assertThat(testCompagny.getSiret()).isEqualTo(UPDATED_SIRET);
        assertThat(testCompagny.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testCompagny.getDefaultIncoterm()).isEqualTo(UPDATED_DEFAULT_INCOTERM);
        assertThat(testCompagny.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Compagny in Elasticsearch
        verify(mockCompagnySearchRepository, times(1)).save(testCompagny);
    }

    @Test
    @Transactional
    public void updateNonExistingCompagny() throws Exception {
        int databaseSizeBeforeUpdate = compagnyRepository.findAll().size();

        // Create the Compagny

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompagnyMockMvc.perform(put("/api/compagnies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compagny)))
            .andExpect(status().isBadRequest());

        // Validate the Compagny in the database
        List<Compagny> compagnyList = compagnyRepository.findAll();
        assertThat(compagnyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Compagny in Elasticsearch
        verify(mockCompagnySearchRepository, times(0)).save(compagny);
    }

    @Test
    @Transactional
    public void deleteCompagny() throws Exception {
        // Initialize the database
        compagnyRepository.saveAndFlush(compagny);

        int databaseSizeBeforeDelete = compagnyRepository.findAll().size();

        // Get the compagny
        restCompagnyMockMvc.perform(delete("/api/compagnies/{id}", compagny.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Compagny> compagnyList = compagnyRepository.findAll();
        assertThat(compagnyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Compagny in Elasticsearch
        verify(mockCompagnySearchRepository, times(1)).deleteById(compagny.getId());
    }

    @Test
    @Transactional
    public void searchCompagny() throws Exception {
        // Initialize the database
        compagnyRepository.saveAndFlush(compagny);
        when(mockCompagnySearchRepository.search(queryStringQuery("id:" + compagny.getId())))
            .thenReturn(Collections.singletonList(compagny));
        // Search the compagny
        restCompagnyMockMvc.perform(get("/api/_search/compagnies?query=id:" + compagny.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compagny.getId().intValue())))
            .andExpect(jsonPath("$.[*].compagnyName").value(hasItem(DEFAULT_COMPAGNY_NAME)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].vAT").value(hasItem(DEFAULT_V_AT)))
            .andExpect(jsonPath("$.[*].compagnyRegistry").value(hasItem(DEFAULT_COMPAGNY_REGISTRY)))
            .andExpect(jsonPath("$.[*].siret").value(hasItem(DEFAULT_SIRET)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].defaultIncoterm").value(hasItem(DEFAULT_DEFAULT_INCOTERM.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compagny.class);
        Compagny compagny1 = new Compagny();
        compagny1.setId(1L);
        Compagny compagny2 = new Compagny();
        compagny2.setId(compagny1.getId());
        assertThat(compagny1).isEqualTo(compagny2);
        compagny2.setId(2L);
        assertThat(compagny1).isNotEqualTo(compagny2);
        compagny1.setId(null);
        assertThat(compagny1).isNotEqualTo(compagny2);
    }
}
