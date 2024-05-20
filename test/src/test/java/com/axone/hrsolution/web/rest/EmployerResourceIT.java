package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.EmployerAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Employer;
import com.axone.hrsolution.domain.Profile;
import com.axone.hrsolution.domain.Wallet;
import com.axone.hrsolution.repository.EmployerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmployerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployerResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN_URL = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN_URL = "BBBBBBBBBB";

    private static final Float DEFAULT_SCORE = 1F;
    private static final Float UPDATED_SCORE = 2F;

    private static final String ENTITY_API_URL = "/api/employers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployerMockMvc;

    private Employer employer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employer createEntity(EntityManager em) {
        Employer employer = new Employer().label(DEFAULT_LABEL).linkedinUrl(DEFAULT_LINKEDIN_URL).score(DEFAULT_SCORE);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        employer.setRelatedUser(profile);
        // Add required entity
        Wallet wallet;
        if (TestUtil.findAll(em, Wallet.class).isEmpty()) {
            wallet = WalletResourceIT.createEntity(em);
            em.persist(wallet);
            em.flush();
        } else {
            wallet = TestUtil.findAll(em, Wallet.class).get(0);
        }
        employer.setWallet(wallet);
        return employer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employer createUpdatedEntity(EntityManager em) {
        Employer employer = new Employer().label(UPDATED_LABEL).linkedinUrl(UPDATED_LINKEDIN_URL).score(UPDATED_SCORE);
        // Add required entity
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createUpdatedEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        employer.setRelatedUser(profile);
        // Add required entity
        Wallet wallet;
        if (TestUtil.findAll(em, Wallet.class).isEmpty()) {
            wallet = WalletResourceIT.createUpdatedEntity(em);
            em.persist(wallet);
            em.flush();
        } else {
            wallet = TestUtil.findAll(em, Wallet.class).get(0);
        }
        employer.setWallet(wallet);
        return employer;
    }

    @BeforeEach
    public void initTest() {
        employer = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Employer
        var returnedEmployer = om.readValue(
            restEmployerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Employer.class
        );

        // Validate the Employer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployerUpdatableFieldsEquals(returnedEmployer, getPersistedEmployer(returnedEmployer));
    }

    @Test
    @Transactional
    void createEmployerWithExistingId() throws Exception {
        // Create the Employer with an existing ID
        employer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employer.setLabel(null);

        // Create the Employer, which fails.

        restEmployerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployers() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employerList
        restEmployerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employer.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].linkedinUrl").value(hasItem(DEFAULT_LINKEDIN_URL)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.doubleValue())));
    }

    @Test
    @Transactional
    void getEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get the employer
        restEmployerMockMvc
            .perform(get(ENTITY_API_URL_ID, employer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employer.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.linkedinUrl").value(DEFAULT_LINKEDIN_URL))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmployer() throws Exception {
        // Get the employer
        restEmployerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employer
        Employer updatedEmployer = employerRepository.findById(employer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployer are not directly saved in db
        em.detach(updatedEmployer);
        updatedEmployer.label(UPDATED_LABEL).linkedinUrl(UPDATED_LINKEDIN_URL).score(UPDATED_SCORE);

        restEmployerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployer))
            )
            .andExpect(status().isOk());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployerToMatchAllProperties(updatedEmployer);
    }

    @Test
    @Transactional
    void putNonExistingEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employer.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployerWithPatch() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employer using partial update
        Employer partialUpdatedEmployer = new Employer();
        partialUpdatedEmployer.setId(employer.getId());

        restEmployerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployer))
            )
            .andExpect(status().isOk());

        // Validate the Employer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmployer, employer), getPersistedEmployer(employer));
    }

    @Test
    @Transactional
    void fullUpdateEmployerWithPatch() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employer using partial update
        Employer partialUpdatedEmployer = new Employer();
        partialUpdatedEmployer.setId(employer.getId());

        partialUpdatedEmployer.label(UPDATED_LABEL).linkedinUrl(UPDATED_LINKEDIN_URL).score(UPDATED_SCORE);

        restEmployerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployer))
            )
            .andExpect(status().isOk());

        // Validate the Employer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployerUpdatableFieldsEquals(partialUpdatedEmployer, getPersistedEmployer(partialUpdatedEmployer));
    }

    @Test
    @Transactional
    void patchNonExistingEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employer
        restEmployerMockMvc
            .perform(delete(ENTITY_API_URL_ID, employer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employerRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Employer getPersistedEmployer(Employer employer) {
        return employerRepository.findById(employer.getId()).orElseThrow();
    }

    protected void assertPersistedEmployerToMatchAllProperties(Employer expectedEmployer) {
        assertEmployerAllPropertiesEquals(expectedEmployer, getPersistedEmployer(expectedEmployer));
    }

    protected void assertPersistedEmployerToMatchUpdatableProperties(Employer expectedEmployer) {
        assertEmployerAllUpdatablePropertiesEquals(expectedEmployer, getPersistedEmployer(expectedEmployer));
    }
}
