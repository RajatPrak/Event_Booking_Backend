package Koders.com.example.Koders.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;

@Component
public class BookingCodeGenerator {

    private static final String PREFIX = "BKG-";
    private static final char[] ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private final SecureRandom random = new SecureRandom();

    public String generate(LocalDate basis) {
        LocalDate date = basis != null ? basis : LocalDate.now();
        String mmm = date.getMonth().name().substring(0, 3); // JAN, FEB, ... SEP
        String year = String.valueOf(date.getYear());
        return PREFIX + mmm + year + "-" + random3();
    }

    private String random3() {
        StringBuilder sb = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            sb.append(ALPHANUM[random.nextInt(ALPHANUM.length)]);
        }
        return sb.toString();
    }
}
