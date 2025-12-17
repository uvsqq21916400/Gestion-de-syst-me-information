package org.example.model;

import java.time.LocalDate;

public class JobDetails {
    private final int jobId;
    private final String role;
    private final String workType;
    private final String salaryRange;
    private final LocalDate postingDate;
    private final String jobTitle;
    private final String jobPortal;
    private final String jobDescription;
    private final String responsibilities;
    private final String benefits;
    private final String companyName;
    private final int locationId;

    public JobDetails(int jobId, String role, String workType, String salaryRange, LocalDate postingDate,
                      String jobTitle, String jobPortal, String jobDescription, String responsibilities,
                      String benefits, String companyName, int locationId) {
        this.jobId = jobId;
        this.role = role;
        this.workType = workType;
        this.salaryRange = salaryRange;
        this.postingDate = postingDate;
        this.jobTitle = jobTitle;
        this.jobPortal = jobPortal;
        this.jobDescription = jobDescription;
        this.responsibilities = responsibilities;
        this.benefits = benefits;
        this.companyName = companyName;
        this.locationId = locationId;
    }

    public int getJobId() { return jobId; }
    public String getRole() { return role; }
    public String getWorkType() { return workType; }
    public String getSalaryRange() { return salaryRange; }
    public LocalDate getPostingDate() { return postingDate; }
    public String getJobTitle() { return jobTitle; }
    public String getJobPortal() { return jobPortal; }
    public String getJobDescription() { return jobDescription; }
    public String getResponsibilities() { return responsibilities; }
    public String getBenefits() { return benefits; }
    public String getCompanyName() { return companyName; }
    public int getLocationId() { return locationId; }
}
