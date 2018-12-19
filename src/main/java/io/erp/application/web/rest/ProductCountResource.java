package io.erp.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.erp.application.domain.ProductCount;
import io.erp.application.repository.ProductCountRepository;
import io.erp.application.repository.search.ProductCountSearchRepository;
import io.erp.application.web.rest.errors.BadRequestAlertException;
import io.erp.application.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProductCount.
 */
@RestController
@RequestMapping("/api")
public class ProductCountResource {

    private final Logger log = LoggerFactory.getLogger(ProductCountResource.class);

    private static final String ENTITY_NAME = "erpProductCount";

    private final ProductCountRepository productCountRepository;

    private final ProductCountSearchRepository productCountSearchRepository;

    public ProductCountResource(ProductCountRepository productCountRepository, ProductCountSearchRepository productCountSearchRepository) {
        this.productCountRepository = productCountRepository;
        this.productCountSearchRepository = productCountSearchRepository;
    }

    /**
     * POST  /product-counts : Create a new productCount.
     *
     * @param productCount the productCount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productCount, or with status 400 (Bad Request) if the productCount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-counts")
    @Timed
    public ResponseEntity<ProductCount> createProductCount(@RequestBody ProductCount productCount) throws URISyntaxException {
        log.debug("REST request to save ProductCount : {}", productCount);
        if (productCount.getId() != null) {
            throw new BadRequestAlertException("A new productCount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductCount result = productCountRepository.save(productCount);
        productCountSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/product-counts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-counts : Updates an existing productCount.
     *
     * @param productCount the productCount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productCount,
     * or with status 400 (Bad Request) if the productCount is not valid,
     * or with status 500 (Internal Server Error) if the productCount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-counts")
    @Timed
    public ResponseEntity<ProductCount> updateProductCount(@RequestBody ProductCount productCount) throws URISyntaxException {
        log.debug("REST request to update ProductCount : {}", productCount);
        if (productCount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductCount result = productCountRepository.save(productCount);
        productCountSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productCount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-counts : get all the productCounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productCounts in body
     */
    @GetMapping("/product-counts")
    @Timed
    public List<ProductCount> getAllProductCounts() {
        log.debug("REST request to get all ProductCounts");
        return productCountRepository.findAll();
    }

    /**
     * GET  /product-counts/:id : get the "id" productCount.
     *
     * @param id the id of the productCount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productCount, or with status 404 (Not Found)
     */
    @GetMapping("/product-counts/{id}")
    @Timed
    public ResponseEntity<ProductCount> getProductCount(@PathVariable Long id) {
        log.debug("REST request to get ProductCount : {}", id);
        Optional<ProductCount> productCount = productCountRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productCount);
    }

    /**
     * DELETE  /product-counts/:id : delete the "id" productCount.
     *
     * @param id the id of the productCount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-counts/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductCount(@PathVariable Long id) {
        log.debug("REST request to delete ProductCount : {}", id);

        productCountRepository.deleteById(id);
        productCountSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-counts?query=:query : search for the productCount corresponding
     * to the query.
     *
     * @param query the query of the productCount search
     * @return the result of the search
     */
    @GetMapping("/_search/product-counts")
    @Timed
    public List<ProductCount> searchProductCounts(@RequestParam String query) {
        log.debug("REST request to search ProductCounts for query {}", query);
        return StreamSupport
            .stream(productCountSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
