package org.example.model;

import java.time.LocalDate;

public class Job {
    private final int jobId;
    private final String title;
    private final String workType;
    private final LocalDate postingDate;
    private final String companyName;

    public Job(int jobId, String title, String workType, LocalDate postingDate, String companyName) {
        this.jobId = jobId;
        this.title = title;
        this.workType = workType;
        this.postingDate = postingDate;
        this.companyName = companyName;
    }

    public int getJobId() { return jobId; }
    public String getTitle() { return title; }
    public String getWorkType() { return workType; }
    public LocalDate getPostingDate() { return postingDate; }
    public String getCompanyName() { return companyName; }
}