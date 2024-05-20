package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmployerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Employer getEmployerSample1() {
        return new Employer().id(1L).label("label1").linkedinUrl("linkedinUrl1");
    }

    public static Employer getEmployerSample2() {
        return new Employer().id(2L).label("label2").linkedinUrl("linkedinUrl2");
    }

    public static Employer getEmployerRandomSampleGenerator() {
        return new Employer().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString()).linkedinUrl(UUID.randomUUID().toString());
    }
}
