package com.axone.hrsolution.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TechCVEmploymentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVEmploymentAllPropertiesEquals(TechCVEmployment expected, TechCVEmployment actual) {
        assertTechCVEmploymentAutoGeneratedPropertiesEquals(expected, actual);
        assertTechCVEmploymentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVEmploymentAllUpdatablePropertiesEquals(TechCVEmployment expected, TechCVEmployment actual) {
        assertTechCVEmploymentUpdatableFieldsEquals(expected, actual);
        assertTechCVEmploymentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVEmploymentAutoGeneratedPropertiesEquals(TechCVEmployment expected, TechCVEmployment actual) {
        assertThat(expected)
            .as("Verify TechCVEmployment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVEmploymentUpdatableFieldsEquals(TechCVEmployment expected, TechCVEmployment actual) {
        assertThat(expected)
            .as("Verify TechCVEmployment relevant properties")
            .satisfies(e -> assertThat(e.getEmployment()).as("check employment").isEqualTo(actual.getEmployment()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVEmploymentUpdatableRelationshipsEquals(TechCVEmployment expected, TechCVEmployment actual) {
        assertThat(expected)
            .as("Verify TechCVEmployment relationships")
            .satisfies(e -> assertThat(e.getTechnicalCV()).as("check technicalCV").isEqualTo(actual.getTechnicalCV()));
    }
}
