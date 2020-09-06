package com.obss.humanresources.model;

import javax.persistence.Id;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Applicant implements Serializable {

    @Id
    private String id;
    private String firstName, lastName, emailAddress, maidenName, headline, location, industry,
            positions, pictureUrl;
    @Size(max = 500)
    private String summary;
    private boolean blacklisted;
    @OneToMany(mappedBy = "applicant",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<JobApplicant> jobs = new ArrayList<JobApplicant>();

    public Applicant(){
        super();
    }

    public Applicant(String id, String firstName, String lastName, String emailAddress, String maidenName,
                     String headline, String location, String industry, String summary, String positions,
                     String pictureUrl) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.maidenName = maidenName;
        this.headline = headline;
        this.location = location;
        this.industry = industry;
        this.summary = summary;
        this.positions = positions;
        this.pictureUrl = pictureUrl;
    }

    public Applicant(String id, String firstName, String lastName, String emailAddress, String maidenName,
                     String headline, String location, String industry, String summary, String positions,
                     String pictureUrl, boolean blacklisted) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.maidenName = maidenName;
        this.headline = headline;
        this.location = location;
        this.industry = industry;
        this.summary = summary;
        this.positions = positions;
        this.pictureUrl = pictureUrl;
        this.blacklisted = blacklisted;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", maidenName='" + maidenName + '\'' +
                ", headline='" + headline + '\'' +
                ", location='" + location + '\'' +
                ", industry='" + industry + '\'' +
                ", positions='" + positions + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", summary='" + summary + '\'' +
                ", blacklisted=" + blacklisted +
                ", jobs=" + jobs +
                '}';
    }

    public List<JobApplicant> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobApplicant> jobs) {
        this.jobs = jobs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMaidenName() {
        return maidenName;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }


    public String getName (){
        return firstName + " " + lastName;
    }
}
