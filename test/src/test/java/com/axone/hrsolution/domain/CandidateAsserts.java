package com.axone.hrsolution.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CandidateAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCandidateAllPropertiesEquals(Candidate expected, Candidate actual) {
        assertCandidateAutoGeneratedPropertiesEquals(expected, actual);
        assertCandidateAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCandidateAllUpdatablePropertiesEquals(Candidate expected, Candidate actual) {
        assertCandidateUpdatableFieldsEquals(expected, actual);
        assertCandidateUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCandidateAutoGeneratedPropertiesEquals(Candidate expected, Candidate actual) {
        assertThat(expected)
            .as("Verify Candidate auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCandidateUpdatableFieldsEquals(Candidate expected, Candidate actual) {
        assertThat(expected)
            .as("Verify Candidate relevant properties")
            .satisfies(e -> assertThat(e.getLinkedinUrl()).as("check linkedinUrl").isEqualTo(actual.getLinkedinUrl()))
            .satisfies(e -> assertThat(e.getFullName()).as("check fullName").isEqualTo(actual.getFullName()))
            .satisfies(e -> assertThat(e.getYearsOfExperience()).as("check yearsOfExperience").isEqualTo(actual.getYearsOfExperience()))
            .satisfies(e -> assertThat(e.getCurrentSalary()).as("check currentSalary").isEqualTo(actual.getCurrentSalary()))
            .satisfies(e -> assertThat(e.getDesiredSalary()).as("check desiredSalary").isEqualTo(actual.getDesiredSalary()))
            .satisfies(e -> assertThat(e.getHasContract()).as("check hasContract").isEqualTo(actual.getHasContract()))
            .satisfies(e -> assertThat(e.getHired()).as("check hired").isEqualTo(actual.getHired()))
            .satisfies(e -> assertThat(e.getRate()).as("check rate").isEqualTo(actual.getRate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCandidateUpdatableRelationshipsEquals(Candidate expected, Candidate actual) {
        assertThat(expected)
            .as("Verify Candidate relationships")
            .satisfies(e -> assertThat(e.getTechCV()).as("check techCV").isEqualTo(actual.getTechCV()))
            .satisfies(e -> assertThat(e.getDomains()).as("check domains").isEqualTo(actual.getDomains()))
            .satisfies(e -> assertThat(e.getApplications()).as("check applications").isEqualTo(actual.getApplications()));
    }
}
