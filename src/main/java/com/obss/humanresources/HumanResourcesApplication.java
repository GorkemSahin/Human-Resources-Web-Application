package com.obss.humanresources;

import antlr.debug.Event;
import com.obss.humanresources.data.ApplicantRepository;
import com.obss.humanresources.model.Applicant;
import com.obss.humanresources.model.Job;
import com.obss.humanresources.service.ApplicantService;
import com.obss.humanresources.service.JobService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.transaction.Transaction;
import java.util.UUID;

@SpringBootApplication
public class HumanResourcesApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HumanResourcesApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

	}

}
