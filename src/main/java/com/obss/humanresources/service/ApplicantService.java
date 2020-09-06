package com.obss.humanresources.service;

import com.obss.humanresources.data.ApplicantRepository;
import com.obss.humanresources.model.Applicant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicantService {

    private final ApplicantRepository applicantRepository;

    @Autowired
    public ApplicantService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    public Applicant persistNewApplicant (Applicant applicant){
        return applicantRepository.save(applicant);
    }

    public Applicant persistNewApplicantFromLinkedIn (String jString){
        JSONObject jApplicant = new JSONObject(jString);
        Applicant applicant = new Applicant();
        applicant.setId(jApplicant.getString("id"));
        applicant.setFirstName(jApplicant.getString("firstName"));
        applicant.setLastName(jApplicant.getString("lastName"));
        applicant.setEmailAddress(jApplicant.getString("emailAddress"));
        return applicantRepository.save(applicant);
    }

    public Optional<Applicant> getApplicant (String id){
        return applicantRepository.findById(id);
    }

    public List<Applicant> getAllApplicants(){
        return applicantRepository.findAll();
    }

    public void deleteApplicant (String id){
        applicantRepository.deleteById(id);
    }

    public void deleteAll(){
        applicantRepository.deleteAll();
    }

}
