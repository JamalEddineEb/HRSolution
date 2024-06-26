package com.axone.hrsolution.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTestAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppTestAllPropertiesEquals(AppTest expected, AppTest actual) {
        assertAppTestAutoGeneratedPropertiesEquals(expected, actual);
        assertAppTestAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppTestAllUpdatablePropertiesEquals(AppTest expected, AppTest actual) {
        assertAppTestUpdatableFieldsEquals(expected, actual);
        assertAppTestUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppTestAutoGeneratedPropertiesEquals(AppTest expected, AppTest actual) {
        assertThat(expected)
            .as("Verify AppTest auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppTestUpdatableFieldsEquals(AppTest expected, AppTest actual) {
        assertThat(expected)
            .as("Verify AppTest relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getInvitationLink()).as("check invitationLink").isEqualTo(actual.getInvitationLink()))
            .satisfies(e -> assertThat(e.getTotalScore()).as("check totalScore").isEqualTo(actual.getTotalScore()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getCompleted()).as("check completed").isEqualTo(actual.getCompleted()))
            .satisfies(e -> assertThat(e.getTestID()).as("check testID").isEqualTo(actual.getTestID()))
            .satisfies(e -> assertThat(e.getAlgorithm()).as("check algorithm").isEqualTo(actual.getAlgorithm()))
            .satisfies(e -> assertThat(e.getIsCodeTest()).as("check isCodeTest").isEqualTo(actual.getIsCodeTest()))
            .satisfies(e -> assertThat(e.getDuration()).as("check duration").isEqualTo(actual.getDuration()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppTestUpdatableRelationshipsEquals(AppTest expected, AppTest actual) {
        assertThat(expected)
            .as("Verify AppTest relationships")
            .satisfies(e -> assertThat(e.getTypes()).as("check types").isEqualTo(actual.getTypes()));
    }
}
