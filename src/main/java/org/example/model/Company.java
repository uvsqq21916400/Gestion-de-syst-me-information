
package org.example.model;

public class Company {
    private final String companyName;
    private final String companyProfile;
    private final String companySize;

    public Company(String companyName, String companyProfile, String companySize) {
        this.companyName = companyName;
        this.companyProfile = companyProfile;
        this.companySize = companySize;
    }

    public String getCompanyName() { return companyName; }
    public String getCompanyProfile() { return companyProfile; }
    public String getCompanySize() { return companySize; }

    @Override public String toString() { return companyName; }
}
