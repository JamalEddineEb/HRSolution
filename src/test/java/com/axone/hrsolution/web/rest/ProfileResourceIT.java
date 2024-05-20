package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.ProfileAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Profile;
import com.axone.hrsolution.domain.User;
import com.axone.hrsolution.domain.enumeration.UserRole;
import com.axone.hrsolution.domain.enumeration.UserStatus;
import com.axone.hrsolution.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfileResourceIT {

    private static final byte[] DEFAULT_PROFILE_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final UserRole DEFAULT_ROLE = UserRole.ADMIN;
    private static final UserRole UPDATED_ROLE = UserRole.RECRUITER;

    private static final UserStatus DEFAULT_STATUS = UserStatus.ACTIVE;
    private static final UserStatus UPDATED_STATUS = UserStatus.DEACTIVATED;

    private static final String ENTITY_API_URL = "/api/profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfileRepository profileRepository;

    @Mock
    private ProfileRepository profileRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfileMockMvc;

    private Profile profile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .profileImage(DEFAULT_PROFILE_IMAGE)
            .profileImageContentType(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)
            .address(DEFAULT_ADDRESS)
            .role(DEFAULT_ROLE)
            .status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setInternalUser(user);
        return profile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createUpdatedEntity(EntityManager em) {
        Profile profile = new Profile()
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setInternalUser(user);
        return profile;
    }

    @BeforeEach
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    void createProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Profile
        var returnedProfile = om.readValue(
            restProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profile)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Profile.class
        );

        // Validate the Profile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProfileUpdatableFieldsEquals(returnedProfile, getPersistedProfile(returnedProfile));
    }

    @Test
    @Transactional
    void createProfileWithExistingId() throws Exception {
        // Create the Profile with an existing ID
        profile.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profile)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profile.setRole(null);

        // Create the Profile, which fails.

        restProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profile)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profile.setStatus(null);

        // Create the Profile, which fails.

        restProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profile)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].profileImageContentType").value(hasItem(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_IMAGE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(profileRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(profileRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(profileRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(profileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.profileImageContentType").value(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profileImage").value(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_IMAGE)))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS);

        restProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProfile))
            )
            .andExpect(status().isOk());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfileToMatchAllProperties(updatedProfile);
    }

    @Test
    @Transactional
    void putNonExistingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(put(ENTITY_API_URL_ID, profile.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profile)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profile))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfileWithPatch() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profile using partial update
        Profile partialUpdatedProfile = new Profile();
        partialUpdatedProfile.setId(profile.getId());

        partialUpdatedProfile.profileImage(UPDATED_PROFILE_IMAGE).profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE);

        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfile))
            )
            .andExpect(status().isOk());

        // Validate the Profile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProfile, profile), getPersistedProfile(profile));
    }

    @Test
    @Transactional
    void fullUpdateProfileWithPatch() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profile using partial update
        Profile partialUpdatedProfile = new Profile();
        partialUpdatedProfile.setId(profile.getId());

        partialUpdatedProfile
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS);

        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfile))
            )
            .andExpect(status().isOk());

        // Validate the Profile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileUpdatableFieldsEquals(partialUpdatedProfile, getPersistedProfile(partialUpdatedProfile));
    }

    @Test
    @Transactional
    void patchNonExistingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profile.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profile))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profile))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profile
        restProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, profile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profileRepository.count();
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

    protected Profile getPersistedProfile(Profile profile) {
        return profileRepository.findById(profile.getId()).orElseThrow();
    }

    protected void assertPersistedProfileToMatchAllProperties(Profile expectedProfile) {
        assertProfileAllPropertiesEquals(expectedProfile, getPersistedProfile(expectedProfile));
    }

    protected void assertPersistedProfileToMatchUpdatableProperties(Profile expectedProfile) {
        assertProfileAllUpdatablePropertiesEquals(expectedProfile, getPersistedProfile(expectedProfile));
    }
}
