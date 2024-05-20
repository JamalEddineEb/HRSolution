package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResumeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Resume getResumeSample1() {
        return new Resume().id(1L).name("name1");
    }

    public static Resume getResumeSample2() {
        return new Resume().id(2L).name("name2");
    }

    public static Resume getResumeRandomSampleGenerator() {
        return new Resume().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
