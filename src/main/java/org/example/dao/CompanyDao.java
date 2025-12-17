package org.example.dao;

import org.example.model.Company;
import java.sql.*;
import java.util.*;

public class CompanyDao {
    private final Connection cn;
    public CompanyDao(Connection cn) { this.cn = cn; }

    public List<Company> findAll() throws SQLException {
        String sql = """
            SELECT company_name, company_profile, company_size
            FROM COMPANY
            ORDER BY company_name
        """;
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Company> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new Company(
                        rs.getString("company_name"),
                        rs.getString("company_profile"),
                        rs.getString("company_size")
                ));
            }
            return out;
        }
    }
}
