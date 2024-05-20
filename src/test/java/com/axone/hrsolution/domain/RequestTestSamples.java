package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Request getRequestSample1() {
        return new Request().id(1L);
    }

    public static Request getRequestSample2() {
        return new Request().id(2L);
    }

    public static Request getRequestRandomSampleGenerator() {
        return new Request().id(longCount.incrementAndGet());
    }
}
