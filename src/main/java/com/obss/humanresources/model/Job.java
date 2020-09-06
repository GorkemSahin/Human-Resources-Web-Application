package com.obss.humanresources.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Id;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Job implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    private String title, description;

    @OneToMany(mappedBy = "job",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<JobApplicant> applicants = new ArrayList<JobApplicant>();

    private Date postedAt = new Date();
    private Date publishedAt;
    private Date removedAt;

    public Job(){
        super();
    }

    public Job(
            @JsonProperty("id") Integer id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", applicants=" + applicants +
                ", postedAt=" + postedAt +
                ", publishedAt=" + publishedAt +
                ", removedAt=" + removedAt +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Date getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(Date removedAt) {
        this.removedAt = removedAt;
    }

    public List<JobApplicant> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<JobApplicant> applicants) {
        this.applicants = applicants;
    }

    public List<Applicant> getActualApplicants(){
        List<Applicant> actualApplicants = new ArrayList<Applicant>();
        for (JobApplicant jobApplicant : applicants){
            actualApplicants.add(jobApplicant.getApplicant());
        }
        return actualApplicants;
    }
}
