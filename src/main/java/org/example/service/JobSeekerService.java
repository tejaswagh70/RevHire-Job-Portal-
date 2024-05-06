package org.example.service;

import org.example.dao.User;
import org.example.model.EmployerModel;
import org.example.model.JobOpeningModel;
import org.example.model.JobSeekerModel;

import java.util.List;

public class JobSeekerService {

    private User jobSeeker;

    public JobSeekerService(User userDAO) {
        this.jobSeeker = userDAO;
    }

    public void JobSeekerRegister(JobSeekerModel jobSeekerModel) {
     jobSeeker.JobSeekerRegister(jobSeekerModel);
    }

    public void validateJobSeekerLogin(){
    jobSeeker.validateJobSeekerLogin();
    }
}
