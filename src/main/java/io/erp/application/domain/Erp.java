package io.erp.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Erp.
 */
@Entity
@Table(name = "erp")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "erp")
public class Erp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created")
    private Instant created;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "erp")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Compagny> compagnies = new HashSet<>();
    @OneToMany(mappedBy = "erp")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AppUser> users = new HashSet<>();
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

    public Erp name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return created;
    }

    public Erp created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Boolean isActive() {
        return active;
    }

    public Erp active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Compagny> getCompagnies() {
        return compagnies;
    }

    public Erp compagnies(Set<Compagny> compagnies) {
        this.compagnies = compagnies;
        return this;
    }

    public Erp addCompagnies(Compagny compagny) {
        this.compagnies.add(compagny);
        compagny.setErp(this);
        return this;
    }

    public Erp removeCompagnies(Compagny compagny) {
        this.compagnies.remove(compagny);
        compagny.setErp(null);
        return this;
    }

    public void setCompagnies(Set<Compagny> compagnies) {
        this.compagnies = compagnies;
    }

    public Set<AppUser> getUsers() {
        return users;
    }

    public Erp users(Set<AppUser> appUsers) {
        this.users = appUsers;
        return this;
    }

    public Erp addUsers(AppUser appUser) {
        this.users.add(appUser);
        appUser.setErp(this);
        return this;
    }

    public Erp removeUsers(AppUser appUser) {
        this.users.remove(appUser);
        appUser.setErp(null);
        return this;
    }

    public void setUsers(Set<AppUser> appUsers) {
        this.users = appUsers;
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
        Erp erp = (Erp) o;
        if (erp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), erp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Erp{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
