package com.obss.humanresources.model;

import javax.persistence.*;

@Entity
@Table(name = "job_applicant")
@IdClass(JobApplicantId.class)
public class JobApplicant {

    public enum STATUS {
        ACTIVE,
        ACCEPTED,
        REJECTED
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    @Id
    @ManyToOne
    @JoinColumn(name = "applicant_id", referencedColumnName = "id")
    private Applicant applicant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",
            length = 10)
    private STATUS status;
    @Transient
    private String statusString;

    public JobApplicant() {
    }

    public JobApplicant(Job job, Applicant applicant, STATUS status) {
        this.job = job;
        this.applicant = applicant;
        this.status = status;
        switch (status){
            case ACTIVE:
                statusString = "Aktif";
                break;
            case ACCEPTED:
                statusString = "Kabul edildi";
                break;
            case REJECTED:
                statusString = "Reddedildi";
                break;
        }
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        switch (status){
            case ACTIVE:
                statusString = "Aktif";
                break;
            case ACCEPTED:
                statusString = "Kabul edildi";
                break;
            case REJECTED:
                statusString = "Reddedildi";
                break;
        }
        this.status = status;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public String getStatusString() {
        return statusString;
    }

    @PostLoad
    public void setStatusString() {
        switch (status){
            case ACTIVE:
                this.statusString = "Aktif";
                break;
            case ACCEPTED:
                this.statusString = "Kabul edildi";
                break;
            case REJECTED:
                this.statusString = "Reddedildi";
                break;
        }
    }
}
