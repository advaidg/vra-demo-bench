package com.example.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Deliberately-vulnerable file reader.
 *
 * SECURITY ISSUE — CWE-22: path traversal. VRA's SAST agent should resolve
 * the path against a base directory and verify it stays inside.
 */
public class PathReader {
    private final Path baseDir;

    public PathReader(String baseDir) {
        this.baseDir = Paths.get(baseDir);
    }

    public String readUserFile(String userSuppliedName) throws IOException {
        // VULNERABLE: no normalization / containment check.
        Path target = baseDir.resolve(userSuppliedName);
        return Files.readString(target);
    }
}
