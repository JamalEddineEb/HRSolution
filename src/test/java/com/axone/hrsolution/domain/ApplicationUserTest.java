package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AppAccountTestSamples.*;
import static com.axone.hrsolution.domain.ApplicationUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void accountTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        AppAccount appAccountBack = getAppAccountRandomSampleGenerator();

        applicationUser.setAccount(appAccountBack);
        assertThat(applicationUser.getAccount()).isEqualTo(appAccountBack);
        assertThat(appAccountBack.getOwner()).isEqualTo(applicationUser);

        applicationUser.account(null);
        assertThat(applicationUser.getAccount()).isNull();
        assertThat(appAccountBack.getOwner()).isNull();
    }
}
