package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.ResumeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResumeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resume.class);
        Resume resume1 = getResumeSample1();
        Resume resume2 = new Resume();
        assertThat(resume1).isNotEqualTo(resume2);

        resume2.setId(resume1.getId());
        assertThat(resume1).isEqualTo(resume2);

        resume2 = getResumeSample2();
        assertThat(resume1).isNotEqualTo(resume2);
    }
}
