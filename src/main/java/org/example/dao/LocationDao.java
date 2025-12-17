package org.example.dao;

import org.example.model.Location;
import java.sql.*;
import java.util.*;

public class LocationDao {
    private final Connection cn;
    public LocationDao(Connection cn) { this.cn = cn; }

    public List<Location> findAll() throws SQLException {
        String sql = """
            SELECT location_id, location, country, latitude, longitude
            FROM LOCATION
            ORDER BY country, location
        """;
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Location> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new Location(
                        rs.getInt("location_id"),
                        rs.getString("location"),
                        rs.getString("country"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                ));
            }
            return out;
        }
    }
}
