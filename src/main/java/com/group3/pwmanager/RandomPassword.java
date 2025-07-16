package com.group3.pwmanager;

import java.security.SecureRandom;
import java.util.function.IntPredicate;

public class RandomPassword {
    public static void main (String[] args) {
        boolean uc = false;
        boolean lc = true;
        boolean num = true;
        boolean spec = true;

        System.out.println(generatePassword(uc, lc, num, spec, 20));
    }

    public static String generatePassword (boolean uc, boolean lc, boolean num, boolean spec, int length) {
        int leftLimit = 37;
        int rightLimit = 122;
        SecureRandom random = new SecureRandom();

        // uc: A-Z (65-90)
        IntPredicate NoUc = i -> !(i >= 65 && i <= 90);

        // lc: a-z (97-122)
        IntPredicate NoLc = i -> !(i >= 97 && i <= 122);

        // num: 0-9 (48-57)
        IntPredicate NoNum = i -> !(i >= 48 && i <= 57);

        // spec:
        // [space]!"#$%&'()*+,-./ (32-47)
        // :;<=>?@ (58-64)
        // [\]^_` (91-96)
        IntPredicate NoSpec = i -> !(i >= 32 && i <= 47) && !(i >= 58 && i <= 64) && !(i >= 91 && i <= 96);

        IntPredicate finalFilter = i -> true;

        if (!uc) finalFilter = NoUc.and(finalFilter);
        if (!lc) finalFilter = NoLc.and(finalFilter);
        if (!num) finalFilter = NoNum.and(finalFilter);
        if (!spec) finalFilter = NoSpec.and(finalFilter);

        String generatedString = random.ints(leftLimit, rightLimit + 1)
            .filter(finalFilter)
            .limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        return generatedString;
    }
}