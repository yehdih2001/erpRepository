package io.erp.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.erp.application.domain.ProductInventoryView;
import io.erp.application.repository.ProductInventoryViewRepository;
import io.erp.application.repository.search.ProductInventoryViewSearchRepository;
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
 * REST controller for managing ProductInventoryView.
 */
@RestController
@RequestMapping("/api")
public class ProductInventoryViewResource {

    private final Logger log = LoggerFactory.getLogger(ProductInventoryViewResource.class);

    private static final String ENTITY_NAME = "erpProductInventoryView";

    private final ProductInventoryViewRepository productInventoryViewRepository;

    private final ProductInventoryViewSearchRepository productInventoryViewSearchRepository;

    public ProductInventoryViewResource(ProductInventoryViewRepository productInventoryViewRepository, ProductInventoryViewSearchRepository productInventoryViewSearchRepository) {
        this.productInventoryViewRepository = productInventoryViewRepository;
        this.productInventoryViewSearchRepository = productInventoryViewSearchRepository;
    }

    /**
     * POST  /product-inventory-views : Create a new productInventoryView.
     *
     * @param productInventoryView the productInventoryView to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productInventoryView, or with status 400 (Bad Request) if the productInventoryView has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-inventory-views")
    @Timed
    public ResponseEntity<ProductInventoryView> createProductInventoryView(@RequestBody ProductInventoryView productInventoryView) throws URISyntaxException {
        log.debug("REST request to save ProductInventoryView : {}", productInventoryView);
        if (productInventoryView.getId() != null) {
            throw new BadRequestAlertException("A new productInventoryView cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductInventoryView result = productInventoryViewRepository.save(productInventoryView);
        productInventoryViewSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/product-inventory-views/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-inventory-views : Updates an existing productInventoryView.
     *
     * @param productInventoryView the productInventoryView to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productInventoryView,
     * or with status 400 (Bad Request) if the productInventoryView is not valid,
     * or with status 500 (Internal Server Error) if the productInventoryView couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-inventory-views")
    @Timed
    public ResponseEntity<ProductInventoryView> updateProductInventoryView(@RequestBody ProductInventoryView productInventoryView) throws URISyntaxException {
        log.debug("REST request to update ProductInventoryView : {}", productInventoryView);
        if (productInventoryView.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductInventoryView result = productInventoryViewRepository.save(productInventoryView);
        productInventoryViewSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productInventoryView.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-inventory-views : get all the productInventoryViews.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productInventoryViews in body
     */
    @GetMapping("/product-inventory-views")
    @Timed
    public List<ProductInventoryView> getAllProductInventoryViews() {
        log.debug("REST request to get all ProductInventoryViews");
        return productInventoryViewRepository.findAll();
    }

    /**
     * GET  /product-inventory-views/:id : get the "id" productInventoryView.
     *
     * @param id the id of the productInventoryView to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productInventoryView, or with status 404 (Not Found)
     */
    @GetMapping("/product-inventory-views/{id}")
    @Timed
    public ResponseEntity<ProductInventoryView> getProductInventoryView(@PathVariable Long id) {
        log.debug("REST request to get ProductInventoryView : {}", id);
        Optional<ProductInventoryView> productInventoryView = productInventoryViewRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productInventoryView);
    }

    /**
     * DELETE  /product-inventory-views/:id : delete the "id" productInventoryView.
     *
     * @param id the id of the productInventoryView to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-inventory-views/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductInventoryView(@PathVariable Long id) {
        log.debug("REST request to delete ProductInventoryView : {}", id);

        productInventoryViewRepository.deleteById(id);
        productInventoryViewSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-inventory-views?query=:query : search for the productInventoryView corresponding
     * to the query.
     *
     * @param query the query of the productInventoryView search
     * @return the result of the search
     */
    @GetMapping("/_search/product-inventory-views")
    @Timed
    public List<ProductInventoryView> searchProductInventoryViews(@RequestParam String query) {
        log.debug("REST request to search ProductInventoryViews for query {}", query);
        return StreamSupport
            .stream(productInventoryViewSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
