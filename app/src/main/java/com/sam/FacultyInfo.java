package com.sam;

/**
 * Created by MOHD IMTIAZ on 29-Jan-17.
 */

public class FacultyInfo {
    public String facultyName, password, email, post, isMember;

    public FacultyInfo() {
    }

    public FacultyInfo(String email, String facultyName, String password, String post, String isMember) {
        this.email = email;
        this.facultyName = facultyName;
        this.password = password;
        this.post = post;
        this.isMember = isMember;
    }

    public String getEmail() {
        return email;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getPassword() {
        return password;
    }

    public String getPost() {
        return post;
    }
}
