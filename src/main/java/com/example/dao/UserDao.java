package com.example.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Deliberately-vulnerable DAO.
 *
 * SECURITY ISSUE — CWE-89: SQL injection via string concatenation in
 * findEmailsByName(). VRA's SAST agent should rewrite this to use a
 * PreparedStatement with parameter binding.
 *
 * The accompanying UserDaoTest exercises the contract using H2 in-memory.
 */
public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Returns email addresses for users whose `name` column equals the input.
     * VULNERABLE: builds the query via string concat.
     */
    public List<String> findEmailsByName(String name) throws SQLException {
        String sql = "SELECT email FROM users WHERE name = '" + name + "'";
        List<String> out = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                out.add(rs.getString(1));
            }
        }
        return out;
    }
}
