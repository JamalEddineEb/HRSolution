package com.axone.hrsolution.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NDAAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNDAAllPropertiesEquals(NDA expected, NDA actual) {
        assertNDAAutoGeneratedPropertiesEquals(expected, actual);
        assertNDAAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNDAAllUpdatablePropertiesEquals(NDA expected, NDA actual) {
        assertNDAUpdatableFieldsEquals(expected, actual);
        assertNDAUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNDAAutoGeneratedPropertiesEquals(NDA expected, NDA actual) {
        assertThat(expected)
            .as("Verify NDA auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNDAUpdatableFieldsEquals(NDA expected, NDA actual) {
        assertThat(expected)
            .as("Verify NDA relevant properties")
            .satisfies(e -> assertThat(e.getDocument()).as("check document").isEqualTo(actual.getDocument()))
            .satisfies(
                e -> assertThat(e.getDocumentContentType()).as("check document contenty type").isEqualTo(actual.getDocumentContentType())
            )
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getPeriod()).as("check period").isEqualTo(actual.getPeriod()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNDAUpdatableRelationshipsEquals(NDA expected, NDA actual) {
        assertThat(expected)
            .as("Verify NDA relationships")
            .satisfies(e -> assertThat(e.getEmployer()).as("check employer").isEqualTo(actual.getEmployer()))
            .satisfies(e -> assertThat(e.getMediator()).as("check mediator").isEqualTo(actual.getMediator()))
            .satisfies(e -> assertThat(e.getCandidate()).as("check candidate").isEqualTo(actual.getCandidate()));
    }
}
