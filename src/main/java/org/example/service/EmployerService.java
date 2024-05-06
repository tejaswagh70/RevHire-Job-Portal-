package org.example.service;

import org.example.dao.User;
import org.example.model.EmployerModel;

public class EmployerService {

    private User employer;

    public EmployerService(User userDAO) {
        this.employer = userDAO;
    }

    public void validateEmployerLogin(){
        employer.validateEmployerLogin();
    }
    public  void EmployerRegister(EmployerModel employerModel){
        employer.EmployerRegister(employerModel);
    }
}
