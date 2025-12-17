package org.example.dao;

import org.example.model.Job;
import org.example.model.JobDetails;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobDao {
    private final Connection cn;
    public JobDao(Connection cn) { this.cn = cn; }

    // 1) LISTE pour TableView
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
                LocalDate d = rs.getDate("job_posting_date").toLocalDate();
                out.add(new Job(
                        rs.getInt("job_id"),
                        rs.getString("job_title"),
                        rs.getString("work_type"),
                        d,
                        rs.getString("company_name")
                ));
            }
            return out;
        }
    }

    // 2) DETAILS quand on clique une ligne
    public JobDetails findDetailsById(int jobId) throws SQLException {
        String sql = """
            SELECT job_id, role, work_type, salary_range, job_posting_date,
                   job_title, job_portal, job_description, responsibilities, benefits,
                   company_name, location_id
            FROM JOB
            WHERE job_id = ?
        """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new JobDetails(
                        rs.getInt("job_id"),
                        rs.getString("role"),
                        rs.getString("work_type"),
                        rs.getString("salary_range"),
                        rs.getDate("job_posting_date").toLocalDate(),
                        rs.getString("job_title"),
                        rs.getString("job_portal"),
                        rs.getString("job_description"),
                        rs.getString("responsibilities"),
                        rs.getString("benefits"),
                        rs.getString("company_name"),
                        rs.getInt("location_id")
                );
            }
        }
    }

    // 3) INSERT/UPDATE/DELETE sur JOB (utilise JobDetails)
    public int insert(JobDetails j) throws SQLException {
        String sql = """
            INSERT INTO JOB(role, work_type, salary_range, job_posting_date,
                            job_title, job_portal, job_description, responsibilities, benefits,
                            company_name, location_id)
            VALUES(?,?,?,?,?,?,?,?,?,?,?)
        """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, j.getRole());
            ps.setString(2, j.getWorkType());
            ps.setString(3, j.getSalaryRange());
            ps.setDate(4, Date.valueOf(j.getPostingDate()));
            ps.setString(5, j.getJobTitle());
            ps.setString(6, j.getJobPortal());
            ps.setString(7, j.getJobDescription());
            ps.setString(8, j.getResponsibilities());
            ps.setString(9, j.getBenefits());
            ps.setString(10, j.getCompanyName());
            ps.setInt(11, j.getLocationId());
            return ps.executeUpdate();
        }
    }

    public int update(JobDetails j) throws SQLException {
        String sql = """
            UPDATE JOB
            SET role=?, work_type=?, salary_range=?, job_posting_date=?,
                job_title=?, job_portal=?, job_description=?, responsibilities=?, benefits=?,
                company_name=?, location_id=?
            WHERE job_id=?
        """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, j.getRole());
            ps.setString(2, j.getWorkType());
            ps.setString(3, j.getSalaryRange());
            ps.setDate(4, Date.valueOf(j.getPostingDate()));
            ps.setString(5, j.getJobTitle());
            ps.setString(6, j.getJobPortal());
            ps.setString(7, j.getJobDescription());
            ps.setString(8, j.getResponsibilities());
            ps.setString(9, j.getBenefits());
            ps.setString(10, j.getCompanyName());
            ps.setInt(11, j.getLocationId());
            ps.setInt(12, j.getJobId());
            return ps.executeUpdate();
        }
    }

    public int delete(int jobId) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM JOB WHERE job_id=?")) {
            ps.setInt(1, jobId);
            return ps.executeUpdate();
        }
    }
}
