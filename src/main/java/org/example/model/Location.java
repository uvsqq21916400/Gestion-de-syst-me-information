package org.example.model;

public class Location {
    private final int locationId;
    private final String location;
    private final String country;
    private final double latitude;
    private final double longitude;

    public Location(int locationId, String location, String country, double latitude, double longitude) {
        this.locationId = locationId;
        this.location = location;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLocationId() { return locationId; }
    public String getLocation() { return location; }
    public String getCountry() { return country; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override public String toString() { return location + ", " + country; }
}

