package com.axone.hrsolution.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Resume.
 */
@Entity
@Table(name = "resume")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "file", nullable = false)
    private byte[] file;

    @NotNull
    @Column(name = "file_content_type", nullable = false)
    private String fileContentType;

    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "techCV", "interviewResults", "candidateCVS", "domains", "applications", "contract", "ndaStatuses" },
        allowSetters = true
    )
    private Candidate owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resume id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return this.file;
    }

    public Resume file(byte[] file) {
        this.setFile(file);
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public Resume fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFilename() {
        return this.filename;
    }

    public Resume filename(String filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Candidate getOwner() {
        return this.owner;
    }

    public void setOwner(Candidate candidate) {
        this.owner = candidate;
    }

    public Resume owner(Candidate candidate) {
        this.setOwner(candidate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resume)) {
            return false;
        }
        return getId() != null && getId().equals(((Resume) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resume{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            ", filename='" + getFilename() + "'" +
            "}";
    }
}
