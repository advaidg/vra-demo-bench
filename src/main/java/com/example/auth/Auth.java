package com.example.auth;

/**
 * Deliberately-vulnerable authenticator.
 *
 * SECURITY ISSUE — CWE-798: hardcoded API key. VRA's SAST agent should
 * replace it with a System.getenv lookup or @Value("${...}") binding.
 */
public class Auth {
    // Hardcoded credential — should come from env var or secrets manager.
    private static final String API_KEY = System.getenv("API_KEY");

    public boolean validate(String key) {
        if (key == null) {
            return false;
        }
        return API_KEY.equals(key);
    }
}
