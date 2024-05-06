package org.example.model;

public class JobOpeningModel {

        private int jobId;
        private String companyName;
        private String jobRole;
        private String jobLocation;
        private float packageAmount;
        private String jobDescription;
        private int noOfPositions;
        private int yearsOfExperience;
        private int employeeId;

    public JobOpeningModel(int jobId, String companyName, String jobRole, String jobLocation, float packageAmount, String jobDescription, int noOfPositions, int yearsOfExperience, int employeeId) {
        this.jobId = jobId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.jobLocation = jobLocation;
        this.packageAmount = packageAmount;
        this.jobDescription = jobDescription;
        this.noOfPositions = noOfPositions;
        this.yearsOfExperience = yearsOfExperience;
        this.employeeId = employeeId;
    }



        // Constructors, getters, and setters

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public float getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(float packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public int getNoOfPositions() {
        return noOfPositions;
    }

    public void setNoOfPositions(int noOfPositions) {
        this.noOfPositions = noOfPositions;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }


}


