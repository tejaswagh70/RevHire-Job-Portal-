package org.example.dao;
import java.util.*;

import org.example.model.EmployerModel;
import org.example.model.JobOpeningModel;
import org.example.model.JobSeekerModel;

public interface User {
    void JobSeekerRegister(JobSeekerModel jobSeekerModel) ;
    void JobSeekerLogin();

    void getJobs();
    void applyForJob();
    void myApplication();
    void EmployerRegister(EmployerModel employerModel);
    void EmployerLogin();
    void postJob();
    void deleteJob();
    void viewApplications();
    void validateJobSeekerLogin();
    void validateEmployerLogin();
}
