package io.erp.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProductReodringRules.
 */
@Entity
@Table(name = "product_reodring_rules")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productreodringrules")
public class ProductReodringRules implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "minimum_quantity")
    private Integer minimumQuantity;

    @Column(name = "maximum_quantity")
    private Integer maximumQuantity;

    @Column(name = "quantity_multiple")
    private Integer quantityMultiple;

    @Column(name = "lead_time")
    private Integer leadTime;

    @Column(name = "active")
    private Boolean active;

    @OneToOne    @JoinColumn(unique = true)
    private ProductInventoryView product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ProductReodringRules name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    public ProductReodringRules minimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
        return this;
    }

    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public Integer getMaximumQuantity() {
        return maximumQuantity;
    }

    public ProductReodringRules maximumQuantity(Integer maximumQuantity) {
        this.maximumQuantity = maximumQuantity;
        return this;
    }

    public void setMaximumQuantity(Integer maximumQuantity) {
        this.maximumQuantity = maximumQuantity;
    }

    public Integer getQuantityMultiple() {
        return quantityMultiple;
    }

    public ProductReodringRules quantityMultiple(Integer quantityMultiple) {
        this.quantityMultiple = quantityMultiple;
        return this;
    }

    public void setQuantityMultiple(Integer quantityMultiple) {
        this.quantityMultiple = quantityMultiple;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public ProductReodringRules leadTime(Integer leadTime) {
        this.leadTime = leadTime;
        return this;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Boolean isActive() {
        return active;
    }

    public ProductReodringRules active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ProductInventoryView getProduct() {
        return product;
    }

    public ProductReodringRules product(ProductInventoryView productInventoryView) {
        this.product = productInventoryView;
        return this;
    }

    public void setProduct(ProductInventoryView productInventoryView) {
        this.product = productInventoryView;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductReodringRules productReodringRules = (ProductReodringRules) o;
        if (productReodringRules.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productReodringRules.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductReodringRules{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", minimumQuantity=" + getMinimumQuantity() +
            ", maximumQuantity=" + getMaximumQuantity() +
            ", quantityMultiple=" + getQuantityMultiple() +
            ", leadTime=" + getLeadTime() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
