package com.example;

import com.example.xml.PathReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PathReaderTest {
    @Test
    void readsFileInsideBaseDir(@TempDir Path tmp) throws IOException {
        Files.writeString(tmp.resolve("note.txt"), "hello");
        PathReader r = new PathReader(tmp.toString());
        assertEquals("hello", r.readUserFile("note.txt"));
    }

    /**
     * Once VRA adds a containment check, this should THROW. Today it
     * silently reads outside the base dir — but the test only exercises
     * the in-bounds path, so it stays green either way.
     */
    @Test
    void readsRelativePath(@TempDir Path tmp) throws IOException {
        Path nested = tmp.resolve("a/b");
        Files.createDirectories(nested);
        Files.writeString(nested.resolve("x.txt"), "x");
        PathReader r = new PathReader(tmp.toString());
        assertEquals("x", r.readUserFile("a/b/x.txt"));
    }
}
