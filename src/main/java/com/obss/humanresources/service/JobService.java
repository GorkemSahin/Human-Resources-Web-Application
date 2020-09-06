package com.obss.humanresources.service;

import com.obss.humanresources.data.JobRepository;
import com.obss.humanresources.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job persistNewJob (Job job){
        return jobRepository.save(job);
    }

    public Optional<Job> getJob (Integer id){
        return jobRepository.findById(id);
    }

    public List<Job> getAllJobs(){
        return jobRepository.findAll();
    }

    public void deleteJob (Integer id){
        jobRepository.deleteById(id);
    }

    public void deleteAll (){
        jobRepository.deleteAll();
    }

}
