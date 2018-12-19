package io.erp.application.repository;

import io.erp.application.domain.ProductReodringRules;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProductReodringRules entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductReodringRulesRepository extends JpaRepository<ProductReodringRules, Long> {

}
