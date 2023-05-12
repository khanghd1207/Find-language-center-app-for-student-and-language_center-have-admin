package com.example.languagecenter;

public class Language_Center {
    private int LangID, OTP, PostID;
    private String Username, PWD;

    public Language_Center(int langID, int OTP, int postID, String username, String PWD) {
        LangID = langID;
        this.OTP = OTP;
        PostID = postID;
        Username = username;
        this.PWD = PWD;
    }

    public int getLangID() {
        return LangID;
    }


    public int getOTP() {
        return OTP;
    }

    public void setOTP(int OTP) {
        this.OTP = OTP;
    }

    public int getPostID() {
        return PostID;
    }

    public void setPostID(int postID) {
        PostID = postID;
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
