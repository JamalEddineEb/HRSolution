package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecruiterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Recruiter getRecruiterSample1() {
        return new Recruiter().id(1L).label("label1").linkedinUrl("linkedinUrl1");
    }

    public static Recruiter getRecruiterSample2() {
        return new Recruiter().id(2L).label("label2").linkedinUrl("linkedinUrl2");
    }

    public static Recruiter getRecruiterRandomSampleGenerator() {
        return new Recruiter()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .linkedinUrl(UUID.randomUUID().toString());
    }
}
