package com.obss.humanresources.controller;

import com.obss.humanresources.model.Applicant;
import com.obss.humanresources.model.Job;
import com.obss.humanresources.model.JobApplicant;
import com.obss.humanresources.model.User;
import com.obss.humanresources.service.ApplicantService;
import com.obss.humanresources.service.JobService;
import com.obss.humanresources.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("jobs")
public class JobController {

    private final JobService jobService;
    private final ApplicantService applicantService;
    private final MailService mailService;

    @Autowired
    public JobController(JobService jobService, ApplicantService applicantService, MailService mailService) {
        this.jobService = jobService;
        this.applicantService = applicantService;
        this.mailService = mailService;
    }

    @GetMapping()
    public ModelAndView jobs (Model model){
        model.addAttribute("jobs", jobService.getAllJobs());
        return new ModelAndView("jobs.html");
    }

    @GetMapping(
            path = "{id}"
    )
    public ModelAndView getJob (@PathVariable("id") Integer id, HttpSession session, Model model){
        Job job = jobService.getJob(id).get();
        model.addAttribute("job", job);
        User user = (User) session.getAttribute("user");
        if (user.getUserType() == User.USER_TYPE.APPLICANT){
            Applicant applicant = applicantService.getApplicant(user.getUserId()).get();
            model.addAttribute(applicant);
            for (JobApplicant jobApplicant : job.getApplicants()){
                if (jobApplicant.getApplicant() == applicant){
                    model.addAttribute("status", jobApplicant.getStatusString());
                }
            }
        }
        return new ModelAndView("job.html");
    }

    @GetMapping(
            path = "{id}/apply"
    )
    public ModelAndView applyGet (@PathVariable("id") Integer id, Model model, HttpSession session){
        model.addAttribute("job", jobService.getJob(id).get());
        return new ModelAndView("apply.html");
    }

    @PostMapping(
            path = "{id}/apply"
    )
    public ModelAndView applyPost (@PathVariable("id") Integer id, @SessionAttribute("user") User user, HttpSession session, Model model){
        Job job = jobService.getJob(id).get();
        Applicant applicant = applicantService.getApplicant(user.getUserId()).get();
        if (applicant.isBlacklisted()){
            return new ModelAndView("blacklisted.html");
        }
        job.getApplicants().add(new JobApplicant(job, applicant, JobApplicant.STATUS.ACTIVE));
        jobService.persistNewJob(job);
        applicantService.persistNewApplicant(applicant);
        session.setAttribute("user", new User(session.getId(), applicant.getId(), User.USER_TYPE.APPLICANT, applicant));
        return new ModelAndView("applied.html");
    }

    @GetMapping(
            path = "{id}/applicants"
    )
    public ModelAndView applicants (@PathVariable("id") Integer id, Model model){
        model.addAttribute("applicants", jobService.getJob(id).get().getApplicants());
        model.addAttribute("job", jobService.getJob(id).get());
        return new ModelAndView("applicants.html");
    }

    @GetMapping(
            path = "{jobId}/applicants/{applicantId}/{response}"
    )
    ModelAndView sendMailToApplicant (@PathVariable("jobId") Integer jobId,
                                      @PathVariable("applicantId") String applicantId,
                                      @PathVariable("response") String response, Model model){
        Optional<Job> optionalJob = jobService.getJob(jobId);
        if (optionalJob.isPresent()){
            Job job = jobService.getJob(jobId).get();
        }
        //Job job = jobService.getJob(jobId).get();
        Applicant applicant = applicantService.getApplicant(applicantId).get();
        String jobTitle = job.getTitle();
        String applicantName = applicant.getFirstName() + " " + applicant.getLastName();
        String mail = "Sayın " + applicantName + ", " + jobTitle + " ilanına yaptığınız başvuru ";
        if (response.equals("accept")){
            for (JobApplicant jobApplicant : job.getApplicants()){
                if (jobApplicant.getApplicant() == applicant){
                    jobApplicant.setStatus(JobApplicant.STATUS.ACCEPTED);
                }
            }
            mail += "kabul edilmiştir.";
        } else {
            for (JobApplicant jobApplicant : job.getApplicants()){
                if (jobApplicant.getApplicant() == applicant){
                    jobApplicant.setStatus(JobApplicant.STATUS.REJECTED);
                }
            }
            mail += "reddedilmiştir.";
        }
        jobService.persistNewJob(job);
        mailService.sendSimpleMessage(applicant.getEmailAddress(), "Sonuç", mail);
        model.addAttribute("mail", mail);
        return new ModelAndView("applicant-notified.html");
    }

    @GetMapping(
            path = "{id}/edit"
    )
    public ModelAndView editJob (@PathVariable("id") Integer id, Model model){
        model.addAttribute("job", jobService.getJob(id).get());
        return new ModelAndView("edit-job.html");
    }

    @PostMapping(
            path = "{id}/edit"
    )
    public ModelAndView editedJob (@PathVariable("id") Integer id, @ModelAttribute("job") Job job, Model model){
        jobService.persistNewJob(job);
        model.addAttribute("jobs", jobService.getAllJobs());
        return new ModelAndView("jobs.html");
    }

    @GetMapping("/new-job")
    public ModelAndView newJob (Model model){
         model.addAttribute("job", new Job());
        return new ModelAndView("new-job.html");
    }

    @PostMapping("/new-job")
    public ModelAndView newJobPost (@ModelAttribute("job") Job job, Model model){
        jobService.persistNewJob(job);
        model.addAttribute("jobs", jobService.getAllJobs());
        return new ModelAndView("jobs.html");
    }

    @GetMapping(
            path = "{id}/delete"
    )
    public ModelAndView deleteJob (@PathVariable("id") Integer id, Model model){
        jobService.deleteJob(id);
        model.addAttribute("jobs", jobService.getAllJobs());
        return new ModelAndView("jobs.html");
    }


    // JSON

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{id}"
    )
    public Optional<Job> getJobJson (@PathVariable("id") Integer id){
        return jobService.getJob(id);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void insertNewJob (@RequestBody Job job){
        jobService.persistNewJob(job);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            path = "{id}"
    )
    public void updateJob (@PathVariable("id") Integer id, @RequestBody Job job){
        job.setId(id);
        jobService.persistNewJob(job);
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "{id}"
    )
    public void deleteJob (@PathVariable("id") Integer id){
        jobService.deleteJob(id);
    }

    @RequestMapping(
            value = "/json",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public List<Job> getAllJobs (){
        return jobService.getAllJobs();
    }


}
