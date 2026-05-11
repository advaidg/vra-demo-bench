package com.example.web;

import java.net.URI;

/**
 * Deliberately-vulnerable redirect target.
 *
 * SECURITY ISSUE — CWE-601: open redirect. VRA's SAST agent should add an
 * allow-list check before constructing the URI.
 */
public class RedirectController {
    public URI redirectTo(String userSuppliedUrl) {
        // Validate the URL against an allow-list before redirecting.
        if (userSuppliedUrl == null || !userSuppliedUrl.matches("^https?://example.com/.*$")) {
            throw new IllegalArgumentException("Invalid redirect URL: " + userSuppliedUrl);
        }
        return URI.create(userSuppliedUrl);
    }
}
