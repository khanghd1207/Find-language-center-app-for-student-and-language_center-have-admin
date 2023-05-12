package com.example.languagecenter;

public class Students {
    private int StudentID, OTP;
    private String Username, PWD;

    public Students(int studentID, int OTP, String username, String PWD) {
        StudentID = studentID;
        this.OTP = OTP;
        Username = username;
        this.PWD = PWD;
    }

    public int getStudentID() {
        return StudentID;
    }


    public int getOTP() {
        return OTP;
    }

    public void setOTP(int OTP) {
        this.OTP = OTP;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPWD() {
        return PWD;
    }

    public void setPWD(String PWD) {
        this.PWD = PWD;
    }
}
