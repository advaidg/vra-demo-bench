package com.example;

import com.example.crypto.CryptoHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoHelperTest {
    @Test
    void producesHexHash() {
        String h = CryptoHelper.hash("hello");
        // MD5 → 32 hex chars; SHA-256 → 64 hex chars. Either is acceptable.
        assertTrue(h.length() == 32 || h.length() == 64,
                "expected MD5(32) or SHA-256(64) hex; got len=" + h.length());
        assertTrue(h.matches("[0-9a-f]+"));
    }

    @Test
    void deterministic() {
        assertEquals(CryptoHelper.hash("x"), CryptoHelper.hash("x"));
    }

    @Test
    void differentInputsDifferentHashes() {
        assertNotEquals(CryptoHelper.hash("x"), CryptoHelper.hash("y"));
    }
}
