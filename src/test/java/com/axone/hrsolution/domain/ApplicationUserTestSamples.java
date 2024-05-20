package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApplicationUser getApplicationUserSample1() {
        return new ApplicationUser().id(1L).address("address1");
    }

    public static ApplicationUser getApplicationUserSample2() {
        return new ApplicationUser().id(2L).address("address2");
    }

    public static ApplicationUser getApplicationUserRandomSampleGenerator() {
        return new ApplicationUser().id(longCount.incrementAndGet()).address(UUID.randomUUID().toString());
    }
}