package com.obss.humanresources.controller;

import com.obss.humanresources.model.Applicant;
import com.obss.humanresources.model.JobApplicant;
import com.obss.humanresources.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("applicants")
public class ApplicantController {

    private final ApplicantService applicantService;

    @Autowired
    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @GetMapping
    public ModelAndView getAllApplicants (Model model){
        model.addAttribute("applicants", applicantService.getAllApplicants());
        return new ModelAndView("all-applicants.html");
    }

    @GetMapping(
            path = "{id}"
    )
    public ModelAndView getApplicant (@PathVariable("id") String id, HttpSession session, Model model){
        Applicant applicant = applicantService.getApplicant(id).get();
        model.addAttribute("applicant", applicant);
        return new ModelAndView("applicant.html");
    }

    @GetMapping(
            path = "{id}/blacklist"
    )
    public ModelAndView blacklist (@PathVariable("id") String id, HttpSession session, Model model){
        Applicant applicant = applicantService.getApplicant(id).get();
        applicant.setBlacklisted(!(applicant.isBlacklisted()));
        if (applicant.isBlacklisted()){
            for (JobApplicant jobApplicant : applicant.getJobs()){
                jobApplicant.setStatus(JobApplicant.STATUS.REJECTED);
            }
        }
        applicantService.persistNewApplicant(applicant);
        model.addAttribute("applicants", applicantService.getAllApplicants());
        return new ModelAndView("all-applicants.html");
    }

    @RequestMapping(
            value = "/json",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Applicant> getAllApplicants (){
        return applicantService.getAllApplicants();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getAllApplicantsJson (Model model){
        model.addAttribute("applicants", applicantService.getAllApplicants());
        return "GİRİŞ YAPILMIŞ";
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{id}"
    )
    public Optional<Applicant> getApplicant (@PathVariable("id") String id){
        return applicantService.getApplicant(id);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void insertNewApplicant (@RequestBody Applicant applicant){
        applicantService.persistNewApplicant(applicant);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            path = "{id}"
    )
    public void updateApplicant (@PathVariable("id") String id, @RequestBody Applicant applicant){
        applicant.setId(id);
        applicantService.persistNewApplicant(applicant);
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "{id}"
    )
    public void deleteApplicant (@PathVariable("id") String id){
        applicantService.deleteApplicant(id);
    }

}
