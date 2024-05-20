package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.ApplicationUserAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.ApplicationUser;
import com.axone.hrsolution.domain.User;
import com.axone.hrsolution.domain.enumeration.UserRole;
import com.axone.hrsolution.domain.enumeration.UserStatus;
import com.axone.hrsolution.repository.ApplicationUserRepository;
import com.axone.hrsolution.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
 * Integration tests for the {@link ApplicationUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplicationUserResourceIT {

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

    private static final String ENTITY_API_URL = "/api/application-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .profileImage(DEFAULT_PROFILE_IMAGE)
            .profileImageContentType(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)
            .address(DEFAULT_ADDRESS)
            .role(DEFAULT_ROLE)
            .status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        applicationUser.setInternalUser(user);
        return applicationUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        applicationUser.setInternalUser(user);
        return applicationUser;
    }

    @BeforeEach
    public void initTest() {
        applicationUser = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ApplicationUser
        var returnedApplicationUser = om.readValue(
            restApplicationUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApplicationUser.class
        );

        // Validate the ApplicationUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertApplicationUserUpdatableFieldsEquals(returnedApplicationUser, getPersistedApplicationUser(returnedApplicationUser));

        assertApplicationUserMapsIdRelationshipPersistedValue(applicationUser, returnedApplicationUser);
    }

    @Test
    @Transactional
    void createApplicationUserWithExistingId() throws Exception {
        // Create the ApplicationUser with an existing ID
        applicationUser.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateApplicationUserMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).orElseThrow();
        assertThat(updatedApplicationUser).isNotNull();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);

        // Update the User with new association value
        //updatedApplicationUser.setUser(user);

        // Update the entity
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testApplicationUser.getId()).isEqualTo(testApplicationUser.getUser().getId());
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationUser.setRole(null);

        // Create the ApplicationUser, which fails.

        restApplicationUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        applicationUser.setStatus(null);

        // Create the ApplicationUser, which fails.

        restApplicationUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUser)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicationUsers() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].profileImageContentType").value(hasItem(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_IMAGE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(applicationUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(applicationUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(applicationUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApplicationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(applicationUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId().intValue()))
            .andExpect(jsonPath("$.profileImageContentType").value(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profileImage").value(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_IMAGE)))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);
        updatedApplicationUser
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS);

        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApplicationUserToMatchAllProperties(updatedApplicationUser);
    }

    @Test
    @Transactional
    void putNonExistingApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .role(UPDATED_ROLE);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApplicationUser, applicationUser),
            getPersistedApplicationUser(applicationUser)
        );
    }

    @Test
    @Transactional
    void fullUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .role(UPDATED_ROLE)
            .status(UPDATED_STATUS);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationUserUpdatableFieldsEquals(
            partialUpdatedApplicationUser,
            getPersistedApplicationUser(partialUpdatedApplicationUser)
        );
    }

    @Test
    @Transactional
    void patchNonExistingApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(applicationUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the applicationUser
        restApplicationUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return applicationUserRepository.count();
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

    protected ApplicationUser getPersistedApplicationUser(ApplicationUser applicationUser) {
        return applicationUserRepository.findById(applicationUser.getId()).orElseThrow();
    }

    protected void assertPersistedApplicationUserToMatchAllProperties(ApplicationUser expectedApplicationUser) {
        assertApplicationUserAllPropertiesEquals(expectedApplicationUser, getPersistedApplicationUser(expectedApplicationUser));
    }

    protected void assertPersistedApplicationUserToMatchUpdatableProperties(ApplicationUser expectedApplicationUser) {
        assertApplicationUserAllUpdatablePropertiesEquals(expectedApplicationUser, getPersistedApplicationUser(expectedApplicationUser));
    }
}
