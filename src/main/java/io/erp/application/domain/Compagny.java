package io.erp.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import io.erp.application.domain.enumeration.Incoterm;

/**
 * A Compagny.
 */
@Entity
@Table(name = "compagny")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "compagny")
public class Compagny implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "compagny_name", nullable = false)
    private String compagnyName;

    @Column(name = "website")
    private String website;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Column(name = "street")
    private String street;

    @Column(name = "street_2")
    private String street2;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state")
    private String state;

    @NotNull
    @Size(min = 5)
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @NotNull
    @Size(min = 3)
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "v_at")
    private String vAT;

    @Column(name = "compagny_registry")
    private String compagnyRegistry;

    @Column(name = "siret")
    private String siret;

    @Column(name = "currency")
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_incoterm")
    private Incoterm defaultIncoterm;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties("compagnies")
    private Erp erp;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompagnyName() {
        return compagnyName;
    }

    public Compagny compagnyName(String compagnyName) {
        this.compagnyName = compagnyName;
        return this;
    }

    public void setCompagnyName(String compagnyName) {
        this.compagnyName = compagnyName;
    }

    public String getWebsite() {
        return website;
    }

    public Compagny website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public byte[] getLogo() {
        return logo;
    }

    public Compagny logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public Compagny logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public String getStreet() {
        return street;
    }

    public Compagny street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet2() {
        return street2;
    }

    public Compagny street2(String street2) {
        this.street2 = street2;
        return this;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public Compagny city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public Compagny state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Compagny zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public Compagny country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public Compagny phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public Compagny email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getvAT() {
        return vAT;
    }

    public Compagny vAT(String vAT) {
        this.vAT = vAT;
        return this;
    }

    public void setvAT(String vAT) {
        this.vAT = vAT;
    }

    public String getCompagnyRegistry() {
        return compagnyRegistry;
    }

    public Compagny compagnyRegistry(String compagnyRegistry) {
        this.compagnyRegistry = compagnyRegistry;
        return this;
    }

    public void setCompagnyRegistry(String compagnyRegistry) {
        this.compagnyRegistry = compagnyRegistry;
    }

    public String getSiret() {
        return siret;
    }

    public Compagny siret(String siret) {
        this.siret = siret;
        return this;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getCurrency() {
        return currency;
    }

    public Compagny currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Incoterm getDefaultIncoterm() {
        return defaultIncoterm;
    }

    public Compagny defaultIncoterm(Incoterm defaultIncoterm) {
        this.defaultIncoterm = defaultIncoterm;
        return this;
    }

    public void setDefaultIncoterm(Incoterm defaultIncoterm) {
        this.defaultIncoterm = defaultIncoterm;
    }

    public Boolean isActive() {
        return active;
    }

    public Compagny active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Erp getErp() {
        return erp;
    }

    public Compagny erp(Erp erp) {
        this.erp = erp;
        return this;
    }

    public void setErp(Erp erp) {
        this.erp = erp;
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
        Compagny compagny = (Compagny) o;
        if (compagny.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), compagny.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Compagny{" +
            "id=" + getId() +
            ", compagnyName='" + getCompagnyName() + "'" +
            ", website='" + getWebsite() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", street='" + getStreet() + "'" +
            ", street2='" + getStreet2() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", vAT='" + getvAT() + "'" +
            ", compagnyRegistry='" + getCompagnyRegistry() + "'" +
            ", siret='" + getSiret() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", defaultIncoterm='" + getDefaultIncoterm() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
