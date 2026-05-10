package com.example;

import com.example.auth.Auth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthTest {
    @Test
    void validateRejectsNull() {
        Auth a = new Auth();
        assertFalse(a.validate(null));
    }

    @Test
    void validateRejectsEmptyString() {
        Auth a = new Auth();
        assertFalse(a.validate(""));
    }

    /**
     * After VRA replaces the hardcoded key with `System.getenv("API_KEY")`,
     * this test still passes when the env var matches the supplied input.
     * The pre-fix version always returned true for the literal — both
     * versions reject mismatches.
     */
    @Test
    void validateRejectsObviouslyWrongKey() {
        Auth a = new Auth();
        assertFalse(a.validate("not-the-key"));
    }
}
