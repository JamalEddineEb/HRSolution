package com.axone.hrsolution.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TechCVDocsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVDocsAllPropertiesEquals(TechCVDocs expected, TechCVDocs actual) {
        assertTechCVDocsAutoGeneratedPropertiesEquals(expected, actual);
        assertTechCVDocsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVDocsAllUpdatablePropertiesEquals(TechCVDocs expected, TechCVDocs actual) {
        assertTechCVDocsUpdatableFieldsEquals(expected, actual);
        assertTechCVDocsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVDocsAutoGeneratedPropertiesEquals(TechCVDocs expected, TechCVDocs actual) {
        assertThat(expected)
            .as("Verify TechCVDocs auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVDocsUpdatableFieldsEquals(TechCVDocs expected, TechCVDocs actual) {
        assertThat(expected)
            .as("Verify TechCVDocs relevant properties")
            .satisfies(e -> assertThat(e.getAttachedDoc()).as("check attachedDoc").isEqualTo(actual.getAttachedDoc()))
            .satisfies(
                e ->
                    assertThat(e.getAttachedDocContentType())
                        .as("check attachedDoc contenty type")
                        .isEqualTo(actual.getAttachedDocContentType())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTechCVDocsUpdatableRelationshipsEquals(TechCVDocs expected, TechCVDocs actual) {
        assertThat(expected)
            .as("Verify TechCVDocs relationships")
            .satisfies(e -> assertThat(e.getTechnicalCV()).as("check technicalCV").isEqualTo(actual.getTechnicalCV()));
    }
}
