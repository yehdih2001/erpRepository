package io.erp.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.erp.application.domain.Erp;
import io.erp.application.repository.ErpRepository;
import io.erp.application.repository.search.ErpSearchRepository;
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
 * REST controller for managing Erp.
 */
@RestController
@RequestMapping("/api")
public class ErpResource {

    private final Logger log = LoggerFactory.getLogger(ErpResource.class);

    private static final String ENTITY_NAME = "erpErp";

    private final ErpRepository erpRepository;

    private final ErpSearchRepository erpSearchRepository;

    public ErpResource(ErpRepository erpRepository, ErpSearchRepository erpSearchRepository) {
        this.erpRepository = erpRepository;
        this.erpSearchRepository = erpSearchRepository;
    }

    /**
     * POST  /erps : Create a new erp.
     *
     * @param erp the erp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new erp, or with status 400 (Bad Request) if the erp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/erps")
    @Timed
    public ResponseEntity<Erp> createErp(@Valid @RequestBody Erp erp) throws URISyntaxException {
        log.debug("REST request to save Erp : {}", erp);
        if (erp.getId() != null) {
            throw new BadRequestAlertException("A new erp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Erp result = erpRepository.save(erp);
        erpSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/erps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /erps : Updates an existing erp.
     *
     * @param erp the erp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated erp,
     * or with status 400 (Bad Request) if the erp is not valid,
     * or with status 500 (Internal Server Error) if the erp couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/erps")
    @Timed
    public ResponseEntity<Erp> updateErp(@Valid @RequestBody Erp erp) throws URISyntaxException {
        log.debug("REST request to update Erp : {}", erp);
        if (erp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Erp result = erpRepository.save(erp);
        erpSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, erp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /erps : get all the erps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of erps in body
     */
    @GetMapping("/erps")
    @Timed
    public List<Erp> getAllErps() {
        log.debug("REST request to get all Erps");
        return erpRepository.findAll();
    }

    /**
     * GET  /erps/:id : get the "id" erp.
     *
     * @param id the id of the erp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the erp, or with status 404 (Not Found)
     */
    @GetMapping("/erps/{id}")
    @Timed
    public ResponseEntity<Erp> getErp(@PathVariable Long id) {
        log.debug("REST request to get Erp : {}", id);
        Optional<Erp> erp = erpRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(erp);
    }

    /**
     * DELETE  /erps/:id : delete the "id" erp.
     *
     * @param id the id of the erp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/erps/{id}")
    @Timed
    public ResponseEntity<Void> deleteErp(@PathVariable Long id) {
        log.debug("REST request to delete Erp : {}", id);

        erpRepository.deleteById(id);
        erpSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/erps?query=:query : search for the erp corresponding
     * to the query.
     *
     * @param query the query of the erp search
     * @return the result of the search
     */
    @GetMapping("/_search/erps")
    @Timed
    public List<Erp> searchErps(@RequestParam String query) {
        log.debug("REST request to search Erps for query {}", query);
        return StreamSupport
            .stream(erpSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
