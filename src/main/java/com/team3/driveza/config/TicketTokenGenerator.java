package com.team3.driveza.config;

import java.security.SecureRandom;

public class TicketTokenGenerator {

    private static final SecureRandom RND = new SecureRandom();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generate() {
        StringBuilder sb = new StringBuilder("TCK-");
        for (int i = 0; i < 8; i++) {
            sb.append(CHARS.charAt(RND.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
