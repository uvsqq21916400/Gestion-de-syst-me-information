package org.example.model;

import java.time.LocalDate;

public class Job {
    private final int jobId;
    private final String jobTitle;
    private final String workType;
    private final LocalDate postingDate;
    private final String companyName;

    public Job(int jobId, String jobTitle, String workType, LocalDate postingDate, String companyName) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.workType = workType;
        this.postingDate = postingDate;
        this.companyName = companyName;
    }

    public int getJobId() { return jobId; }
    public String getJobTitle() { return jobTitle; }
    public String getWorkType() { return workType; }
    public LocalDate getPostingDate() { return postingDate; }
    public String getCompanyName() { return companyName; }

    // compat si ton MainController utilise getTitle()
    public String getTitle() { return jobTitle; }
}
