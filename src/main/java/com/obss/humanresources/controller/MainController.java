package com.obss.humanresources.controller;

import com.obss.humanresources.model.Applicant;
import com.obss.humanresources.model.Job;
import com.obss.humanresources.model.JobApplicant;
import com.obss.humanresources.model.User;
import com.obss.humanresources.service.ApplicantService;
import com.obss.humanresources.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class MainController {

    private ApplicantService applicantService;
    private JobService jobService;

    @Autowired
    public MainController(ApplicantService applicantService, JobService jobService){
        this.applicantService = applicantService;
        this.jobService = jobService;
    }



    @RequestMapping(value={"/", " * ", "index"})
    public ModelAndView home (Model model) {
        Integer applicants, jobs;
        applicants = applicantService.getAllApplicants().size();
        jobs = jobService.getAllJobs().size();
        String applicantNo, jobNo;
        applicantNo = applicants.toString();
        jobNo = jobs.toString();
        model.addAttribute("jobNo", jobNo);
        model.addAttribute("applicantNo", applicantNo);
        return new ModelAndView("index.html");
    }

    @GetMapping("/about")
    public String about (@SessionAttribute("user") User user, Model model) {
        //model.addAttribute("user", user);
        return "about.html";
    }

    @GetMapping("/profile")
    public ModelAndView profile (@SessionAttribute("user") User user, Model model) {
        if (user.getUserType() == User.USER_TYPE.APPLICANT){
            Applicant applicant = applicantService.getApplicant(user.getUserId()).get();
            model.addAttribute("user", user);
            model.addAttribute("applicant", applicant);
            List<JobApplicant> jobs = applicant.getJobs();
            model.addAttribute("jobs", jobs);
        }
        return new ModelAndView("profile.html");
    }

    @GetMapping("/logout")
    public ModelAndView logOut (HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if(session != null && request.isRequestedSessionIdValid() )
        {
            session.invalidate();
        }
        session = request.getSession();
        session.setAttribute("user", new User(session.getId(), null, User.USER_TYPE.VISITOR, null));
        return new ModelAndView("logout.html");
    }

}
