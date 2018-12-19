package io.erp.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProductCount.
 */
@Entity
@Table(name = "product_count")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productcount")
public class ProductCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "on_hand")
    private Integer onHand;

    @Column(name = "purchased")
    private Integer purchased;

    @Column(name = "forecasted")
    private Integer forecasted;

    @Column(name = "sold")
    private Integer sold;

    @OneToOne    @JoinColumn(unique = true)
    private ProductInventoryView product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOnHand() {
        return onHand;
    }

    public ProductCount onHand(Integer onHand) {
        this.onHand = onHand;
        return this;
    }

    public void setOnHand(Integer onHand) {
        this.onHand = onHand;
    }

    public Integer getPurchased() {
        return purchased;
    }

    public ProductCount purchased(Integer purchased) {
        this.purchased = purchased;
        return this;
    }

    public void setPurchased(Integer purchased) {
        this.purchased = purchased;
    }

    public Integer getForecasted() {
        return forecasted;
    }

    public ProductCount forecasted(Integer forecasted) {
        this.forecasted = forecasted;
        return this;
    }

    public void setForecasted(Integer forecasted) {
        this.forecasted = forecasted;
    }

    public Integer getSold() {
        return sold;
    }

    public ProductCount sold(Integer sold) {
        this.sold = sold;
        return this;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public ProductInventoryView getProduct() {
        return product;
    }

    public ProductCount product(ProductInventoryView productInventoryView) {
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
        ProductCount productCount = (ProductCount) o;
        if (productCount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productCount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductCount{" +
            "id=" + getId() +
            ", onHand=" + getOnHand() +
            ", purchased=" + getPurchased() +
            ", forecasted=" + getForecasted() +
            ", sold=" + getSold() +
            "}";
    }
}
