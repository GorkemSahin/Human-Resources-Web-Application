package com.obss.humanresources.model;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class JobApplicantId implements Serializable {
    private Integer job;
    private String applicant;

    public int hashCode() {
        return Objects.hash(this.job, this.applicant);
    }

    public boolean equals (Object object) {
        if (object instanceof JobApplicantId) {
            JobApplicantId id = (JobApplicantId) object;
            return (id.applicant == this.applicant) && (id.job == this.job);
        }
        return false;
    }

    public JobApplicantId() {
    }

    public JobApplicantId(Integer job, String applicant) {
        this.job = job;
        this.applicant = applicant;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }
}