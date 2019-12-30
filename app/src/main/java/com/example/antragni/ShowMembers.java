package com.example.antragni;
import java.io.Serializable;

public class ShowMembers implements Serializable{

    String id;
    String email;
    String teacher;
    String attendence;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ShowMembers(String email, String teacher,String id,String attendence) {
        this.email = email;
        this.teacher = teacher;
        this.id = id;
        this.attendence = attendence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getAttendence() {
        return attendence;
    }

    public void setAttendence(String attendence) {
        this.attendence = attendence;
    }

    public ShowMembers() {
    }
}