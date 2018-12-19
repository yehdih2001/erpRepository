package io.erp.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProductInventoryView.
 */
@Entity
@Table(name = "product_inventory_view")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productinventoryview")
public class ProductInventoryView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "weight_in_kg")
    private Double weightInKg;

    @Column(name = "volume_in_metercube")
    private Double volumeInMetercube;

    @Lob
    @Column(name = "description_for_delivery_orders")
    private String descriptionForDeliveryOrders;

    @Lob
    @Column(name = "description_for_receipts")
    private String descriptionForReceipts;

    @Column(name = "customer_lead_time")
    private Integer customerLeadTime;

    @OneToOne    @JoinColumn(unique = true)
    private Product product;

    @OneToOne    @JoinColumn(unique = true)
    private AppUser responsible;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeightInKg() {
        return weightInKg;
    }

    public ProductInventoryView weightInKg(Double weightInKg) {
        this.weightInKg = weightInKg;
        return this;
    }

    public void setWeightInKg(Double weightInKg) {
        this.weightInKg = weightInKg;
    }

    public Double getVolumeInMetercube() {
        return volumeInMetercube;
    }

    public ProductInventoryView volumeInMetercube(Double volumeInMetercube) {
        this.volumeInMetercube = volumeInMetercube;
        return this;
    }

    public void setVolumeInMetercube(Double volumeInMetercube) {
        this.volumeInMetercube = volumeInMetercube;
    }

    public String getDescriptionForDeliveryOrders() {
        return descriptionForDeliveryOrders;
    }

    public ProductInventoryView descriptionForDeliveryOrders(String descriptionForDeliveryOrders) {
        this.descriptionForDeliveryOrders = descriptionForDeliveryOrders;
        return this;
    }

    public void setDescriptionForDeliveryOrders(String descriptionForDeliveryOrders) {
        this.descriptionForDeliveryOrders = descriptionForDeliveryOrders;
    }

    public String getDescriptionForReceipts() {
        return descriptionForReceipts;
    }

    public ProductInventoryView descriptionForReceipts(String descriptionForReceipts) {
        this.descriptionForReceipts = descriptionForReceipts;
        return this;
    }

    public void setDescriptionForReceipts(String descriptionForReceipts) {
        this.descriptionForReceipts = descriptionForReceipts;
    }

    public Integer getCustomerLeadTime() {
        return customerLeadTime;
    }

    public ProductInventoryView customerLeadTime(Integer customerLeadTime) {
        this.customerLeadTime = customerLeadTime;
        return this;
    }

    public void setCustomerLeadTime(Integer customerLeadTime) {
        this.customerLeadTime = customerLeadTime;
    }

    public Product getProduct() {
        return product;
    }

    public ProductInventoryView product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public AppUser getResponsible() {
        return responsible;
    }

    public ProductInventoryView responsible(AppUser appUser) {
        this.responsible = appUser;
        return this;
    }

    public void setResponsible(AppUser appUser) {
        this.responsible = appUser;
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
        ProductInventoryView productInventoryView = (ProductInventoryView) o;
        if (productInventoryView.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productInventoryView.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductInventoryView{" +
            "id=" + getId() +
            ", weightInKg=" + getWeightInKg() +
            ", volumeInMetercube=" + getVolumeInMetercube() +
            ", descriptionForDeliveryOrders='" + getDescriptionForDeliveryOrders() + "'" +
            ", descriptionForReceipts='" + getDescriptionForReceipts() + "'" +
            ", customerLeadTime=" + getCustomerLeadTime() +
            "}";
    }
}
