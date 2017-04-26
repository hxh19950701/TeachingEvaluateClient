package com.hxh19950701.teachingevaluateclient.bean.response;


import java.util.List;

public class Department extends TimeMakableRecord {

    private String name;
    private List<Subject> subjects;

    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return name;
    }
}