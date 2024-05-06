package org.example.model;

public class EmployerModel {
    private String Ename;
    private String Department;

    private String Position;
    private String Eemail;
    private  String CompanyName;
    private  String Password;

    public EmployerModel(String ename, String department,String position, String eemail, String companyName, String password) {
        Ename = ename;
        Department = department;
        Position = position;
        Eemail = eemail;
        CompanyName = companyName;
        Password=password;

    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEname() {
        return Ename;
    }

    public void setEname(String ename) {
        Ename = ename;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getEemail() {
        return Eemail;
    }

    public void setEemail(String eemail) {
        Eemail = eemail;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
