package com.obss.humanresources.data;

import com.obss.humanresources.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface ApplicantRepository extends JpaRepository<Applicant, String> {

}
