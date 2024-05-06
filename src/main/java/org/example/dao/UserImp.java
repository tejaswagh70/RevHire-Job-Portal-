package org.example.dao;

import org.example.Main;
import org.example.model.EmployerModel;
import org.example.model.JobApplication;
import org.example.model.JobSeekerModel;
import org.example.model.JobOpeningModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserImp implements User {

    private static final Logger logger = LoggerFactory.getLogger(UserImp.class);
    private static UserImp instance;
    private final Connection connection;
    Scanner sc=new Scanner(System.in);
    private  int userid;
    private  int employerid;

    // Private constructor to prevent external instantiation
    private UserImp(Connection connection) {
        this.connection = connection;
    }

    public static  UserImp getJobSeekerInstance(Connection connection) {
        if (instance == null) {
            instance = new UserImp(connection);
        }
        return instance;
    }

    @Override
    public void JobSeekerRegister(JobSeekerModel jobSeekerModel) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO JOBSEEKER (UserName, dob,email,password) VALUES (?, ?, ?, ?)");

            preparedStatement.setString(1, jobSeekerModel.getUserName());
            preparedStatement.setString(2, jobSeekerModel.getDob());
            preparedStatement.setString(3, jobSeekerModel.getEmail());
            preparedStatement.setString(4, jobSeekerModel.getPassword());
            int res=preparedStatement.executeUpdate();
            if(res>0){
                System.out.println("User has Registered Successfully");
                logger.info("Job Seeker Registered");
                validateJobSeekerLogin();
            }
            else{
                System.out.println("Registration of the user is Failed!! Please Try Again by entering valid inputs");
                logger.warn("Registration Failed");
                JobSeekerRegister(jobSeekerModel);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void validateJobSeekerLogin() {
        System.out.println("Please Login");
        System.out.println("Enter Your Name");
        String userName=sc.next();
        System.out.println("Enter Your Password");
        String password=sc.next();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM JOBSEEKER WHERE UserName=? AND Password=?");
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,password);
            ResultSet res=preparedStatement.executeQuery();
            if(res.next()){
                this.userid=res.getInt("userID");
                logger.info("User Logged In");
                JobSeekerLogin();
            }
            else{
                System.out.println("Please Enter Valid Details");
                logger.warn("Wrong Credentials");
                validateJobSeekerLogin();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public void JobSeekerLogin() {
        int choice = 0;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome");
            System.out.println("1. Get Job Openings");
            System.out.println("2. Get My Applications");
            System.out.println("3. Exit");

            try {
                choice = sc.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Error: Please enter a valid choice (1-3)");
                    sc.nextLine(); // Clear the input buffer
                    continue; // Ask for input again
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a number");
                sc.nextLine(); // Clear the input buffer
                continue; // Ask for input again
            }

            // Perform action based on the validated choice
            switch (choice) {
                case 1:
                    // Display all job openings
                    List<JobOpeningModel> allJobs = getAllJobs();
                    displayJobList(allJobs);
                    // Provide filter option
                    System.out.println("Do you want to filter the job list? (Y/N)");
                    String filterChoice = sc.next();
                    if (filterChoice.equalsIgnoreCase("Y")) {
                        getJobs(); // Implement filter method
                    }
                    break;
                case 2:
                    myApplication();
                    break;
                case 3:
                    System.exit(0);
                    break;
            }
        }
    }

    private void displayJobList(List<JobOpeningModel> jobOpenings) {
        for (JobOpeningModel emp : jobOpenings) {
            System.out.println("ID: " + emp.getJobId() + " Company Name " + emp.getCompanyName() + " Job Role " + emp.getJobRole() + " Job Location " + emp.getJobLocation() + " Package " + emp.getPackageAmount() + " Job Description " + emp.getJobDescription() + " No.Of Positions " + emp.getNoOfPositions() + " Years Of Experience " + emp.getYearsOfExperience() + " Posted By " + emp.getEmployeeId());
        }
    }

    private List<JobOpeningModel> getAllJobs() {
        List<JobOpeningModel> jobOpenings = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM JobOpenings");
            while (resultSet.next()) {
                jobOpenings.add(new JobOpeningModel(
                        resultSet.getInt("Jobid"),
                        resultSet.getString("CompanyName"),
                        resultSet.getString("JobRole"),
                        resultSet.getString("jobLocation"),
                        resultSet.getInt("Package"),
                        resultSet.getString("jobDescription"),
                        resultSet.getInt("noOfPositions"),
                        resultSet.getInt("YearsOfExperience"),
                        resultSet.getInt("EmployeeID")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobOpenings;
    }

    @Override
    public void getJobs() {
        List<JobOpeningModel> jobOpenings = new ArrayList<>();
        System.out.println("Enter Details To Filter");
        System.out.println("Enter Company Name");
        String companyName=sc.next();
        System.out.println("Enter Location");
        String location=sc.next();
        System.out.println("Enter Experience");
        int experience=sc.nextInt();
        System.out.println("Enter Job Role");
        String jobRole=sc.next();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM JobOpenings Where CompanyName=? AND jobLocation=? AND YearsOfExperience=? AND JobRole=? ");
            statement.setString(1,companyName);
            statement.setString(2,location);
            statement.setInt(3,experience);
            statement.setString(4,jobRole);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                jobOpenings.add(new JobOpeningModel(
                        resultSet.getInt("Jobid"),
                        resultSet.getString("CompanyName"),
                        resultSet.getString("JobRole"),
                        resultSet.getString("jobLocation"),
                        resultSet.getInt("Package"),
                        resultSet.getString("jobDescription"),
                        resultSet.getInt("noOfPositions"),
                        resultSet.getInt("YearsOfExperience"),
                        resultSet.getInt("EmployeeID")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (JobOpeningModel emp : jobOpenings) {
            System.out.println("ID: " + emp.getJobId()+" Company Name "+emp.getCompanyName()+" Job Role "+emp.getJobRole()+" Job Location "+emp.getJobLocation()+" Package "+emp.getPackageAmount()+" Job Description "+emp.getJobDescription()+" No.Of Positions "+emp.getNoOfPositions()+" Years Of Experience "+emp.getYearsOfExperience()+" Posted By "+emp.getEmployeeId());
        }
        System.out.println("1.Apply For The Job");
        System.out.println("2.Go To Main Menu");
        int choice=sc.nextInt();
        switch (choice){
            case 1:
                applyForJob();
                break;
            case 2:
                JobSeekerLogin();
                break;
            default:
                System.out.println("Please Enter Valid Choice");
                JobSeekerLogin();
        }
    }


    @Override
    public void myApplication() {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Jobapplication Where userid=? ");
            preparedStatement.setInt(1,userid);
            ResultSet result=preparedStatement.executeQuery();
            List <JobApplication> jobApplications=new ArrayList<>();
            while (result.next()){
                jobApplications.add(new JobApplication(
                        result.getInt("Appid"),
                        result.getInt("jobid"),
                        result.getInt("userid"),
                        result.getString("appstatus")
                ));
            }
            System.out.println("Your Applications");
            for (JobApplication job:jobApplications){
                System.out.println("Application: " + job.getAppId()+" Job ID "+job.getJobId()+" Application Status "+job.getAppstatus());
            }
            System.out.println("1.Withdraw Application");
            System.out.println("2.Main Menu");
            int choice=sc.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Enter Application ID To Withdraw");
                    int appId=sc.nextInt();
                    preparedStatement=connection.prepareStatement("DELETE FROM JobApplication Where Appid=?;");
                    preparedStatement.setInt(1,appId);
                    int rows=preparedStatement.executeUpdate();
                    if(rows>0){
                        System.out.println("Successfully Unregistered");
                        logger.info("Withdrawn His Applications");
                        JobSeekerLogin();
                    }
                    else{
                        System.out.println("Withdraw Failed");
                        JobSeekerLogin();
                    }
                    break;
                case 2:
                    JobSeekerLogin();
                    break;
                default:
                    System.out.println("Enter Valid Choice");
                    myApplication();
                    break;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void applyForJob(){
        System.out.println("Enter Job ID");
        int job_id=sc.nextInt();
        try {
            PreparedStatement findEmployeeId= connection.prepareStatement("select EmployeeID from Jobopenings where jobid=?");
            findEmployeeId.setInt(1, job_id);
            ResultSet empResult = findEmployeeId.executeQuery();
            if (empResult.next()) {
                int employee_id = empResult.getInt("EmployeeID");
                //System.out.println(userid);
                //System.out.println(employee_id);
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO JobApplication Values(?,?,?,?)");
                    preparedStatement.setInt(1, employee_id);
                    preparedStatement.setInt(3, job_id);
                    preparedStatement.setInt(2, userid);
                    preparedStatement.setString(4, "Pending");
                    int rows = preparedStatement.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Applied Successfully");
                        logger.info("Applied For The Job");
                        JobSeekerLogin();
                    } else {
                        System.out.println("Application Failed");
                        JobSeekerLogin();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void EmployerRegister(EmployerModel employerModel) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Employee (EName, Department,Position,Eemail,CompanyName,password) VALUES (?,?, ?, ?, ?,?)");

            preparedStatement.setString(1,employerModel.getEname());
            preparedStatement.setString(2, employerModel.getDepartment());
            preparedStatement.setString(3, employerModel.getPosition());
            preparedStatement.setString(4, employerModel.getEemail());
            preparedStatement.setString(5, employerModel.getCompanyName());
            preparedStatement.setString(6, employerModel.getPassword());
            int res=preparedStatement.executeUpdate();
            if(res>0){
                System.out.println("User Registered Successfully");
                validateEmployerLogin();
            }
            else{
                System.out.println("Registration Failed");
                EmployerRegister(employerModel);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }


    @Override
    public void validateEmployerLogin() {
        System.out.println("Please Login");
        System.out.println("Enter Your Name");
        String userName=sc.next();
        System.out.println("Enter Your Password");
        String password=sc.next();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Employee WHERE EName=? AND password=?");
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,password);
            ResultSet res=preparedStatement.executeQuery();
            if(res.next()){
                this.employerid=res.getInt("EmployeeID");
                EmployerLogin();
            }
            else{
                System.out.println("Please Enter Valid Details");
                validateEmployerLogin();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    public  void EmployerLogin(){
        System.out.println("Welcome");
        System.out.println("Enter Your Choice");
        System.out.println("1.Post Job");
        System.out.println("2.View Applications");
        System.out.println("3.Delete Job");
        System.out.println("4.Exit");
        Scanner sc = new Scanner(System.in);
        int choice= sc.nextInt();

        switch (choice){
            case 1:
                postJob();
                break;
            case 2:
                viewApplications();
                break;
            case 4:
                System.exit(0);
                break;
            case 3:
                deleteJob();
                break;
            default:
                System.out.println("Enter Valid Choice");
                break;
        }

    }


    @Override
    public void viewApplications() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Jobapplication Where EmployeeID=?");
            preparedStatement.setInt(1,employerid);
            ResultSet result=preparedStatement.executeQuery();
            List <JobApplication> jobApplications=new ArrayList<>();
            while (result.next()){
                jobApplications.add(new JobApplication(
                        result.getInt("Appid"),
                        result.getInt("jobid"),
                        result.getInt("userid"),
                        result.getString("appstatus")
                ));
            }
            System.out.println("Applications");
            for (JobApplication job:jobApplications){
                System.out.println("Application ID: " + job.getAppId()+" Job ID "+job.getJobId()+" Application Status "+job.getAppstatus());
            }
            System.out.println("1.Reject");
            System.out.println("2.ShortList");
            System.out.println("3.Main Menu");
            int choice=sc.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Enter Application ID To Reject");
                    int appId=sc.nextInt();
                    preparedStatement=connection.prepareStatement("UPDATE JobApplication SET appStatus='Rejected' Where Appid=?");
                    preparedStatement.setInt(1,appId);
                    int rows=preparedStatement.executeUpdate();
                    if(rows>0){
                        System.out.println("Successfully Updated");
                        viewApplications();
                    }
                    else{
                        System.out.println("Failed");
                        viewApplications();
                    }
                    break;
                case 2:
                    System.out.println("Enter Application ID To Shortlist");
                    int applicationId=sc.nextInt();
                    preparedStatement=connection.prepareStatement("UPDATE JobApplication SET appStatus='Shortlisted' Where Appid=?");
                    preparedStatement.setInt(1,applicationId);
                    int resultRows=preparedStatement.executeUpdate();
                    if(resultRows>0){
                        System.out.println("Successfully Updated");
                        viewApplications();
                    }
                    else{
                        System.out.println("Failed");
                        viewApplications();
                    }
                    break;
                case 3:
                    EmployerLogin();
                    break;
                default:
                    System.out.println("Enter Valid Choice");
                    myApplication();
                    break;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void postJob() {
        try{
            System.out.println("Enter Job ID:");
            int jobId = sc.nextInt();

            System.out.println("Enter Company Name:");
            String companyName = sc.next();

            System.out.println("Enter Job Role:");
            String jobRole = sc.next();

            System.out.println("Enter Job Location:");
            String jobLocation = sc.next();

            System.out.println("Enter Package Amount:");
            float packageAmount = sc.nextFloat();

            System.out.println("Enter Job Description:");
            String jobDescription = sc.next();

            System.out.println("Enter Number of Positions:");
            int noOfPositions = sc.nextInt();

            System.out.println("Enter Years of Experience:");
            int yearsOfExperience = sc.nextInt();

            JobOpeningModel jobOpeningModel=new JobOpeningModel(jobId,companyName,jobRole,jobLocation,packageAmount,jobDescription,noOfPositions,yearsOfExperience,employerid);
            // Prepare and execute the INSERT statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO JobOpenings (Jobid, CompanyName, JobRole, jobLocation, Package, jobDescription, NoOfPositions, YearsOfExperience, EmployeeID) VALUES (?, ?, ?, ?, CAST(? AS MONEY), ?, ?, ?, ?)");

            // Set values for each parameter in the INSERT statement
            statement.setInt(1, jobOpeningModel.getJobId());
            statement.setString(2, jobOpeningModel.getCompanyName());
            statement.setString(3, jobOpeningModel.getJobRole());
            statement.setString(4, jobOpeningModel.getJobLocation());
            statement.setFloat(5, jobOpeningModel.getPackageAmount());
            statement.setString(6, jobOpeningModel.getJobDescription());
            statement.setInt(7, jobOpeningModel.getNoOfPositions());
            statement.setInt(8, jobOpeningModel.getYearsOfExperience());
            statement.setInt(9, jobOpeningModel.getEmployeeId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Job posted successfully.");
                EmployerLogin();
            } else {
                System.out.println("Failed to post job Try Again");
                postJob();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteJob() {
        System.out.println("Your Job Postings\n");
        List<JobOpeningModel> jobOpenings = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM JobOpenings where EmployeeID=?");
            statement.setInt(1,employerid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                jobOpenings.add(new JobOpeningModel(
                        resultSet.getInt("Jobid"),
                        resultSet.getString("CompanyName"),
                        resultSet.getString("JobRole"),
                        resultSet.getString("jobLocation"),
                        resultSet.getInt("Package"),
                        resultSet.getString("jobDescription"),
                        resultSet.getInt("noOfPositions"),
                        resultSet.getInt("YearsOfExperience"),
                        resultSet.getInt("EmployeeID")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (JobOpeningModel emp : jobOpenings) {
            System.out.println("ID: " + emp.getJobId()+" Company Name "+emp.getCompanyName()+" Job Role "+emp.getJobRole()+" Job Location "+emp.getJobLocation()+" Package "+emp.getPackageAmount()+" Job Description "+emp.getJobDescription()+" No.Of Positions "+emp.getNoOfPositions()+" Years Of Experience "+emp.getYearsOfExperience()+" Posted By "+emp.getEmployeeId());
        }
        System.out.println("Enter Job ID To Delete");
        int jobId=sc.nextInt();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Delete from jobOpenings where jobid=?");
            preparedStatement.setInt(1,jobId);
            int rows=preparedStatement.executeUpdate();
            if(rows>0){
                System.out.println("Deleted Successfully");
                deleteJob();
            }
            else{
                System.out.println("Failed To Delete");
                deleteJob();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}

