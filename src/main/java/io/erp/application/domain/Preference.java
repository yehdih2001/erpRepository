package io.erp.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import io.erp.application.domain.enumeration.NotificationManagement;

/**
 * A Preference.
 */
@Entity
@Table(name = "preference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "preference")
public class Preference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "language", nullable = false)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification")
    private NotificationManagement notification;

    @Column(name = "alias")
    private String alias;

    @Lob
    @Column(name = "signature")
    private String signature;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public Preference language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public NotificationManagement getNotification() {
        return notification;
    }

    public Preference notification(NotificationManagement notification) {
        this.notification = notification;
        return this;
    }

    public void setNotification(NotificationManagement notification) {
        this.notification = notification;
    }

    public String getAlias() {
        return alias;
    }

    public Preference alias(String alias) {
        this.alias = alias;
        return this;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSignature() {
        return signature;
    }

    public Preference signature(String signature) {
        this.signature = signature;
        return this;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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
        Preference preference = (Preference) o;
        if (preference.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preference.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Preference{" +
            "id=" + getId() +
            ", language='" + getLanguage() + "'" +
            ", notification='" + getNotification() + "'" +
            ", alias='" + getAlias() + "'" +
            ", signature='" + getSignature() + "'" +
            "}";
    }
}
