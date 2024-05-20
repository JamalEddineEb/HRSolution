package com.axone.hrsolution.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employer.
 */
@Entity
@Table(name = "employer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "score")
    private Float score;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private User internalUser;

    @JsonIgnoreProperties(value = { "relatedToAccount", "recruiter", "employer", "admin" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Wallet wallet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "recruiters", "candidates", "applications", "employer" }, allowSetters = true)
    private Set<Domain> operationalDomains = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ifEmployer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "owner", "types", "providers", "relatedWallet", "ifEmployer" }, allowSetters = true)
    private Set<AppAccount> paymentAccounts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "contracts",
            "interviews",
            "contractTypes",
            "contractTemplates",
            "criteria",
            "domains",
            "employer",
            "recruiters",
            "candidates",
            "request",
        },
        allowSetters = true
    )
    private Set<Application> applications = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "template", "candidate", "recruiter", "employer", "application" }, allowSetters = true)
    private Set<Contract> contracts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "owner", "applications" }, allowSetters = true)
    private Set<Template> templates = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employer", "mediator", "candidate" }, allowSetters = true)
    private Set<NDA> ndaStatuses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Employer label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLinkedinUrl() {
        return this.linkedinUrl;
    }

    public Employer linkedinUrl(String linkedinUrl) {
        this.setLinkedinUrl(linkedinUrl);
        return this;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public Float getScore() {
        return this.score;
    }

    public Employer score(Float score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Employer internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Employer wallet(Wallet wallet) {
        this.setWallet(wallet);
        return this;
    }

    public Set<Domain> getOperationalDomains() {
        return this.operationalDomains;
    }

    public void setOperationalDomains(Set<Domain> domains) {
        if (this.operationalDomains != null) {
            this.operationalDomains.forEach(i -> i.setEmployer(null));
        }
        if (domains != null) {
            domains.forEach(i -> i.setEmployer(this));
        }
        this.operationalDomains = domains;
    }

    public Employer operationalDomains(Set<Domain> domains) {
        this.setOperationalDomains(domains);
        return this;
    }

    public Employer addOperationalDomain(Domain domain) {
        this.operationalDomains.add(domain);
        domain.setEmployer(this);
        return this;
    }

    public Employer removeOperationalDomain(Domain domain) {
        this.operationalDomains.remove(domain);
        domain.setEmployer(null);
        return this;
    }

    public Set<AppAccount> getPaymentAccounts() {
        return this.paymentAccounts;
    }

    public void setPaymentAccounts(Set<AppAccount> appAccounts) {
        if (this.paymentAccounts != null) {
            this.paymentAccounts.forEach(i -> i.setIfEmployer(null));
        }
        if (appAccounts != null) {
            appAccounts.forEach(i -> i.setIfEmployer(this));
        }
        this.paymentAccounts = appAccounts;
    }

    public Employer paymentAccounts(Set<AppAccount> appAccounts) {
        this.setPaymentAccounts(appAccounts);
        return this;
    }

    public Employer addPaymentAccount(AppAccount appAccount) {
        this.paymentAccounts.add(appAccount);
        appAccount.setIfEmployer(this);
        return this;
    }

    public Employer removePaymentAccount(AppAccount appAccount) {
        this.paymentAccounts.remove(appAccount);
        appAccount.setIfEmployer(null);
        return this;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.setEmployer(null));
        }
        if (applications != null) {
            applications.forEach(i -> i.setEmployer(this));
        }
        this.applications = applications;
    }

    public Employer applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public Employer addApplication(Application application) {
        this.applications.add(application);
        application.setEmployer(this);
        return this;
    }

    public Employer removeApplication(Application application) {
        this.applications.remove(application);
        application.setEmployer(null);
        return this;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setEmployer(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setEmployer(this));
        }
        this.contracts = contracts;
    }

    public Employer contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public Employer addContract(Contract contract) {
        this.contracts.add(contract);
        contract.setEmployer(this);
        return this;
    }

    public Employer removeContract(Contract contract) {
        this.contracts.remove(contract);
        contract.setEmployer(null);
        return this;
    }

    public Set<Template> getTemplates() {
        return this.templates;
    }

    public void setTemplates(Set<Template> templates) {
        if (this.templates != null) {
            this.templates.forEach(i -> i.setOwner(null));
        }
        if (templates != null) {
            templates.forEach(i -> i.setOwner(this));
        }
        this.templates = templates;
    }

    public Employer templates(Set<Template> templates) {
        this.setTemplates(templates);
        return this;
    }

    public Employer addTemplate(Template template) {
        this.templates.add(template);
        template.setOwner(this);
        return this;
    }

    public Employer removeTemplate(Template template) {
        this.templates.remove(template);
        template.setOwner(null);
        return this;
    }

    public Set<NDA> getNdaStatuses() {
        return this.ndaStatuses;
    }

    public void setNdaStatuses(Set<NDA> nDAS) {
        if (this.ndaStatuses != null) {
            this.ndaStatuses.forEach(i -> i.setEmployer(null));
        }
        if (nDAS != null) {
            nDAS.forEach(i -> i.setEmployer(this));
        }
        this.ndaStatuses = nDAS;
    }

    public Employer ndaStatuses(Set<NDA> nDAS) {
        this.setNdaStatuses(nDAS);
        return this;
    }

    public Employer addNdaStatus(NDA nDA) {
        this.ndaStatuses.add(nDA);
        nDA.setEmployer(this);
        return this;
    }

    public Employer removeNdaStatus(NDA nDA) {
        this.ndaStatuses.remove(nDA);
        nDA.setEmployer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employer)) {
            return false;
        }
        return getId() != null && getId().equals(((Employer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employer{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", linkedinUrl='" + getLinkedinUrl() + "'" +
            ", score=" + getScore() +
            "}";
    }
}
