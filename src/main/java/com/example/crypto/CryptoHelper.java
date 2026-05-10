package com.example.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Deliberately-vulnerable hashing helper.
 *
 * SECURITY ISSUE — CWE-327: MD5 is broken for security. VRA's SAST agent
 * should swap "MD5" → "SHA-256". The accompanying CryptoHelperTest verifies
 * the OUTPUT length changes to 64 hex characters after the fix (the test
 * asserts ≥32 so it stays green either way).
 */
public class CryptoHelper {
    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("hash algorithm missing", e);
        }
    }
}
