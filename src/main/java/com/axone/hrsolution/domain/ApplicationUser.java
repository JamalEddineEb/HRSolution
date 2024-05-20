package com.axone.hrsolution.domain;

import com.axone.hrsolution.domain.enumeration.UserRole;
import com.axone.hrsolution.domain.enumeration.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @Column(name = "profile_image_content_type")
    private String profileImageContentType;

    @Column(name = "address")
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User internalUser;

    @JsonIgnoreProperties(value = { "owner", "types", "providers", "relatedWallet", "ifEmployer" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "owner")
    private AppAccount account;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getProfileImage() {
        return this.profileImage;
    }

    public ApplicationUser profileImage(byte[] profileImage) {
        this.setProfileImage(profileImage);
        return this;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageContentType() {
        return this.profileImageContentType;
    }

    public ApplicationUser profileImageContentType(String profileImageContentType) {
        this.profileImageContentType = profileImageContentType;
        return this;
    }

    public void setProfileImageContentType(String profileImageContentType) {
        this.profileImageContentType = profileImageContentType;
    }

    public String getAddress() {
        return this.address;
    }

    public ApplicationUser address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserRole getRole() {
        return this.role;
    }

    public ApplicationUser role(UserRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public ApplicationUser status(UserStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public ApplicationUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public AppAccount getAccount() {
        return this.account;
    }

    public void setAccount(AppAccount appAccount) {
        if (this.account != null) {
            this.account.setOwner(null);
        }
        if (appAccount != null) {
            appAccount.setOwner(this);
        }
        this.account = appAccount;
    }

    public ApplicationUser account(AppAccount appAccount) {
        this.setAccount(appAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", profileImage='" + getProfileImage() + "'" +
            ", profileImageContentType='" + getProfileImageContentType() + "'" +
            ", address='" + getAddress() + "'" +
            ", role='" + getRole() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
