package com.practice.urlshortener.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class HelperFunctions {

    public static String randomAlphanumeric(int count) {
        return new StringBuilder(RandomStringUtils.randomAlphanumeric(count)).append(new SecureRandom().nextInt(1000)).toString();
    }

    public static void main(String[] args) {
    }
}
