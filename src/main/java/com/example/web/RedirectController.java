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
        // No validation — accepts any URL the caller passes.
        return URI.create(userSuppliedUrl);
    }
}
