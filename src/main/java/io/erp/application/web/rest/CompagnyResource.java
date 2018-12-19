package io.erp.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.erp.application.domain.Compagny;
import io.erp.application.repository.CompagnyRepository;
import io.erp.application.repository.search.CompagnySearchRepository;
import io.erp.application.web.rest.errors.BadRequestAlertException;
import io.erp.application.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Compagny.
 */
@RestController
@RequestMapping("/api")
public class CompagnyResource {

    private final Logger log = LoggerFactory.getLogger(CompagnyResource.class);

    private static final String ENTITY_NAME = "erpCompagny";

    private final CompagnyRepository compagnyRepository;

    private final CompagnySearchRepository compagnySearchRepository;

    public CompagnyResource(CompagnyRepository compagnyRepository, CompagnySearchRepository compagnySearchRepository) {
        this.compagnyRepository = compagnyRepository;
        this.compagnySearchRepository = compagnySearchRepository;
    }

    /**
     * POST  /compagnies : Create a new compagny.
     *
     * @param compagny the compagny to create
     * @return the ResponseEntity with status 201 (Created) and with body the new compagny, or with status 400 (Bad Request) if the compagny has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/compagnies")
    @Timed
    public ResponseEntity<Compagny> createCompagny(@Valid @RequestBody Compagny compagny) throws URISyntaxException {
        log.debug("REST request to save Compagny : {}", compagny);
        if (compagny.getId() != null) {
            throw new BadRequestAlertException("A new compagny cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Compagny result = compagnyRepository.save(compagny);
        compagnySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/compagnies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /compagnies : Updates an existing compagny.
     *
     * @param compagny the compagny to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated compagny,
     * or with status 400 (Bad Request) if the compagny is not valid,
     * or with status 500 (Internal Server Error) if the compagny couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/compagnies")
    @Timed
    public ResponseEntity<Compagny> updateCompagny(@Valid @RequestBody Compagny compagny) throws URISyntaxException {
        log.debug("REST request to update Compagny : {}", compagny);
        if (compagny.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Compagny result = compagnyRepository.save(compagny);
        compagnySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, compagny.getId().toString()))
            .body(result);
    }

    /**
     * GET  /compagnies : get all the compagnies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of compagnies in body
     */
    @GetMapping("/compagnies")
    @Timed
    public List<Compagny> getAllCompagnies() {
        log.debug("REST request to get all Compagnies");
        return compagnyRepository.findAll();
    }

    /**
     * GET  /compagnies/:id : get the "id" compagny.
     *
     * @param id the id of the compagny to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the compagny, or with status 404 (Not Found)
     */
    @GetMapping("/compagnies/{id}")
    @Timed
    public ResponseEntity<Compagny> getCompagny(@PathVariable Long id) {
        log.debug("REST request to get Compagny : {}", id);
        Optional<Compagny> compagny = compagnyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(compagny);
    }

    /**
     * DELETE  /compagnies/:id : delete the "id" compagny.
     *
     * @param id the id of the compagny to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/compagnies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompagny(@PathVariable Long id) {
        log.debug("REST request to delete Compagny : {}", id);

        compagnyRepository.deleteById(id);
        compagnySearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/compagnies?query=:query : search for the compagny corresponding
     * to the query.
     *
     * @param query the query of the compagny search
     * @return the result of the search
     */
    @GetMapping("/_search/compagnies")
    @Timed
    public List<Compagny> searchCompagnies(@RequestParam String query) {
        log.debug("REST request to search Compagnies for query {}", query);
        return StreamSupport
            .stream(compagnySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
