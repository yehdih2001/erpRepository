package io.erp.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.erp.application.domain.ProductReodringRules;
import io.erp.application.repository.ProductReodringRulesRepository;
import io.erp.application.repository.search.ProductReodringRulesSearchRepository;
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
 * REST controller for managing ProductReodringRules.
 */
@RestController
@RequestMapping("/api")
public class ProductReodringRulesResource {

    private final Logger log = LoggerFactory.getLogger(ProductReodringRulesResource.class);

    private static final String ENTITY_NAME = "erpProductReodringRules";

    private final ProductReodringRulesRepository productReodringRulesRepository;

    private final ProductReodringRulesSearchRepository productReodringRulesSearchRepository;

    public ProductReodringRulesResource(ProductReodringRulesRepository productReodringRulesRepository, ProductReodringRulesSearchRepository productReodringRulesSearchRepository) {
        this.productReodringRulesRepository = productReodringRulesRepository;
        this.productReodringRulesSearchRepository = productReodringRulesSearchRepository;
    }

    /**
     * POST  /product-reodring-rules : Create a new productReodringRules.
     *
     * @param productReodringRules the productReodringRules to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productReodringRules, or with status 400 (Bad Request) if the productReodringRules has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-reodring-rules")
    @Timed
    public ResponseEntity<ProductReodringRules> createProductReodringRules(@Valid @RequestBody ProductReodringRules productReodringRules) throws URISyntaxException {
        log.debug("REST request to save ProductReodringRules : {}", productReodringRules);
        if (productReodringRules.getId() != null) {
            throw new BadRequestAlertException("A new productReodringRules cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductReodringRules result = productReodringRulesRepository.save(productReodringRules);
        productReodringRulesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/product-reodring-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-reodring-rules : Updates an existing productReodringRules.
     *
     * @param productReodringRules the productReodringRules to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productReodringRules,
     * or with status 400 (Bad Request) if the productReodringRules is not valid,
     * or with status 500 (Internal Server Error) if the productReodringRules couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-reodring-rules")
    @Timed
    public ResponseEntity<ProductReodringRules> updateProductReodringRules(@Valid @RequestBody ProductReodringRules productReodringRules) throws URISyntaxException {
        log.debug("REST request to update ProductReodringRules : {}", productReodringRules);
        if (productReodringRules.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductReodringRules result = productReodringRulesRepository.save(productReodringRules);
        productReodringRulesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productReodringRules.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-reodring-rules : get all the productReodringRules.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productReodringRules in body
     */
    @GetMapping("/product-reodring-rules")
    @Timed
    public List<ProductReodringRules> getAllProductReodringRules() {
        log.debug("REST request to get all ProductReodringRules");
        return productReodringRulesRepository.findAll();
    }

    /**
     * GET  /product-reodring-rules/:id : get the "id" productReodringRules.
     *
     * @param id the id of the productReodringRules to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productReodringRules, or with status 404 (Not Found)
     */
    @GetMapping("/product-reodring-rules/{id}")
    @Timed
    public ResponseEntity<ProductReodringRules> getProductReodringRules(@PathVariable Long id) {
        log.debug("REST request to get ProductReodringRules : {}", id);
        Optional<ProductReodringRules> productReodringRules = productReodringRulesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productReodringRules);
    }

    /**
     * DELETE  /product-reodring-rules/:id : delete the "id" productReodringRules.
     *
     * @param id the id of the productReodringRules to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-reodring-rules/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductReodringRules(@PathVariable Long id) {
        log.debug("REST request to delete ProductReodringRules : {}", id);

        productReodringRulesRepository.deleteById(id);
        productReodringRulesSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-reodring-rules?query=:query : search for the productReodringRules corresponding
     * to the query.
     *
     * @param query the query of the productReodringRules search
     * @return the result of the search
     */
    @GetMapping("/_search/product-reodring-rules")
    @Timed
    public List<ProductReodringRules> searchProductReodringRules(@RequestParam String query) {
        log.debug("REST request to search ProductReodringRules for query {}", query);
        return StreamSupport
            .stream(productReodringRulesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
