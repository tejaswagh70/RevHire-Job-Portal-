package org.example;

import org.example.dao.User;
import org.example.dao.UserImp;
import org.example.model.EmployerModel;
import org.example.model.JobSeekerModel;
import org.example.service.EmployerService;
import org.example.service.JobSeekerService;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Connection connection;
        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String url = "jdbc:sqlserver://localhost;databaseName=Revhire;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url);
            logger.info("Connected to the database.");

            User userDAO = UserImp.getJobSeekerInstance(connection);
            EmployerService employerService = new EmployerService(userDAO);
            JobSeekerService jobSeekerService = new JobSeekerService(userDAO);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("WELCOME TO REVHIRE PORTAL");
                System.out.println("\nMay i know Who You Are:");
                System.out.println("Enter 1 if u are a Job Seeker");
                System.out.println("Enter 2 if u are A Employee");
                System.out.println("Press 3 to exit");

                int choice;
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input Format");
                    scanner.nextLine(); // Clear the invalid input
                    continue; // Restart the loop to ask for input again
                }

                switch (choice) {
                    case 1:
                        System.out.println("Enter Your Choice To Proceed");
                        System.out.println("1.Login");
                        System.out.println("2.Register");
                        int innerJsChoice;
                        try {
                            innerJsChoice = scanner.nextInt();
                        } catch (InputMismatchException ex) {
                            System.out.println("Invalid Input Format");
                            scanner.nextLine(); // Clear the invalid input
                            break; // Continue to the outer loop
                        }

                        switch (innerJsChoice) {
                            case 1:
                                jobSeekerService.validateJobSeekerLogin();
                                break;
                            case 2:
                                // Job Seeker Registration
                                System.out.println("Enter Your User Name:");
                                String userName = scanner.next();
                                String dob;
                                String invalidDobMessage;
                                do {
                                    System.out.println("Enter Your Date Of Birth (dd/mm/yyyy):");
                                    dob = scanner.next();
                                    invalidDobMessage = isValidDOBMessage(dob);
                                    if (!invalidDobMessage.isEmpty()) {
                                        System.out.println(invalidDobMessage);
                                    }
                                } while (!invalidDobMessage.isEmpty());

                                System.out.println("Enter Your User Email:");
                                String email = scanner.next();
                                while (!isValidEmail(email)) {
                                    System.out.println("Invalid email format. Enter a valid email:");
                                    email = scanner.next();
                                }

                                System.out.println("Enter Your User Password:");
                                String password = scanner.next();

                                // Create Model JobSeeker Object
                                JobSeekerModel jobSeekerModel = new JobSeekerModel(userName, dob, email, password);
                                jobSeekerService.JobSeekerRegister(jobSeekerModel);
                                break;
                            default:
                                System.out.println("Enter Valid Choice");
                        }
                        break;
                    case 2:
                        System.out.println("Enter Your Choice To Proceed");
                        System.out.println("1.Login");
                        System.out.println("2.Register");
                        int innerEmpChoice;
                        try {
                            innerEmpChoice = scanner.nextInt();
                        } catch (InputMismatchException ex) {
                            System.out.println("Invalid Input Format");
                            scanner.nextLine(); // Clear the invalid input
                            break; // Continue to the outer loop
                        }

                        switch (innerEmpChoice) {
                            case 1:
                                employerService.validateEmployerLogin();
                                break;
                            case 2:
                                // Employer Registration
                                System.out.println("Enter Your User Name:");
                                String user_name=scanner.next();
                                System.out.println("Enter Your Department");
                                String dob=scanner.next();
                                System.out.println("Enter Your Position");
                                String position=scanner.next();
                                System.out.println("Enter Your User Email:");
                                String email = scanner.next();
                                while (!isValidEmail(email)) {
                                    System.out.println("Invalid email format. Enter a valid email:");
                                    email = scanner.next();
                                }

                                System.out.println("Enter Your Company Name");
                                String company_name=scanner.next();
                                System.out.println("Enter Your User Password:");
                                String password=scanner.next();

                                EmployerModel employerModel=new EmployerModel(user_name,position,dob,email,company_name,password);
                                employerService.EmployerRegister(employerModel);
                                break;
                            default:
                                System.out.println("Enter Valid Choice");
                        }
                        break;
                    case 3:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Connection failed. Error: " + e.getMessage());
            System.out.println("Connection failed. Error: " + e.getMessage());
        }
    }

    private static String isValidDOBMessage(String dob) {
        // Regex for date validation (dd/mm/yyyy)
        String dobRegex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(dobRegex);
        Matcher matcher = pattern.matcher(dob);
        if (matcher.matches()) {
            return ""; // Valid date format
        } else {
            return "Invalid date format. Please enter the date in the format dd/mm/yyyy:";
        }
    }

    private static boolean isValidEmail(String email) {
        // Regex for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
