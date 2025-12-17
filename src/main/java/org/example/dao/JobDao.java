package org.example.dao;

import org.example.model.Job;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobDao {

    private final Connection cn;

    public JobDao(Connection cn) {
        this.cn = cn;
    }

    public List<Job> findAll() throws SQLException {
        String sql = """
            SELECT job_id, job_title, work_type, job_posting_date, company_name
            FROM JOB
            ORDER BY job_posting_date DESC
        """;

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Job> out = new ArrayList<>();
            while (rs.next()) {
                Date d = rs.getDate("job_posting_date");
                LocalDate ld = (d == null) ? null : d.toLocalDate();

                out.add(new Job(
                        rs.getInt("job_id"),
                        rs.getString("job_title"),
                        rs.getString("work_type"),
                        ld,
                        rs.getString("company_name")
                ));
            }
            return out;
        }
    }
}