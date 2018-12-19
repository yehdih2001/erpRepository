package io.erp.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import io.erp.application.domain.enumeration.ProductType;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "can_be_sold")
    private Boolean canBeSold;

    @Column(name = "can_be_purchased")
    private Boolean canBePurchased;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "internal_reference")
    private String internalReference;

    @Column(name = "sales_price")
    private Double salesPrice;

    @Column(name = "jhi_cost")
    private Double cost;

    @Lob
    @Column(name = "bar_code")
    private byte[] barCode;

    @Column(name = "bar_code_content_type")
    private String barCodeContentType;

    @Lob
    @Column(name = "internal_notes")
    private String internalNotes;

    @Column(name = "active")
    private Boolean active;

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

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isCanBeSold() {
        return canBeSold;
    }

    public Product canBeSold(Boolean canBeSold) {
        this.canBeSold = canBeSold;
        return this;
    }

    public void setCanBeSold(Boolean canBeSold) {
        this.canBeSold = canBeSold;
    }

    public Boolean isCanBePurchased() {
        return canBePurchased;
    }

    public Product canBePurchased(Boolean canBePurchased) {
        this.canBePurchased = canBePurchased;
        return this;
    }

    public void setCanBePurchased(Boolean canBePurchased) {
        this.canBePurchased = canBePurchased;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Product productType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public Product productCategory(String productCategory) {
        this.productCategory = productCategory;
        return this;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public Product internalReference(String internalReference) {
        this.internalReference = internalReference;
        return this;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public Product salesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
        return this;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Double getCost() {
        return cost;
    }

    public Product cost(Double cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public byte[] getBarCode() {
        return barCode;
    }

    public Product barCode(byte[] barCode) {
        this.barCode = barCode;
        return this;
    }

    public void setBarCode(byte[] barCode) {
        this.barCode = barCode;
    }

    public String getBarCodeContentType() {
        return barCodeContentType;
    }

    public Product barCodeContentType(String barCodeContentType) {
        this.barCodeContentType = barCodeContentType;
        return this;
    }

    public void setBarCodeContentType(String barCodeContentType) {
        this.barCodeContentType = barCodeContentType;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public Product internalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
        return this;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public Boolean isActive() {
        return active;
    }

    public Product active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", canBeSold='" + isCanBeSold() + "'" +
            ", canBePurchased='" + isCanBePurchased() + "'" +
            ", productType='" + getProductType() + "'" +
            ", productCategory='" + getProductCategory() + "'" +
            ", internalReference='" + getInternalReference() + "'" +
            ", salesPrice=" + getSalesPrice() +
            ", cost=" + getCost() +
            ", barCode='" + getBarCode() + "'" +
            ", barCodeContentType='" + getBarCodeContentType() + "'" +
            ", internalNotes='" + getInternalNotes() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
