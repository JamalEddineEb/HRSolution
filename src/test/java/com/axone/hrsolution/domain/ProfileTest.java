package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AdminTestSamples.*;
import static com.axone.hrsolution.domain.AppAccountTestSamples.*;
import static com.axone.hrsolution.domain.EmployerTestSamples.*;
import static com.axone.hrsolution.domain.ProfileTestSamples.*;
import static com.axone.hrsolution.domain.RecruiterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = getProfileSample1();
        Profile profile2 = new Profile();
        assertThat(profile1).isNotEqualTo(profile2);

        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);

        profile2 = getProfileSample2();
        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    void recruiterTest() throws Exception {
        Profile profile = getProfileRandomSampleGenerator();
        Recruiter recruiterBack = getRecruiterRandomSampleGenerator();

        profile.setRecruiter(recruiterBack);
        assertThat(profile.getRecruiter()).isEqualTo(recruiterBack);
        assertThat(recruiterBack.getRelatedUser()).isEqualTo(profile);

        profile.recruiter(null);
        assertThat(profile.getRecruiter()).isNull();
        assertThat(recruiterBack.getRelatedUser()).isNull();
    }

    @Test
    void employerTest() throws Exception {
        Profile profile = getProfileRandomSampleGenerator();
        Employer employerBack = getEmployerRandomSampleGenerator();

        profile.setEmployer(employerBack);
        assertThat(profile.getEmployer()).isEqualTo(employerBack);
        assertThat(employerBack.getRelatedUser()).isEqualTo(profile);

        profile.employer(null);
        assertThat(profile.getEmployer()).isNull();
        assertThat(employerBack.getRelatedUser()).isNull();
    }

    @Test
    void adminTest() throws Exception {
        Profile profile = getProfileRandomSampleGenerator();
        Admin adminBack = getAdminRandomSampleGenerator();

        profile.setAdmin(adminBack);
        assertThat(profile.getAdmin()).isEqualTo(adminBack);
        assertThat(adminBack.getRelatedUser()).isEqualTo(profile);

        profile.admin(null);
        assertThat(profile.getAdmin()).isNull();
        assertThat(adminBack.getRelatedUser()).isNull();
    }

    @Test
    void accountTest() throws Exception {
        Profile profile = getProfileRandomSampleGenerator();
        AppAccount appAccountBack = getAppAccountRandomSampleGenerator();

        profile.setAccount(appAccountBack);
        assertThat(profile.getAccount()).isEqualTo(appAccountBack);
        assertThat(appAccountBack.getOwner()).isEqualTo(profile);

        profile.account(null);
        assertThat(profile.getAccount()).isNull();
        assertThat(appAccountBack.getOwner()).isNull();
    }
}
