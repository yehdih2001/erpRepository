package io.erp.application.repository;

import io.erp.application.domain.Compagny;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Compagny entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompagnyRepository extends JpaRepository<Compagny, Long> {

}
