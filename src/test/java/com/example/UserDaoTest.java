package com.example;

import com.example.dao.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validates UserDao against H2 in-memory.  Uses a unique DB name per run so
 * tests are isolated.  The contract under test:  given a name, return that
 * user's email(s).  Holds true both for the SQL-injection-vulnerable
 * implementation AND any PreparedStatement rewrite VRA might apply.
 */
class UserDaoTest {
    private Connection conn;

    @BeforeEach
    void setUp() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
        try (Statement s = conn.createStatement()) {
            s.execute("CREATE TABLE users (name VARCHAR(64), email VARCHAR(128))");
            s.execute("INSERT INTO users VALUES ('alice', 'alice@example.com')");
            s.execute("INSERT INTO users VALUES ('bob',   'bob@example.com')");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        conn.close();
    }

    @Test
    void findsExistingEmail() throws SQLException {
        UserDao dao = new UserDao(conn);
        List<String> out = dao.findEmailsByName("alice");
        assertEquals(List.of("alice@example.com"), out);
    }

    @Test
    void returnsEmptyForUnknownName() throws SQLException {
        UserDao dao = new UserDao(conn);
        List<String> out = dao.findEmailsByName("zzz-does-not-exist");
        assertEquals(List.of(), out);
    }
}
